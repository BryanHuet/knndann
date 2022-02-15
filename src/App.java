import classifier.DANN;
import classifier.KNN;
import elements.DataIrisLecture;
import elements.Element;
import jeigen.DenseMatrix;

import java.util.*;

import static jeigen.DenseMatrix.diag;
import static jeigen.Shortcuts.rand;
import static jeigen.Shortcuts.spdiag;

public class App {



    public static void main(String[] args){

        int k = 3;

        String[] classes = {"Iris-setosa","Iris-versicolor","Iris-virginica"};

        HashSet<Element> know_iris = DataIrisLecture.proceed("data/iris.data");

        DenseMatrix query = new DenseMatrix( new double[][]{{5.0, 1.4, 5.5, 0.2}});
        //DenseMatrix query = new DenseMatrix(new double[][]{{5.6, 3, 4.5, 1.5}});
        Element x0 = new Element(query,0);


        int result_DANN = DANN.proceed(x0,k,3,know_iris);
        System.out.println("");
        int result_KNN = KNN.proceed(x0,k,know_iris);

        System.out.println("\n----------- CLASSIFICATION DE x0 ------------");

        System.out.println("DANN : " +  classes[result_DANN]);
        System.out.println("KNN :  " +  classes[result_KNN]);

        System.out.println("---------------------------------------------");
        DenseMatrix test = DenseMatrix.eye(4);


        //System.out.println(DANN.Wdiag(DANN.W(new ArrayList<>(know_iris),x0)));

        //System.out.println(test.shape().getValues()[0]);



          //kfold, pour l'études des tests
          //transformer w en mat diagonals





    }
}
