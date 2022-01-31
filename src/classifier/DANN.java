package classifier;
import elements.Element;
import elements.Iris;
import jeigen.DenseMatrix;

import java.util.*;

import static jeigen.DenseMatrix.ones;
import static jeigen.DenseMatrix.zeros;

public class DANN {

    public static int NB_PARAMETERS = 4;

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
                moy.add(e.getVector());
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

        for(int j=0; j<NB_PARAMETERS; j++){
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
        for(int j=0;j<NB_PARAMETERS;j++){
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


    public static String proceed(Iris query, int k, HashSet<Iris> data){


        double h=-1000;



        /*
        for(int x =0; x<k;x++){
            System.out.println(result.get(x).getClasse() + " " + result.get(x).getDistance());
        }*/
        return "";
    }
}
