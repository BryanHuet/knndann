import java.util.ArrayList;
import java.util.Random;

import data.DataSet;
import parser.*;

public class App {
    public static void main(String[] args) throws Exception {
        if(args.length == 0){
            throw new IllegalArgumentException("Usage : App < chemin fichier > < k >"+"\n"+"ex: java App /data/iris.data 3");
        }
        if(args.length != 2){
            throw new IllegalArgumentException("Le nombre d'arguments est incorrect");
        }
        Parser numerique = new NumericalParser();
        String chemin = args[0];//"D:/Partage/Master/Projet/knndann/data/iris.data"; //"/data/iris.data";
        ArrayList<ArrayList<String>> data = numerique.parserFichier(chemin);
        int[] separationDonnees;
        if(data.size()%2 == 0){
            separationDonnees = new int[]{0,(data.size()/2),data.size()}; // 0 - n | n - size
        } else {
            separationDonnees = new int[]{0,(data.size()/2),data.size()}; // 0 - n | n - size
        }
        DataSet bdd = new DataSet(data, separationDonnees, 4);
        int k = Integer.parseInt(args[1]);
        if(k < 1){
            throw new ArithmeticException("k doit être un nombre supérieur à 1");
        }
        DANN dann = new DANN(bdd,1);
    }
}
