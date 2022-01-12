package classifier;
import elements.Iris;
import jeigen.DenseMatrix;

import java.util.*;

public class DANN {

    public static String majorClasse(List<Result> list, int k){
        String soluce="";
        return soluce;
    }

    public static double[] di(Iris xi, Iris x0){
        double[] result = new double[2];
        double distancePetal;
        double distanceSepal;

        distancePetal = Math.pow(xi.getPetal_length()-x0.getPetal_length(),2);
        distancePetal += Math.pow(xi.getPetal_width()- x0.getPetal_width(), 2);
        distanceSepal = Math.pow(xi.getSepal_length()- x0.getPetal_length(), 2);
        distanceSepal += Math.pow(xi.getSepal_width()- x0.getSepal_width(), 2);

        result[0]=distancePetal;
        result[1]=distanceSepal;

        return result;
    }

    public static double hi(double[] di){
        double h=-100000000;
        for (double v : di) {
            if (h < v) {
                h = v;
            }
        }
        return h;
    }

    public static double[] k(Iris x0, Iris xi){
         double[] result = new double[2];
         double[] di=di(xi,x0);
         double h = hi(di);
         result[0]= di[0]<h ? Math.pow((1-Math.pow(di[0]/h,3))*di[0],3)
                            : Math.pow((1-Math.pow(di[0]/h,3)),3);
         result[1]= di[1]<h ? Math.pow((1-Math.pow(di[1]/h,3))*di[1],3)
                : Math.pow((1-Math.pow(di[1]/h,3)),3);
         return result;
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
