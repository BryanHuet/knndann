import static jeigen.Shortcuts.*;

import java.util.ArrayList;

import data.DataSet;
import jeigen.*;

public class DANN {
    /**
     * @param dataset est l'ensemble des données qui serviront pour la classification des points.
     * @param k permet de sélectionner k voisins autour d'un point à classifier.
     * @param km est le nombre de voisins dans le voisinage permettant d'adapter la métrique.
     * @param epsilon Le paramètre arrondit le voisinage, à partir d'une 
     * bande infinie à un ellipsoïde, pour éviter d'utiliser des points éloignés de la requête point.
     */
    private DataSet dataSet;
    private final int k;
    // km = max(n/5,50) -> peut être choisi mais raisonnablement selon le papier. 
    // n est ici le nombre d'exemples dans X_train du DataSet. n = numElements(X_train)
    private final int km;
    private final double epsilon;

    /**
     * Le constructeur est semblable à la fonction fit() de sklearn à la différence qu'il ne s'agit ici que 
     * d'initialisation, knn est un algo qui ne possède pas de phase d'apprentissage.
     */
    public DANN(DataSet dataset, int k, int km, int epsilon){
        this.dataSet = dataset;
        this.k = k;
        this.km = Math.max(km, 50); // 50 : Selon le livre 
        this.epsilon = epsilon; // 1 : Valeur fonctionnant bien dans la plupart des cas selon le papier
    }

    public DANN(DataSet dataset, int k){
        this(dataset,k,50,1);
    }

    /**
     * @return l'efficacité du modèle ( la valeur de précision est un double compris entre 0 et 1 ).
     */
    public double score(){
        return 0;
    }

    /**
     * Méthode permettant de classer un point n'ayant pas d'étiquette
     * @param x0 point à classifier, c'est un vecteur. x0[nombre_de_features].
     */
    public void predict(DenseMatrix x0){
        // Fonction à exécuters autant de fois qu'il y a de points à classifier
        // On récupère d'abord le nombre de features de notre point
        final int nombreDeFeatures = x0.cols;
        ArrayList<Double> distances = new ArrayList<>();
        for(int i=0; i<this.getDataSet().getM(); i++){
            for(int j=0; i<this.getDataSet().getN(); j++){
                 //valeur = this.dataSet.getX_Train().get(i,j);
            }
        }
        // ...
        ArrayList<DenseMatrix> plusProchesVoisins = new ArrayList<>();
        
        // On effectue un tri et on sélectionne les km voisins
        /*for()
        plusProchesVoisins.add(distance[...]);*/
        DenseMatrix voisinage;
        // ...
        // Moyenne de chaque colonne dans x
        DenseMatrix xMean = voisinage.meanOverCols();
        // Moyenne dans x pour la classe j
        DenseMatrix xjMean = ;
        DenseMatrix J = this.dataSet.getY_Train();

        // On initialise les matrices sigma, B, W nécessaires pour calculer la métrique.
        // Soit B la matrice de covariance des "between class" et W la matrice de covariance des "within class" 
        // Elles ont une taille de (nombre_de_features x nombre_de_features) et sont remplis de 0.
        DenseMatrix sigma = DenseMatrix.eye(nombreDeFeatures);
        DenseMatrix B = DenseMatrix.zeros(nombreDeFeatures,nombreDeFeatures);
        DenseMatrix W = DenseMatrix.zeros(nombreDeFeatures,nombreDeFeatures);
        double sommeDesPoids = 0.0;
        for(int j=0; j<J.rows; j++){

        }
        W = W.mexp();
        B = W.mmul(B);
        sigma = W.mmul(B.add(epsilon).mmul(DenseMatrix.eye(this.dataSet.getN()))).mmul(W);
    }

    /**
     * @param x0 le point de référence
     * @param x1 le point à mesurer
     * @param sigma la métrique
     * @return la distance entre x0 et x1
     */
    public DenseMatrix distance(DenseMatrix x0, DenseMatrix x1, DenseMatrix sigma){
        DenseMatrix soustraction = x0.sub(x1);
        DenseMatrix distance = soustraction.mmul(soustraction.t().mmul(sigma));
        return distance;
    }

    public DataSet getDataSet(){
        return this.dataSet;
    }
}
