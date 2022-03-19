import classifier.DANN;
import classifier.KNN;
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
            throw new IllegalArgumentException("Usage : App < chemin fichier > < ensemble des k voisins à tester > < nombre de découpes CV >"+
            "\n"+"ex: java App /data/iris.data 3,5,8,5 5");
        }
        if(args.length != 3){
            throw new IllegalArgumentException("Le nombre d'arguments est incorrect");
        }


        String chemin = args[0]; 
        //int k = Integer.parseInt(args[1]);//3

        ArrayList<Integer> k = new ArrayList<>();
        for (String s : args[1].split(" ")) {
            try {
                int number = Integer.parseInt(s);
                if(number < 1) {
                    throw new ArithmeticException("k doit comporter uniquement des nombres >= à 1");
                }
                k.add(number);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        int nb_blocs = Integer.parseInt(args[2]);

        if(nb_blocs < 2){
            throw new IllegalArgumentException("Le nombre de blocs ne peut pas être < à 2");
        }

        NumericalParser data_parse = new NumericalParser(chemin);
        HashSet<Element> data = data_parse.proceed();

        if(nb_blocs > data.size()/2){
            throw new IllegalArgumentException("Le nombre de blocs ne peut pas être > à la longueur du dataset/2");
        }

        CrossValidation bc = new CrossValidation(data);
        bc.crossValidation(nb_blocs, data_parse.getParametersNumber(), data_parse.getClassNumber(), new HashSet<>(k));
        
        // DenseMatrix query = new DenseMatrix(new double[][]{{5.6, 3, 4.5, 1.5}});
        // Element x0 = new Element(query,0);

        // DANN dann = new DANN(data, k, 3, data_parse.getParametersNumber(), data_parse.getClassNumber());


        // System.out.println("Nombre de donnee "+data.size());

        // CrossValidation cv = new CrossValidation(data);
        // cv.crossValidation(nb_blocs,dann);
        // System.out.println(cv.getScore());


    }
}
