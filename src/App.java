import classifier.KNN;
import elements.DataIrisLecture;
import elements.Iris;
import jeigen.DenseMatrix;

import java.util.*;

public class App {



    public static void main(String[] args){

        int k = 3;

        DenseMatrix m = new DenseMatrix( new double[][]{{1,1,1},{1,-1,1},{1,1,1}} );

        HashSet<Iris> know_iris = DataIrisLecture.proceed("data/iris.data");
        Iris query = new Iris(3.6f,1.4f,5.5f, 0.2f);

        System.out.println(KNN.proceed(query,3,know_iris));
    }
}
