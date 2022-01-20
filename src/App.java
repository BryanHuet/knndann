import java.util.ArrayList;
import java.util.Random;

import data.DataSet;
import data.Point;
import jeigen.DenseMatrix;
import parser.*;

public class App {
    // java App "/data/iris.data" 3
    public static void main(String[] args) throws Exception {
        Parser numerique = new NumericalParser();
        String chemin = "D:/Partage/Master/Projet/knndann/data/iris.data"; //"/data/iris.data";//args[0];
        ArrayList<ArrayList<String>> data = numerique.parserFichier(chemin);
        int[] separationDonnees = new int[]{0,50,100}; // 0 - n | n - size
        DataSet bdd = new DataSet(data, separationDonnees, 4);
        int k = 1;//Integer.parseInt(args[1]);
        if(k < 1){
            throw new ArithmeticException("k doit être un nombre supérieur à 1");
        }
        DANN dann = new DANN(bdd,1);
    }
}
