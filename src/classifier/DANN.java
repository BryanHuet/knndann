package classifier;
import elements.Element;
import jeigen.DenseMatrix;

import java.util.*;

import static jeigen.DenseMatrix.zeros;

public class DANN {

    public static int NB_PARAMETERS = 4;
    public static int NB_CLASSES = 3;
    public static double EPSILON = 1.0;

    public static String majorClasse(List<Result> list, int k){
        String soluce="";
        return soluce;
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
    public static double pj(Element x0, ArrayList<Element> elementNear, int j){
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

    public static DenseMatrix moyenneXj(ArrayList<Element> elementNear,int j){
        DenseMatrix moy = zeros(1,NB_PARAMETERS);
        for (Element e: elementNear){
            if (e.getClasse()==j){
                moy = moy.add(e.getVector());
            }
        }
        return  moy.div(elementNear.size());
    }
    public static DenseMatrix moyenneX(ArrayList<Element> elementNear){
        DenseMatrix moy = zeros(1,NB_PARAMETERS);
        for (Element e: elementNear){
                moy = moy.add(e.getVector());
        }
        return  moy.div(elementNear.size());
    }

    public static DenseMatrix B(ArrayList<Element> elementNear, Element x0){
        DenseMatrix matrice = zeros(NB_PARAMETERS,NB_PARAMETERS);
        for(int j=0; j<NB_CLASSES; j++){
            double pj = pj(x0,elementNear,j);
            DenseMatrix moy = moyenneX(elementNear).sub(moyenneXj(elementNear,j));
            moy = moy.t().mmul(moy);
            matrice = matrice.add(moy.mul(pj));
        }
        return matrice;
    }

    public static DenseMatrix W(ArrayList<Element> elementNear, Element x0){
        DenseMatrix matrice = zeros(NB_PARAMETERS,NB_PARAMETERS);
        double total_weigth=0;
        for(int j=0;j<NB_CLASSES;j++){
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

    public static DenseMatrix Wdiag(DenseMatrix W){
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


    public static double DANN_distance(Element x0, Element x, DenseMatrix sigma){
        DenseMatrix difference = x0.getVector().sub(x.getVector());
        return (difference.mmul(sigma)).mmul(difference.t()).getValues()[0];
    }

    public static ArrayList<Element> nearest_neighbor(Element x0, DenseMatrix sigma, HashSet<Element> data, int k){
        ArrayList<Element> near = new ArrayList<>();
        TreeMap<Double, Element> map = new TreeMap<>();
        for(Element e: data){
            map.put(DANN_distance(x0,e,sigma),e);
        }
        for (int i=0;i<k;i++){
            near.add(map.pollFirstEntry().getValue());
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
    public static int majorClasseD(List<Element> list,int k) {
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
                System.out.println("DANN classe :"+entry.getKey()+" nb :"+entry.getValue());

                if (entry.getValue().equals(Collections.max(dico.values()))) {
                    soluce = entry.getKey();
                }
            }

        return Integer.parseInt(soluce);
    }



    public static int proceed(Element query, int k, int nb_iteration, HashSet<Element> data){

        DenseMatrix sigma = DenseMatrix.eye(NB_PARAMETERS);

        ArrayList<Element> near = nearest_neighbor(query,sigma,data,k);

        for(int i=0; i<nb_iteration;i++){
            DenseMatrix B = DANN.B(near,query);
            DenseMatrix W = DANN.W(near,query);

            W = DANN.Wdiag(W);
            //B = DenseMatrix.eye((int) (B.shape().getValues()[0])).mul(B);


            sigma = W.mmul(
                    W.mmul(B.mmul(W)).add(DenseMatrix.eye(NB_PARAMETERS).mul(EPSILON))
            ).mmul(W);

            near = nearest_neighbor(query,sigma,data,k);
        }
        return majorClasseD(near,3);

    }
}
