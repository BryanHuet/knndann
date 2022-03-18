package validator;

import java.util.*;

import classifier.DANN;
import elements.Element;

public class BetaCV2 {

    private ArrayList<Element> dataset;
    private ArrayList<Element> test_set;
    private ArrayList<Double> scores;

    public BetaCV2(HashSet<Element> dataset){
        this.dataset = new ArrayList<>(dataset);
        Collections.shuffle(this.dataset);
        this.scores = new ArrayList<>();
    }

    public void crossValidation(int k, int parametersNumber, int classNumber, Set<Integer> parameters){
        ArrayList<ArrayList<Element>> folds = new ArrayList<>();
        if(k <= 0){
            throw new IllegalArgumentException("Le nombre de blocs ne peut pas être <= à 0");
        }
        // partitions : S1 ... Sk
        for(int i=0; i<this.dataset.size(); i++){
            folds.add(new ArrayList<>(this.dataset.subList(i,Math.min(this.dataset.size(), i + k))));
        }
        // On itère sur les hyperparamètres que l'on souhaite tester
        for(int p : parameters){
            for(int i=0; i<k; i++){
                ArrayList<Element> testSet = folds.get(i);
                ArrayList<Element> trainSet = folds.removeAll(testSet);
                DANN dann = new DANN(new HashSet(.get(j)), p, 3, parametersNumber, classNumber);
            }
            this.scores.add(this.score(data));
        }

    }

    public double score(ArrayList<Element> data){
        int goodClass = 0;
        for(int i=0; i<data.size(); i++){
            if(data.get(i).getClasse() != data.get(i).getPredict()){
                goodClass++;
            }
        }
        return goodClass/data.size();
    }
}
