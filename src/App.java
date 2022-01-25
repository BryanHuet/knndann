import classifier.DANN;
import elements.DataIrisLecture;
import elements.Iris;
import elements.Element;
import jeigen.DenseMatrix;

import java.util.*;

public class App {



    public static void main(String[] args){

        int k = 3;

        HashSet<Element> know_iris = DataIrisLecture.proceed("data/iris.data");
        DenseMatrix query = new DenseMatrix( new double[][]{{3.6, 1.4, 5.5, 0.2}});
        Element A = new Element(new DenseMatrix( new double[][]{{1, 1, 1, 1}}),0);
        Element B = new Element(new DenseMatrix( new double[][]{{3, 3, 3, 3}}),0);

        System.out.println(DANN.distanceEuclidienne(A,B));



    }
}
