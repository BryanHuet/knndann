
import jeigen.DenseMatrix;
import static jeigen.Shortcuts.*;
public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("------------------");
        DenseMatrix m = new DenseMatrix( new double[][]{{1,1,1},{1,-1,1},{1,1,1}} );

        System.out.println(m.mul(2));
    }
}
