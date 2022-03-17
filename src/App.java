import classifier.DANN;
import classifier.KNN;
import elements.DataIrisLecture;
import elements.NumericalParser;
import elements.Element;
import jeigen.DenseMatrix;
import validator.CrossValidation;

import java.util.*;

import static jeigen.DenseMatrix.diag;
import static jeigen.Shortcuts.rand;
import static jeigen.Shortcuts.spdiag;

public class App {

    public static void main(String[] args){




        if(args.length == 0){
            throw new IllegalArgumentException("Usage : App < chemin fichier > < k > < nombre de découpes CV >"+"\n"+"ex: java App /data/iris.data 3 5");
        }
        if(args.length != 3){
            throw new IllegalArgumentException("Le nombre d'arguments est incorrect");
        }



        String chemin = args[0]; 
        int k=Integer.parseInt(args[1]);//3
        int nb_blocs = Integer.parseInt(args[2]);


        if(k < 1){
            throw new ArithmeticException("k doit être un nombre supérieur à 1");
        }




        /*
        String[] classes = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", 
            "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};*/
        NumericalParser data_parse = new NumericalParser(chemin);

        //"data/iris.data"


        HashSet<Element> data = data_parse.proceed();

        //DenseMatrix query = new DenseMatrix( new double[][]{{5.0, 1.4, 5.5, 0.2,2,4,5,7,8,10,4,1,2,3,4,7}});
        DenseMatrix query = new DenseMatrix(new double[][]{{5.6, 3, 4.5, 1.5,1.1,2}});
        Element x0 = new Element(query,0);

        DANN dann = new DANN(data, k, 1, data_parse.getParametersNumber(), data_parse.getClassNumber());
        //int result_DANN = dann.proceed(x0);


        System.out.println("");

        System.out.println("\n----------- CLASSIFICATION DE x0 ------------");


        System.out.println("---------------------------------------------");
      
        System.out.println("Nombre de donne "+data.size());
        CrossValidation cv = new CrossValidation(data);


        cv.crossValidation(nb_blocs,dann);
        System.out.println(cv.getScore());
 

        //System.out.println(DANN.Wdiag(DANN.W(new ArrayList<>(know_iris),x0)));

        //System.out.println(test.shape().getValues()[0]);



          //kfold, pour l'études des tests
          //transformer w en mat diagonals





    }
}
