package elements;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.HashMap;
import jeigen.DenseMatrix;

/**
 * Classe permettant de parcourir un fichier de type numérique, d'en extraire les données et de les préparer.
 * Les données extraites sont ensuite stockées dans un vecteur de type DenseMatrix de la bibliothèque jeigen.
 * Ce vecteur est enfin ajouté dans un objet de type Element qui représente un point de notre dataset.
 * La classe d'un point est représenté par un entier naturel, ajouté également dans l'objet Element.
 */
public class NumericalParser {

    private String path_to_file;
    private int class_number;
    private int nb_parameters;

    /**
     *
     * @param path_to_file le chemin du fichier à parcourir.
     */
    public NumericalParser(String path_to_file){
        this.path_to_file = path_to_file;
    }

    /**
     * @return l'ensemble des points de notre dataset.
     */
    public HashSet<Element> proceed(){
        HashSet<Element> iris_set = new HashSet<>();
        BufferedReader reader;
        try{
            reader = new BufferedReader(new FileReader(path_to_file));
            String line;
            int label = -1;
            int classNumber = 0;
            HashMap<String,Integer> known_class=new HashMap<>();
            double[][] featuresVector;
            while((line = reader.readLine())!= null){
               
                String[] attributes = line.split(",");
                featuresVector = new double[1][attributes.length-1];

                if( line.length() >0 && (line.charAt(0)!='%' &&  line.charAt(0)!='@')){


                    for(int j=0; j<attributes.length; j++){

                            if(j != attributes.length-1){
                                if(!"".equals(attributes[j])){
                                    featuresVector[0][j] = Double.parseDouble(attributes[j]);
                                }
                            } else {
                                if(known_class.containsKey(attributes[j])){
                                    label=known_class.get(attributes[j]);
                                }else{
                                    known_class.put(attributes[j],classNumber);
                                    classNumber+=1;
                                    label=classNumber;
                                }
                            }
                        
                    }

                    DenseMatrix vector = new DenseMatrix(featuresVector);
                    iris_set.add(new Element(vector,label));
                    this.nb_parameters = attributes.length-1;
                }
            }

            this.class_number=classNumber;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return iris_set;
    }

    public int getClassNumber(){
        return this.class_number;
    }
    public int getParametersNumber(){
        return this.nb_parameters;
    }
}
