package classifier;

import elements.Element;
import jeigen.DenseMatrix;

import java.util.*;

import static jeigen.DenseMatrix.zeros;

/**
 * Class implémentant l'algorithme DANN
 */
public class DANN {

    public static double EPSILON = 1.0;

    private int nb_classes;
    private int nb_parameters;
    private HashSet<Element> dataset;
    private int k;
    private int nb_iteration;

    //(Element query, int k, int nb_iteration, int parameters,int class_number,HashSet<Element> data)

    /**
     * @param dataset       Un ensemble de point definie en objet Ensemble
     * @param k             le nombre de voisin maximum dans le voisinage
     * @param nb_iteration  nombre d'iteration de l'algorithme dann
     * @param nb_parameters le nombre de features
     * @param nb_classes    le nombre de classe
     */
    public DANN(HashSet<Element> dataset, int k, int nb_iteration, int nb_parameters, int nb_classes) {
        this.nb_classes = nb_classes;
        this.nb_parameters = nb_parameters;
        this.dataset = dataset;
        this.k = k;
        this.nb_iteration = nb_iteration;

    }

    /**
     * Deuxième constructeur qui ne prend en paramètre uniquement un dataset, le nombre de features et le nombre de classes
     *
     * @param dataset       Un ensemble de point definie en objet Ensemble
     * @param nb_parameters le nombre de features
     * @param nb_classes    le nombre de classe
     */
    public DANN(HashSet<Element> dataset, int nb_parameters, int nb_classes) {
        this(dataset, 50, 1, nb_parameters, nb_classes);
    }

    public void setDataset(HashSet<Element> dataset) {
        this.dataset = dataset;
    }

    /**
     * Calcul la distance euclidienne entre le vecteur xi et x0.
     * Correspond à la formule di du point (6)
     *
     * @param xi
     * @param x0
     * @return double
     */
    public static double distanceEuclidienne(Element xi, Element x0) {
        double result = 0;
        DenseMatrix calcul = xi.getVector().sub(x0.getVector());
        calcul = calcul.mul(calcul);
        calcul = calcul.sumOverCols();
        result = calcul.getValues()[0];
        return Math.sqrt(result);
    }

    /**
     * Correspond à la formule h du point (6)
     *
     * @param x0       la requete
     * @param elements le voisinage de x0
     * @return double
     */
    public static double max(Element x0, ArrayList<Element> elements) {
        double h = -100000000;
        ArrayList<Double> di = new ArrayList<>();

        for (Element e : elements) {
            di.add(distanceEuclidienne(e, x0));
        }

        for (double v : di) {
            if (h < v) {
                h = v;
            }
        }
        return h;
    }

    /**
     * Correspond à la formule k du point (7)
     *
     * @param xi un element
     * @param x0 la requete
     * @param h
     * @return Matrice
     */
    public static double weights(Element xi, Element x0, double h) {
        return Math.pow(1 - (Math.pow(distanceEuclidienne(xi, x0) / h, 3)), 3);
    }

    /**
     * Correspond à la formule pj du point (8)
     *
     * @param x0          la query
     * @param elementNear le voisinage
     * @param j           classe j
     * @return double
     */
    private double pj(Element x0, ArrayList<Element> elementNear, int j) {
        double numerator = 0;
        double denominator = 1;
        for (Element e1 : elementNear) {
            if (e1.getClasse() == j) {
                numerator += weights(e1, x0, max(x0, elementNear));
            }
            denominator += weights(e1, x0, max(x0, elementNear));
        }
        return numerator / denominator;
    }

    /**
     * Calcul la moyenne des points correspondant a la classe j
     *
     * @param elementNear un ensemble d'element
     * @param j           une classe j
     * @return une matrice des moyennes de la classe j
     */
    private DenseMatrix moyenneXj(ArrayList<Element> elementNear, int j) {
        DenseMatrix moy = zeros(1, this.nb_parameters);
        for (Element e : elementNear) {
            if (e.getClasse() == j) {
                moy = moy.add(e.getVector());
            }
        }
        return moy.div(elementNear.size());
    }

    /**
     * Calcul la moyenne des points
     *
     * @param elementNear un ensemble d'element
     * @return une matrice des moyennes
     */
    private DenseMatrix moyenneX(ArrayList<Element> elementNear) {
        DenseMatrix moy = zeros(1, this.nb_parameters);
        for (Element e : elementNear) {
            moy = moy.add(e.getVector());
        }
        return moy.div(elementNear.size());
    }

    /**
     * Calcul de la matrice B, des elements "between-classes" de la requete x0
     *
     * @param elementNear ensemble d'elements, le voisinage de x0
     * @param x0          la requete
     * @return une matrice
     */
    private DenseMatrix B(ArrayList<Element> elementNear, Element x0) {
        DenseMatrix matrice = zeros(this.nb_parameters, this.nb_parameters);
        for (int j = 0; j < this.nb_classes; j++) {
            double pj = pj(x0, elementNear, j);
            DenseMatrix moy = moyenneX(elementNear).sub(moyenneXj(elementNear, j));
            moy = moy.t().mmul(moy);
            matrice = matrice.add(moy.mul(pj));
        }
        return matrice;
    }

    /**
     * Calcul de la matrice W, des elements "within-classes" de la requete x0
     *
     * @param elementNear ensemble d'elements, le voisinage de x0
     * @param x0          la requete
     * @return une matrice
     */
    private DenseMatrix W(ArrayList<Element> elementNear, Element x0) {
        DenseMatrix matrice = zeros(this.nb_parameters, this.nb_parameters);
        double total_weigth = 0;
        for (int j = 0; j < this.nb_classes; j++) {
            for (Element e : elementNear) {
                if (e.getClasse() == j) {
                    DenseMatrix current = e.getVector().sub(moyenneXj(elementNear, j));
                    current = current.t().mmul(current);
                    matrice = matrice.add(current.mul(weights(e, x0, max(x0, elementNear))));
                }
            }
        }
        for (Element e : elementNear) {
            total_weigth += weights(e, x0, max(x0, elementNear));
        }
        return matrice.div(total_weigth);
    }

    /**
     * Remplace toute les valeurs d'une matrice par 0 en gardant la diagonale intact.
     *
     * @param matrix une matrice
     * @return une matrice de 0 sauf sur la diagonale
     */
    private DenseMatrix keepDiag(DenseMatrix matrix) {
        matrix = DenseMatrix.eye((int) (matrix.shape().getValues()[0])).mul(matrix);
        matrix = matrix.sqrt();
        matrix = matrix.recpr();
        for (int i = 0; i < matrix.shape().getValues()[0]; i++) {
            for (int j = 0; j < matrix.shape().getValues()[0]; j++) {
                if (matrix.get(i, j) > 1000000) {
                    matrix.set(i, j, 0);
                }
                if (i != j) {
                    matrix.set(i, j, 0);
                }
            }
        }
        return matrix;
    }

    /**
     * Le point (1) du papier
     *
     * @param x0    le point à classer ( la requête )
     * @param x     le point de comparaison ( un élément du dataset )
     * @param sigma la métrique
     * @return la valeur de la distance
     */
    private double DANN_distance(Element x0, Element x, DenseMatrix sigma) {
        DenseMatrix difference = x0.getVector().sub(x.getVector());
        return (difference.mmul(sigma)).mmul(difference.t()).getValues()[0];
    }

    /**
     * Calcul du voisinage de la requete x0 en utilisant un discriminant sigma
     *
     * @param x0    la requete
     * @param sigma une matrice
     * @return un ensemble d'element
     */
    private ArrayList<Element> nearest_neighbor(Element x0, DenseMatrix sigma) {
        ArrayList<Element> near = new ArrayList<>();
        TreeMap<Double, Element> map = new TreeMap<>();
        for (Element e : this.dataset) {
            map.put(this.DANN_distance(x0, e, sigma), e);
        }

        for (int i = 0; i < this.k; i++) {
            if (map.firstEntry() != null) {
                near.add(map.pollFirstEntry().getValue());
            }
        }
        return near;
    }

    /**
     * Returne la classe majoritaire dans la liste d'element.
     *
     * @param list une liste d'elements
     * @return un entier qui correspond a la classe la plus frequente
     */
    private int majorClasse(List<Element> list) {
        String soluce = "";
        HashMap<String, Integer> dico = new HashMap<>();

        for (Element element : list) {
            String classe = "" + element.getClasse();
            if (dico.containsKey(classe)) {
                dico.put(classe, dico.get(classe) + 1);
            } else {
                dico.put(classe, 1);
            }
        }
        for (Map.Entry<String, Integer> entry : dico.entrySet()) {
            if (entry.getValue().equals(Collections.max(dico.values()))) {
                soluce = entry.getKey();
            }
        }

        return Integer.parseInt(soluce);
    }


    /**
     * Méthode permettant de classifier un élément
     * @param query la requete
     * @return la classe predite
     */
    public int proceed(Element query) {

        ArrayList<Element> near;
        DenseMatrix sigma = DenseMatrix.eye(this.nb_parameters);

        for (int i = 0; i < nb_iteration; i++) {
            near = nearest_neighbor(query, sigma);
            DenseMatrix B = B(near, query);
            DenseMatrix W = W(near, query);

            W = keepDiag(W);
            //B = DenseMatrix.eye((int) (B.shape().getValues()[0])).mul(B);
            sigma = W.mmul(
                    W.mmul(B.mmul(W)).add(DenseMatrix.eye(this.nb_parameters).mul(EPSILON))
            ).mmul(W);
        }

        near = nearest_neighbor(query, sigma);
        return majorClasse(near);
    }
}
