package validator;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;
import elements.Element;
import jeigen.DenseMatrix;
import classifier.*;
/**
 * Classe implémentant la validation croisée:
 * 
 * @param dataset jeu de données à découper
 * @param n : nombre de blocs pour le découpage
 */
public class CrossValidation {

    private ArrayList<Element> dataset;
    private ArrayList<ArrayList<Element>> test_set;
    private ArrayList<ArrayList<Element>> training_set;
    private ArrayList<Element> validation_set;
    private ArrayList<Double> scores;
    

    public CrossValidation(HashSet<Element> dataset){
        this.dataset = new ArrayList<>(dataset);
        Collections.shuffle(this.dataset);
        if (this.dataset.size()%2==1){
            this.dataset.remove(this.dataset.get(this.dataset.size()-1));
        }
        test_set = new ArrayList<>();
        training_set = new ArrayList<>();
        validation_set = new ArrayList<>();
        scores = new ArrayList<>();
    }

    public ArrayList<Double> getScore(){
        return this.scores;
    }

    public void crossValidation(int nbDecoupes, DANN classifier){
        int bloc_size = Math.round(dataset.size()/nbDecoupes);

        for (int i=0; i<nbDecoupes;i++){  
            ArrayList<Element> part1;
            ArrayList<Element> part2;
            double scorei=0;


            test_set.add(new ArrayList<>(dataset.subList(i*bloc_size,(i*bloc_size)+bloc_size)));

            if(i==0){
                training_set.add(new ArrayList<>(dataset.subList(bloc_size,dataset.size())));
            }
            if(i!=0 && i!=nbDecoupes-1){
                part1= new ArrayList<>(dataset.subList(0,i*bloc_size));
                part2 = new ArrayList<>(dataset.subList(i*bloc_size+bloc_size,dataset.size()));
                part1.addAll(part2);
                training_set.add(part1);
            }
            if(i==nbDecoupes-1){
                training_set.add(new ArrayList<>(dataset.subList(0,dataset.size()-bloc_size)));
            }
            


            ///DANN 

            for(Element query: this.test_set.get(i)){
                classifier.setDataset(new HashSet(this.training_set.get(i)));
                query.setPredict(classifier.proceed(query));
                scorei+=(query.getClasse() == query.getPredict() ? 1.0 : 0);
            }
            this.scores.add(scorei/bloc_size);
            



        }

        
        
    }

    /**
     * @param testSet l'ensemble des éléments de notre test
     * @return le taux d'éléments bien classés ( en pourcentage )
     */
    public double score(){
        double score=0;
        
        return score;
    } // PAS BON CAR HASHSET NON ORDONNE
}
