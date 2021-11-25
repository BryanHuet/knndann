import org.ejml.data.Matrix;
import org.ejml.simple.*;
import jeigen.DenseMatrix;
import static jeigen.Shortcuts.*;
import java.util.*;
public class Dann{

        
        static List<Point> data;
        Point x0;
        static int N;
        static int J;


        public Dann(){}

        public static SimpleMatrix metric2(SimpleMatrix B, SimpleMatrix W){
                SimpleMatrix Winvert = W.invert();
                return Winvert.mult(B.mult(Winvert));
        }

        public static DenseMatrix B(Point x0, DenseMatrix E){
                DenseMatrix B= DenseMatrix.eye(3);
                long pi=1;
                for (int j=0; j< J;j++){
                        DenseMatrix distance = data.get(j).distance(x0).toMatrix();
                        B= distance.mmul(distance.t());
                }
                return B;
        }

        public static DenseMatrix W(Point x0, DenseMatrix E){
                DenseMatrix W= DenseMatrix.eye(3);
                return W;
        }



}