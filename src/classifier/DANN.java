package classifier;
import elements.Element;
import jeigen.DenseMatrix;

import java.util.*;

import static jeigen.DenseMatrix.zeros;

public class DANN {

    public static double EPSILON = 1.0;

    private int nb_classes;
    private int nb_parameters;
    private HashSet<Element> dataset;
    private int k;
    private int nb_iteration;


    
    
    //(Element query, int k, int nb_iteration, int parameters,int class_number,HashSet<Element> data)
       

    public DANN(HashSet<Element> dataset, int k, int nb_iteration, int nb_parameters, int nb_classes){
        this.nb_classes=nb_classes;
        this.nb_parameters=nb_parameters;
        this.dataset=dataset;
        this.k=k;
        this.nb_iteration=nb_iteration;

    }

    public void setDataset(HashSet<Element> dataset){
        this.dataset = dataset;
    }

    /**
     *
     * @param xi
     * @param x0
     * @return double,
     * Calcul la distance euclidienne entre le vecteur xi et x0.
     * Correspond à la formule di du point (6)
     * */
    public static double distanceEuclidienne(Element xi, Element x0){
        double result=0;
        DenseMatrix calcul = xi.getVector().sub(x0.getVector());
        calcul = calcul.mul(calcul);
        calcul = calcul.sumOverCols();
        result = calcul.getValues()[0];
        return Math.sqrt(result);
    }

    /**
     *
     * @param elements
     * @return double
     * Correspond à la formule h du point (6)
     */
    public static double max(Element x0,ArrayList<Element> elements){
        double h=-100000000;
        ArrayList<Double> di = new ArrayList<>();

        for(Element e: elements){
            di.add(distanceEuclidienne(e,x0));
        }

        for (double v : di) {
            if (h < v) {
                h = v;
            }
        }
        return h;
    }

    /**
     *
     * @param xi
     * @param x0
     * @param h
     * @return Matrice
     * Correspond à la formule k du point (7)
     *//*
    public static DenseMatrix weights(Element xi, Element x0, double h){
         DenseMatrix result = DenseMatrix.eye(NB_PARAMETERS);
         double value = Math.pow(1 - (Math.pow(distanceEuclidienne(xi,x0)/h,3)),3);
         result = result.mul(value);
         return result;
    }*/
    public static double weights(Element xi, Element x0, double h){
        return Math.pow(1 - (Math.pow(distanceEuclidienne(xi,x0)/h,3)),3);
    }

    /**
     *
     * @param x0
     * @param elementNear
     * @param j
     * @return double
     * Correspond à la formule pj du point (8)
     */
    private double pj(Element x0, ArrayList<Element> elementNear, int j){
        double numerator=0;
        double denominator=1;
        for(Element e1: elementNear){
            if (e1.getClasse()==j){
                numerator+=weights(e1,x0,max(x0,elementNear));
            }
            denominator+=weights(e1,x0,max(x0,elementNear));
        }
        return numerator/denominator;
    }

    private DenseMatrix moyenneXj(ArrayList<Element> elementNear,int j){
        DenseMatrix moy = zeros(1,this.nb_parameters);
        for (Element e: elementNear){
            if (e.getClasse()==j){
                moy = moy.add(e.getVector());
            }
        }
        return  moy.div(elementNear.size());
    }
    private DenseMatrix moyenneX(ArrayList<Element> elementNear){
        DenseMatrix moy = zeros(1,this.nb_parameters);
        for (Element e: elementNear){
                moy = moy.add(e.getVector());
        }
        return  moy.div(elementNear.size());
    }

    private DenseMatrix B(ArrayList<Element> elementNear, Element x0){
        DenseMatrix matrice = zeros(this.nb_parameters,this.nb_parameters);
        for(int j=0; j<this.nb_classes; j++){
            double pj = pj(x0,elementNear,j);
            DenseMatrix moy = moyenneX(elementNear).sub(moyenneXj(elementNear,j));
            moy = moy.t().mmul(moy);
            matrice = matrice.add(moy.mul(pj));
        }
        return matrice;
    }

    private DenseMatrix W(ArrayList<Element> elementNear, Element x0){
        DenseMatrix matrice = zeros(this.nb_parameters,this.nb_parameters);
        double total_weigth=0;
        for(int j=0;j<this.nb_classes;j++){
            for (Element e: elementNear) {
                if(e.getClasse()==j){
                    DenseMatrix current = e.getVector().sub(moyenneXj(elementNear,j));
                    current = current.t().mmul(current);
                    matrice=matrice.add(current.mul(weights(e,x0,max(x0,elementNear))));
                }
            }
        }
        for(Element e:elementNear){
            total_weigth += weights(e,x0,max(x0,elementNear));
        }
        return matrice.div(total_weigth);
    }

    private DenseMatrix Wdiag(DenseMatrix W){
        W = DenseMatrix.eye((int) (W.shape().getValues()[0])).mul(W);
        W = W.sqrt();
        W = W.recpr();
        for(int i=0; i<W.shape().getValues()[0];i++){
            for(int j=0; j< W.shape().getValues()[0];j++){
                if(W.get(i,j)>1000000){
                    W.set(i,j,0);
                }
                if(i!=j){
                    W.set(i,j,0);
                }
            }
        }
        //W = W.sub(1);
        return W;
    }


    private double DANN_distance(Element x0, Element x, DenseMatrix sigma){
        DenseMatrix difference = x0.getVector().sub(x.getVector());
        return (difference.mmul(sigma)).mmul(difference.t()).getValues()[0];
    }

    private ArrayList<Element> nearest_neighbor(Element x0, DenseMatrix sigma){
        ArrayList<Element> near = new ArrayList<>();
        TreeMap<Double, Element> map = new TreeMap<>();
        for(Element e: this.dataset){
            map.put(this.DANN_distance(x0,e,sigma),e);
        }

        for (int i=0;i<this.k;i++){
            if(map.firstEntry()!=null){
                near.add(map.pollFirstEntry().getValue());
            }
        }
        return near;
    }

    /**
     *
     * @param list
     * @param k
     * @return
     * Returne la classe majoritaire dans la liste d'element.
     */
    private int majorClasseD(List<Element> list,int k) {
        String soluce = "";
        HashMap<String, Integer> dico = new HashMap<>();

        for (int i = 0; i < list.size(); i++) {

            String classe = ""+list.get(i).getClasse();

            if (dico.containsKey(classe)) {
                dico.put(classe, dico.get(classe) + 1);

            } else {

                dico.put(classe, 1);
            }
        }

            for (Map.Entry<String, Integer> entry : dico.entrySet()) {

                if (entry.getValue().equals(Collections.max(dico.values()))) {
                    soluce = entry.getKey();
                }
            }

        return Integer.parseInt(soluce);
    }



    public int proceed(Element query){


        DenseMatrix sigma = DenseMatrix.eye(this.nb_parameters);
        ArrayList<Element> near = nearest_neighbor(query,sigma);

        for(int i=0; i<nb_iteration;i++){
            DenseMatrix B = B(near,query);
            DenseMatrix W = W(near,query);

            W = Wdiag(W);
            //B = DenseMatrix.eye((int) (B.shape().getValues()[0])).mul(B);

            sigma = W.mmul(
                    W.mmul(B.mmul(W)).add(DenseMatrix.eye(this.nb_parameters).mul(EPSILON))
            ).mmul(W);
            near = nearest_neighbor(query,sigma);
        }

        return majorClasseD(near,this.nb_classes);

    }
}
