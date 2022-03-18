package validator;

import java.util.*;

import classifier.DANN;
import elements.Element;

public class BetaCV2 {

    private ArrayList<Element> dataset;
    private ArrayList<Element> test_set;
    private ArrayList<Double> scores;
    private HashMap<Integer, Double> scorePerParameter = new HashMap<>();

    private ArrayList<ArrayList<Element>> validation_set;
    private ArrayList<ArrayList<Element>> training_set;

    public BetaCV2(HashSet<Element> dataset){
        this.dataset = new ArrayList<>(dataset);
        Collections.shuffle(this.dataset);
        this.scores = new ArrayList<>();
        this.test_set = new ArrayList<>();
        this.training_set = new ArrayList<>();
        this.validation_set = new ArrayList<>();
    }

    public void crossValidation(int k, int parametersNumber, int classNumber, Set<Integer> parameters){
        ArrayList<ArrayList<Element>> folds = new ArrayList<>();
        if(k <= 0){
            throw new IllegalArgumentException("Le nombre de blocs ne peut pas être <= à 0");
        }
        // partitions : S1 ... Sk
        // for(int i=0; i<this.dataset.size(); i+=k){
        //     folds.add(new ArrayList<>(this.dataset.subList(i,Math.min(this.dataset.size(), i + k))));
        // }
        //System.out.println(folds);
        // // On itère sur les hyperparamètres que l'on souhaite tester
        // for(int p : parameters){
        //     for(int i=0; i<k; i++){
        //         validationSet = folds.get(i);
        //         trainSet = new ArrayList<>();
        //         for(int j=0; j<k; j++){
        //             if(!folds.get(j).containsAll(validationSet)){
        //                 trainSet.addAll(folds.get(j));
        //             }
        //         }
        //         DANN dann = new DANN(new HashSet(trainSet), p, 3, parametersNumber, classNumber);
        //         for(Element query : validationSet){
        //             query.setPredict(dann.proceed(query));
        //             //score +=(query.getClasse() == query.getPredict() ? 1.0 : 0);
        //         }
        //     }
        //     double error = score(validationSet);
        //     this.scores.add(error);
        // }
        int bloc_size = Math.round(dataset.size()/(k+1));
        test_set = new ArrayList<>(dataset.subList(0,bloc_size));
        dataset.removeAll(test_set);
        // On itère sur les hyperparamètres que l'on souhaite tester
        for (int i=0; i<k;i++){

            double scorei=0;
            ArrayList<Element> datasetcopy = (ArrayList) this.dataset.clone();

            validation_set.add(new ArrayList<>(dataset.subList(i*bloc_size,(i*bloc_size)+bloc_size)));

            datasetcopy.removeAll(validation_set.get(i));

            training_set.add(new ArrayList<>(datasetcopy));
        }
        for(int p : parameters){
            for(int i=0; i<k; i++){
                DANN dann = new DANN(new HashSet(training_set.get(i)), p, 1, parametersNumber, classNumber);
                for(Element query : validation_set.get(i)){
                    query.setPredict(dann.proceed(query));
                }
            }
            this.scorePerParameter.put(p, this.score(this.dataset));
        }
        for(Map.Entry<Integer, Double> pair: this.scorePerParameter.entrySet()){
            System.out.println("p="+pair.getKey()+" ; score="+pair.getValue());
        }
    }

    public double score(ArrayList<Element> dataset){
        double goodClass = 0;
        for(Element element : dataset){
            goodClass += element.getClasse() == element.getPredict() ? 1.0 : 0;
        }
        return goodClass/dataset.size();
    }
}
