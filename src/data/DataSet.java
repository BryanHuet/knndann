package data;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jeigen.DenseMatrix;

//import jeigen.DenseMatrix;

public class DataSet {

    // m : nombre d'exemples ( lignes )
    // n : nombre de features ( colonnes )
    /**
     * @param X_train Matrice des caractéristiques de notre modèle ( features )
     * X est une matrice. Il est possible d'avoir plusieurs colonnes dans le cas où interviennent plusieurs 
     * variables. Donc un vecteur où X_j sont les composantes. X_i la ith observation ( lignes matrice ).
     * @param y_train Vecteur cible, valeurs à prédire ( target ). Pour le dataset des iris, on cherche à prédire 
     * l'appartenance d'une plante à une espèce à partir de ses caractéristiques. On utilise pour entrainer 
     * l'algorithme un dataset avec des étiquettes ( label/target y ).
     * @param separation : sépare un dataset selon un certain pourcentage
     */
    private DenseMatrix X_train;
    private DenseMatrix y_train;
    private DenseMatrix x_test;

    public DataSet(ArrayList<ArrayList<String>> dataArray, int[] separation, int indexCible){
        DenseMatrix data = this.prepareData(dataArray, indexCible);
        this.X_train = new DenseMatrix(data.slice(separation[0], separation[1], 0, data.cols-1));
        this.y_train = new DenseMatrix(data.slice(separation[0], separation[1], data.cols-1, data.cols));
        this.x_test = new DenseMatrix(data.slice(separation[1], separation[2], 0, data.cols-1));
        //System.out.println(this.x_test.toString());
    }

    public DenseMatrix prepareData(ArrayList<ArrayList<String>> donneesExtraites, int indexCible){
        DenseMatrix enMatrice = new DenseMatrix(donneesExtraites.size(), donneesExtraites.get(0).size());
        // Stockage des classes temporairement afin de les convertir en entier
        HashMap<String, Integer> classeCouranteDetectee = new HashMap<>();
        for(int i=0; i<donneesExtraites.size(); i++){
            //System.out.println("di: "+donneesExtraites.get(i));
            for(int j=0; j<donneesExtraites.get(i).size(); j++){
                //System.out.println("valeur: "+donneesExtraites.get(i).get(j));
                if(j == indexCible){
                    if(classeCouranteDetectee.containsKey(donneesExtraites.get(i).get(j)) || classeCouranteDetectee.isEmpty()){
                        String classe = donneesExtraites.get(i).get(j);
                        classeCouranteDetectee.replace(classe, classeCouranteDetectee.get(classe)+1);
                        //classes.put(donneesExtraites.get(i).get(j), compteur);
                    }
                    double classeEnInt = classeCouranteDetectee.get(donneesExtraites.get(i).get(j));
                    enMatrice.set(i, j, classeEnInt);
                } else {
                    enMatrice.set(i, j, Double.parseDouble(donneesExtraites.get(i).get(j)));
                }
            }
            //System.out.println("de: "+classes.entrySet());
        }
        //System.out.println(enMatrice.toString());
        return enMatrice;
    }

    /**
     * Retourne le nombre d'instances dans le dataset pour la matrice X_train
     */
    public int getM(){
        return this.X_train.rows;
    }

    /** 
     * Retourne le nombre de features pour une instance de la matrice X_train
     */
    public int getN(){
        return this.X_train.cols;
    }

    public DenseMatrix getX_Train(){
        return this.X_train;
    }

    public DenseMatrix getX_Test(){
        return this.x_test;
    }

    public DenseMatrix getY_Train(){
        return this.y_train;
    }
}
