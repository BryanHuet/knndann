package validator;

import java.util.*;
import elements.Element;
import jeigen.DenseMatrix;
import classifier.*;

public class NestedCrossValidation {

    private ArrayList<Element> dataset;
    private ArrayList<Element> test_set;
    private ArrayList<Double> scores;

    public NestedCrossValidation(HashSet<Element> dataset){
        this.dataset = new ArrayList<>(dataset);
        Collections.shuffle(this.dataset);
        this.scores = new ArrayList<>();
    }

    public void crossValidation(int blocs_number, int parametersNumber, int classNumber){
        int bloc_size = Math.round(dataset.size()/blocs_number);
        ArrayList<ArrayList<Element>> folds = new ArrayList<>();
        if(blocs_number <= 0){
            throw new IllegalArgumentException("Le nombre de blocs ne peut pas être <= à 0");
        }
        for(int i=0; i<this.dataset.size(); i++){
            folds.add(new ArrayList<>(this.dataset.subList(i,Math.min(this.dataset.size(), i + blocs_number))));
        }
        for(int i=0; i<blocs_number; i++){
            ArrayList<Element> test = folds.get(i);
            ArrayList<ArrayList<Element>> train = folds;
            train.remove(folds.get(i));
            ArrayList<Integer> parameters = new ArrayList<>(Arrays.asList(2,3,5));
            for(int p=0; p<parameters.size(); p++){
                for(int j=0; j<train.size(); j++){
                    ArrayList<Element> validation = train.get(i);
                    ArrayList<ArrayList<Element>> subTrain = train;
                    subTrain.remove(train.get(i));
                    DANN dann = new DANN(new HashSet(subTrain.get(j)), p, 3, parametersNumber, classNumber);
                    for(Element query : validation){
                        query.setPredict(dann.proceed(query));
                    }
                }

            }
            //DANN dann = new DANN(dataset, k, nb_iteration, nb_parameters, nb_classes)
            // 
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
