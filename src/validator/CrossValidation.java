package validator;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

import elements.Element;
import jeigen.DenseMatrix;
/**
 * Classe implémentant la validation croisée:
 * 
 * @param dataset jeu de données à découper
 * @param n : nombre de blocs pour le découpage
 */
public class CrossValidation {

    private HashSet<Element> dataset;
    private int nbDecoupes;

    private HashSet<Element> trainSet;
    private HashSet<Element> validationSet;
    private HashSet<Element> testSet;

    public CrossValidation(HashSet<Element> dataset, int n, float[] split){
        this.dataset = dataset;
        this.nbDecoupes = n;
        // On initialise un itérateur pour notre HashSet
        Iterator<Element> iterator = dataset.iterator();

        int i = 0;
        Element randomElement = null;
        // On commence par prendre aléatoirement des éléments pour notre X_test/y_test regroupés dans le testSet
        while (iterator.hasNext()) {
            int randomNumber = new Random().nextInt(dataset.size());
            randomElement = iterator.next();
            if (i == randomNumber)
                this.testSet.add(randomElement);
                // On omet pas de supprimer l'élément du dataset pour qu'il ne soit pas de nouveau pioché
                this.dataset.remove(randomElement);
            i++;
        }
        // for(int i=0; i<dataset.size()*split[2]; i++){
        //     int randomNumber = rd.nextInt((dataset.size()-0)+1) + 0;
        //     dataset.
        // }
    }

    public 

    /**
     * @param testSet l'ensemble des éléments de notre test
     * @return le taux d'éléments bien classés ( en pourcentage )
     */
    public double score(){
        int goodClass = 0;
        
        return goodClass;
    } // PAS BON CAR HASHSET NON ORDONNE
}
