package parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;

import jeigen.DenseMatrix;

public class ElementParser {
    /**
     * Classe permettant de parcourir un fichier de type numérique, d'en extraire les données et de les préparer.
     * Les données extraites sont ensuite stockées dans un vecteur de type DenseMatrix de la bibliothèque jeigen.
     * Ce vecteur est enfin ajouté dans un objet de type Element qui représente un point de notre dataset.
     * La classe d'un point est représenté par un entier naturel, ajouté également dans l'objet Element.
     * @param path_to_file le chemin du fichier à parcourir.
     * @return la liste des points de notre dataset.
     */
    public static HashSet<Element> proceed(String path_to_file){
        HashSet<Element> iris_set = new HashSet<>();
        BufferedReader reader;
        try{
            reader = new BufferedReader(new FileReader(path_to_file));
            String line;
            int classe = -1;
            String classeName = "";
            double[][] tmpVector;
            while((line = reader.readLine())!= null){
                String[] attributes = line.split(",");
                tmpVector = new double[1][attributes.length];
                for(int j=0; j<attributes.length; j++){
                    if(j != attributes.length-1){
                        if(!"".equals(attributes[j])){
                            tmpVector[0][j] = Double.parseDouble(attributes[j]);
                        }
                    } else {
                        if(attributes[j] != classeName){
                            classeName = attributes[j];
                            classe += 1;
                        }
                    }
                }
                DenseMatrix vector = new DenseMatrix(tmpVector);
                iris_set.add(new Element(vector,classe));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return iris_set;
    }
}
