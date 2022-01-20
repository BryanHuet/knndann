package data;
import java.util.ArrayList;
import java.util.List;

//import jeigen.DenseMatrix;

public class Point {
    /**
     * Un point est ... par ses caractéristiques et sa classe
     * @param features Le vecteur des valeurs des caractéristiques de notre point ( x, sans la cible ).
     * @param classe La classe d'appartenance de notre point ( y, la cible ).
     */
    private List<Number> features;
    private int classe;
    //private List<Point> voisins;

    public Point(){
        this.features = new ArrayList<>();
    }

    public boolean compare(){
        return false;
    }

    public void displayElementsFeatures(){
        for (Number feature : features) {
            System.out.println(feature);
        }
    }

    public int getFeaturesNumber(){
        return this.features.size();
    }
}
