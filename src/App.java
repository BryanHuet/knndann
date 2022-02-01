import classifier.DANN;
import classifier.KNN;
import elements.DataIrisLecture;
import elements.Element;
import jeigen.DenseMatrix;

import java.util.*;

public class App {



    public static void main(String[] args){

        int k = 3;

        String[] classes = {"Iris-setosa","Iris-versicolor","Iris-virginica"};

        HashSet<Element> know_iris = DataIrisLecture.proceed("data/iris.data");
        DenseMatrix query = new DenseMatrix( new double[][]{{3.1, 1.4, 5.5, 0.2}});
        Element x0 = new Element(query,0);

        int result_DANN = DANN.proceed(x0,k,1,know_iris);
        int result_KNN = KNN.proceed(x0,k,know_iris);

        System.out.println("DANN : " +  classes[result_DANN]);
        System.out.println("KNN :  " +  classes[result_KNN]);
    }
}
