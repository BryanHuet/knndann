package classifier;
import java.util.Comparator;

public class DistanceComparator implements Comparator<Result> {
    @Override
    public int compare(Result a, Result b){
        return Double.compare(a.distance, b.distance);
    }
}