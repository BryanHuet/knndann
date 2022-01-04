package classifier;

public class Result{
    double distance;
    String classe;
    public Result(double distance, String classe){
        this.distance=distance;
        this.classe=classe;
    }

    public double getDistance() {
        return distance;
    }

    public String getClasse() {
        return classe;
    }
}