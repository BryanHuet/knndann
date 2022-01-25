package classifier;
import elements.Element;
import elements.Iris;
import jeigen.DenseMatrix;

import java.util.*;

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
    public double pj(Element x0, ArrayList<Element> elementNear, int j){
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


    public static String proceed(Iris query, int k, HashSet<Iris> data){


        double h=-1000;



        /*
        for(int x =0; x<k;x++){
            System.out.println(result.get(x).getClasse() + " " + result.get(x).getDistance());
        }*/
        return "";
    }
}
