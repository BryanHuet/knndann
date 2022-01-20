package parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class NumericalParser implements Parser {
    /**
     * Classe permettant de parcourir un fichier de type numérique et d'en extraire les données.
     * Les données extraites sont ensuite stockées dans des matrices ( DenseMatrix de la bibliothèque jeigen ).
     */

    @Override
    public ArrayList<ArrayList<String>> parserFichier(String chemin) {
        // Le nombre de lignes du fichier représentent le nombre d'exemples.
        // Le nombre de colonnes, le nombre de paramètres.
        ArrayList<ArrayList<String>> donneesExtraites = new ArrayList<>();
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(chemin));
            String ligne;
            String features[];
            int i=0; // indice de ligne pour y placer les éléments
            while((ligne = br.readLine()) != null){
                //System.out.println("Ligne lue : "+ligne);
                features = ligne.split(",");
                ArrayList<String> example = new ArrayList<>();                    
                for(int j=0; j<features.length; j++){
                    if(!"".equals(features[j])){
                        example.add(j, features[j]);
                        //System.out.println("ex: "+example.toString());
                    }
                }
                donneesExtraites.add(i, example);
                i++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return donneesExtraites;
    }    
}