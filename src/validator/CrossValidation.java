package validator;

import java.util.*;

import classifier.DANN;
import elements.Element;
import jeigen.DenseMatrix;

import static java.lang.Math.round;

/**
 * Classe implémentant la méthode k fold cross validation pour valider le meilleur modèle d'une série
 */
public class CrossValidation {

    public final int NB_ITERATION = 1;
    public final boolean SHUFFLE_DATA = true;

    private ArrayList<Element> dataset;
    private int datasize;
    private ArrayList<Element> test_set;
    private Fold[] folds;
    private HashMap<Integer, Double> scores;
    private int k;
    private int parametersNumber;
    private int classNumber;
    private ArrayList<Element> best_training_set;
    private Set<Integer> parameters;

    /**
     * Constructeur
     * @param dataset le jeu de données
     */
    public CrossValidation(int k, HashSet<Element> dataset, Set<Integer> parameters,int dataDimension, int classNumber){
        this.parametersNumber = dataDimension;
        this.classNumber = classNumber;
        this.k = k;
        this.dataset = new ArrayList<>(dataset);
        this.datasize = dataset.size();
        if(SHUFFLE_DATA){
            Collections.shuffle(this.dataset);
        }
        this.test_set = new ArrayList<>();
        this.folds = folds(k);
        this.scores = new HashMap<>();
        this.best_training_set = new ArrayList<>();
        this.parameters=parameters;
    }

    /**
     * Méthode découpant le dataset en k blocs
     * @param k le nombre de blocs de notre jeu de données
     * @return le dataset découpé en k blocs
     */
    public Fold[] folds(int k){
        if(k <= 0){
            throw new IllegalArgumentException("Le nombre de blocs ne peut pas être <= à 0");
        }
        int bloc_size = round(dataset.size()/(k+1));
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

    /**
     * Méthode lançant la validation croisée
     * Prépare au début un jeu de test qui sera totalement isolé et préservé pour la validation finale
     */
    public void crossValidation(){
        //this.displayFolds();
        double scores = 0;
        double bestScore = -1;

        int bestP = -1;
        ArrayList<Element> bestTrainingSet = new ArrayList<>();
        // On itère sur les hyperparamètres que l'on souhaite tester
        for(int p : parameters){
            for(int i=0; i<this.folds.length; i++){
                DANN dann = new DANN(new HashSet(this.folds[i].getTrain_set()), p, NB_ITERATION, parametersNumber, classNumber);
                for(Element query : this.folds[i].getValidation_set()){
                    query.setPredict(dann.proceed(query));
                }
                double score = this.score(this.folds[i].getValidation_set());
                this.folds[i].setScore(score);
                scores+=this.folds[i].getScore();
                if(score > bestScore){
                    bestScore = score;
                    this.best_training_set = this.folds[i].getTrain_set();
                }
            }
            this.scores.put(p,(scores/this.k));
            scores=0;

        }

    }

    /**
     * Méthode permettant de valider un modèle avec DANN et le test_set
     * @param dataset le meilleur jeu d'entrainement trouvé par la cross validation
     * @param p le meilleur paramètre sélectionné dans la méthode crossValidation
     * @param parametersNumber le nombre de features dans un vecteur x
     * @param classNumber le nombre de classes possibles
     */
    private void validate(ArrayList<Element> dataset, int p, int parametersNumber, int classNumber){
        DANN dann = new DANN(new HashSet<>(dataset), p, NB_ITERATION, parametersNumber, classNumber);
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

    /**
     * Methode d'affichage pour donner un rendu terminal.
     * @param chemin
     */
    public void displayResults(String chemin){
        int bestP=0;
        double bestScore=-1;

        System.out.println("-------- DISPLAY RESULTS CROSS VALIDATION -------\n");
        System.out.println("# Parameters used");
        if(!Objects.equals(chemin, "")){
            System.out.println("datafile : "+chemin);
        }
        System.out.println("data size : "+this.datasize+" | attributes number : "+this.parametersNumber+" | class number : "+this.classNumber);
        System.out.println("k fold : "+this.k+" | k neighbourhood tested : "+this.parameters+" | test set size : "+this.test_set.size());

        System.out.println("\n# Results");
        for (Map.Entry<Integer,Double> mapentry : this.scores.entrySet()) {
            System.out.println("k= "+mapentry.getKey()+" Mean Score= "+mapentry.getValue() );
            if (mapentry.getValue() > bestScore){
                bestScore = mapentry.getValue();
                bestP = mapentry.getKey();
            }
        }

        System.out.println("best k neighbourhood is "+bestP+" for an accuracy of "+round(bestScore*100.0)+"% good classification.");

        System.out.println("\n #validation using the best k with the test set and the best training set");
        validate(this.best_training_set, bestP, parametersNumber, classNumber);
        System.out.println("Score : "+this.score(this.test_set));
        System.out.println("Confusion matrix formed by a "+this.confusionMatrix());

    }

    /**
     * surcharche de displayResults
     */
    public void displayResults(){
        this.displayResults("");
    }

    /**
     * Calcul de la matrice confusion sur le test_set
     * @return La matrice de confusion en format DenseMatrix.
     */
     public DenseMatrix confusionMatrix(){
        HashMap<Integer,HashMap<Integer,Integer>> predictedMatrix = new HashMap<>();
        StringBuilder mat = new StringBuilder();

         for(int i=0; i<this.classNumber;i++){
             HashMap<Integer,Integer> internPredict = new HashMap<>();
             for(int j=0; j<this.classNumber;j++){
                 internPredict.put(j,0);
             }
             for (Element e: this.test_set){
                 if (e.getClasse()==i) {
                     if (e.getClasse() == e.getPredict()) {
                         if (internPredict.containsKey(i)) {
                             internPredict.put(i, internPredict.get(i) + 1);
                         } else {
                             internPredict.put(i, 1);
                         }
                     } else {
                         if (internPredict.containsKey(e.getPredict())) {
                             internPredict.put(e.getPredict(), internPredict.get(e.getPredict()) + 1);
                         } else {
                             internPredict.put(e.getPredict(), 1);
                         }
                     }
                 }
             }
             predictedMatrix.put(i,internPredict);
         }
         for (Map.Entry<Integer,HashMap<Integer,Integer>> mapentry : predictedMatrix.entrySet()) {
             //System.out.print("[ ");
             for (Map.Entry<Integer,Integer> entry : mapentry.getValue().entrySet()) {
                 //System.out.print(entry.getValue()+" ");
                 mat.append(entry.getValue()).append(" ");
             }
             mat.append(";");
             //System.out.println(" ]");
         }



        return new DenseMatrix(mat.toString());
    }

}