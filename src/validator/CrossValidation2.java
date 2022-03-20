package validator;

import java.util.*;

import classifier.DANN;
import elements.Element;

/**
 * Classe implémentant la méthode k fold cross validation pour valider le meilleur modèle d'une série
 */
public class CrossValidation2 {

    private ArrayList<Element> dataset;
    private ArrayList<Element> test_set;
    private Fold[] folds;

    /**
     * Constructeur
     * @param dataset le jeu de données
     */
    public CrossValidation2(int k, HashSet<Element> dataset){
        this.dataset = new ArrayList<>(dataset);
        Collections.shuffle(this.dataset);
        this.test_set = new ArrayList<>();
        this.folds = folds(k);
    }

    /**
     * Méthode lançant la validation croisée
     * @param k le nombre de blocs de notre jeu de données
     * @param parametersNumber le nombre de features dans un vecteur x
     * @param classNumber le nombre de classes possibles
     * @param parameters l'ensemble des nombres de voisins
     * Prépare au début un jeu de test qui sera totalement isolé et préservé pour la validation finale
     */
    public Fold[] folds(int k){
        if(k <= 0){
            throw new IllegalArgumentException("Le nombre de blocs ne peut pas être <= à 0");
        }
        int bloc_size = Math.round(dataset.size()/(k+1));
        // On isole le jeu de test : par exemple on prend les "bloc_size ième" éléments
        test_set = new ArrayList<>(dataset.subList(0,bloc_size));
        // On omet pas de les supprimer du dataset
        dataset.removeAll(test_set);

        // On effectue le découpage des données | partitions : S1 ... Sk
        Fold[] folds = new Fold[k];
        for (int i=0; i<k;i++){
            int start = bloc_size * i;
            int end = bloc_size * (i + 1);
            if (i == k-1) end = this.dataset.size();
            ArrayList<Element> validation = new ArrayList<>();
            ArrayList<Element> train = new ArrayList<>();
            for (int j = 0; j < this.dataset.size(); j++) {
                if (j >= start && j < end) {
                    validation.add(this.dataset.get(j));
                } else {
                    train.add(this.dataset.get(j));
                }
            }
            folds[i] = new Fold(train, validation);
        }
        return folds;
    }

    public void crossValidation(int parametersNumber, int classNumber, Set<Integer> parameters){
        this.displayFolds();
        double bestScore = 0;
        int bestP = -1;
        ArrayList<Element> bestTrainingSet = new ArrayList<>();
        // On itère sur les hyperparamètres que l'on souhaite tester
        for(int p : parameters){
            System.out.println("p="+p);
            for(int i=0; i<this.folds.length; i++){
                DANN dann = new DANN(new HashSet(this.folds[i].getTrain_set()), p, 1, parametersNumber, classNumber);
                for(Element query : this.folds[i].getValidation_set()){
                    query.setPredict(dann.proceed(query));
                }
                double score = this.score(this.folds[i].getValidation_set());
                this.folds[i].setScore(score);
                System.out.println("Score "+i+" : "+this.folds[i].getScore());
                if(score > bestScore){
                    bestScore = score;
                    bestP = p;
                    bestTrainingSet = this.folds[i].getTrain_set();
                }
            }
        }
        System.out.println("bestP : "+bestP);
        System.out.println("bestScore : "+bestScore);
        System.out.println("VALIDATION DU MEILLEUR MODELE :");
        validate(bestTrainingSet, bestP, parametersNumber, classNumber);
        System.out.println("Validation finale avec "+bestP+":"+this.score(this.test_set));
    }

    /**
     * Méthode permettant de valider un modèle avec DANN et le test_set
     * @param dataset le meilleur jeu d'entrainement trouvé par la cross validation
     * @param p le meilleur paramètre sélectionné dans la méthode crossValidation
     * @param parametersNumber le nombre de features dans un vecteur x
     * @param classNumber le nombre de classes possibles
     */
    private void validate(ArrayList<Element> dataset, int p, int parametersNumber, int classNumber){
        DANN dann = new DANN(new HashSet(dataset), p, 1, parametersNumber, classNumber);
        for(Element query : this.test_set){
            query.setPredict(dann.proceed(query));
        }
    }

    /**
     * Méthode permettant de calculer les éléments bien classés
     * @param dataset le jeu de données
     * @return le pourcentage de réussite dans le jeu de données pris en paramètre
     */
    public double score(ArrayList<Element> dataset){
        double goodClass = 0;
        for(Element element : dataset){
            goodClass += element.getClasse() == element.getPredict() ? 1.0 : 0;
        }
        return goodClass/dataset.size();
    }

    /**
     * Méthode permettant de calculer les éléments mal classés
     * @param dataset le jeu de données
     * @return le pourcentage d'erreurs dans le jeu de données pris en paramètre
     */
    public double errors(ArrayList<Element> dataset){
        double badClass = 0;
        for(Element element : dataset){
            badClass += element.getClasse() != element.getPredict() ? 1.0 : 0;
        }
        return badClass/dataset.size();
    }

    /**
     * Fonction de debug pour vérifier que le découpage du dataset s'effectue bien.
     * A utiliser sur un très faible nombre de données !
     * Exemple : 10 observations pour le dataset.
     */
    public void displayFolds(){
        //System.out.println("DATASET ------------\n");
        //System.out.println(this.dataset);
        System.out.println("TRAIN/VAL --------------\n");
        for(int z=0; z<this.folds.length; z++){
            System.out.println("SET "+z+" :\n");
            System.out.println("train="+this.folds[z].getTrain_set().size()+" ; validation="+this.folds[z].getValidation_set().size());
            //System.out.println("train="+this.folds[z].getTrain_set()+" ; validation="+this.folds[z].getValidation_set());
        }
    }

    // public DenseMatrix confusionMatrix(){
    //     return ;
    // }

    // public void auc(){
        
    // }
}