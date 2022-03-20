package validator;
import java.util.*;
import elements.Element;

public class Fold {

    private ArrayList<Element> train_set;
    private ArrayList<Element> validation_set;
    private double score;

    public Fold(ArrayList<Element> train_set, ArrayList<Element> validation_set) {
        this.train_set = train_set;
        this.validation_set = validation_set;
        this.score = 0;
    }

    public ArrayList<Element> getTrain_set() {
        return this.train_set;
    }

    public ArrayList<Element> getValidation_set() {
        return this.validation_set;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public double getScore() {
        return this.score;
    }
}
