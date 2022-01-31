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
        ArrayList<Element> iris = new ArrayList<>(know_iris);

        DenseMatrix query = new DenseMatrix( new double[][]{{3.6, 1.4, 5.5, 0.2}});
        Element x0 = new Element(query,0);
        Element A = new Element(new DenseMatrix( new double[][]{{0.3, 1.1, 1, 2.3}}),0);
        Element B1 = new Element(new DenseMatrix( new double[][]{{6.1, 3, 3.2, 3}}),1);
        Element C = new Element(new DenseMatrix( new double[][]{{1.2, 4.5, 3, 3}}),2);

        System.out.println(DANN.B(iris,x0));
        System.out.println(DANN.W(iris,x0));
        DenseMatrix B = DANN.B(iris,x0);
        DenseMatrix W = DANN.W(iris,x0);
        DenseMatrix sigma = DenseMatrix.eye(4);
        System.out.println(sigma);
        sigma = W.mmul(
                    W.mmul(B.mmul(W)).add(DenseMatrix.eye(4).mul(1))
                ).mmul(W);
        System.out.println(sigma);
        //System.out.println(DANN.distanceEuclidienne(A,B));



    }
}
