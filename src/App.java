import jeigen.DenseMatrix;
public class App {
    public static void main(String[] args){
        System.out.println("Hello here");
        System.out.println("OK");
        DenseMatrix m = new DenseMatrix( new double[][]{{1,1,1},{1,-1,1},{1,1,1}} );
        System.out.println(m);
    }
}
