import elements.DataIrisLecture;
import jeigen.DenseMatrix;
public class App {
    public static void main(String[] args){

        System.out.println("Hello here");
        DenseMatrix m = new DenseMatrix( new double[][]{{1,1,1},{1,-1,1},{1,1,1}} );

        System.out.println(DataIrisLecture.proceed("data/iris.data"));

    }
}
