import classifier.DANN;
import classifier.KNN;
import elements.DataIrisLecture;
import elements.NumericalParser;
import elements.Element;
import jeigen.DenseMatrix;
import validator.BetaCV2;
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
        int k = Integer.parseInt(args[1]);//3
        int nb_blocs = Integer.parseInt(args[2]);


        if(k < 1) {
            throw new ArithmeticException("k doit être un nombre supérieur à 1");
        }
        if(nb_blocs < 2){
            throw new IllegalArgumentException("Le nombre de blocs ne peut pas être < à 2");
        }

        NumericalParser data_parse = new NumericalParser(chemin);
        HashSet<Element> data = data_parse.proceed();

        if(nb_blocs > data.size()/2){
            throw new IllegalArgumentException("Le nombre de blocs ne peut pas être > à la longueur du dataset/2");
        }

        BetaCV2 bc = new BetaCV2(data);
        bc.crossValidation(nb_blocs, data_parse.getParametersNumber(), data_parse.getClassNumber(), new HashSet<>(Arrays.asList(1,3,15,25,50,100)));
        
        // DenseMatrix query = new DenseMatrix(new double[][]{{5.6, 3, 4.5, 1.5}});
        // Element x0 = new Element(query,0);

        // DANN dann = new DANN(data, k, 3, data_parse.getParametersNumber(), data_parse.getClassNumber());


        // System.out.println("Nombre de donnee "+data.size());

        // CrossValidation cv = new CrossValidation(data);
        // cv.crossValidation(nb_blocs,dann);
        // System.out.println(cv.getScore());


    }
}
