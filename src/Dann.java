import org.ejml.data.Matrix;
import org.ejml.simple.*;
import jeigen.DenseMatrix;
import static jeigen.Shortcuts.*;
import java.util.*;
public class Dann{

        
        List<Point> data;
        Point x0;

        public Dann(){}

        public static SimpleMatrix metric2(SimpleMatrix B, SimpleMatrix W){
                SimpleMatrix Winvert = W.invert();
                return Winvert.mult(B.mult(Winvert));
        }





}