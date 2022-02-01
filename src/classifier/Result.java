package classifier;

public class Result{
    double distance;
    int classe;
    public Result(double distance, int classe){
        this.distance=distance;
        this.classe=classe;
    }

    public double getDistance() {
        return distance;
    }

    public int getClasse() {
        return classe;
    }
}