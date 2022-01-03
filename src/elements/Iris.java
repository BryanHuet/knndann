package elements;

public class Iris {
    private float sepal_length;
    private float sepal_width;
    private float petal_length;
    private float petal_width;
    private String classe;

    public Iris(float sepal_length, float sepal_width, float petal_length, float petal_width, String classe) {
        this.sepal_length = sepal_length;
        this.sepal_width = sepal_width;
        this.petal_length = petal_length;
        this.petal_width = petal_width;
        this.classe = classe;
    }

    public float getSepal_length() {
        return sepal_length;
    }

    public void setSepal_length(float sepal_length) {
        this.sepal_length = sepal_length;
    }

    public float getSepal_width() {
        return sepal_width;
    }

    public void setSepal_width(float sepal_width) {
        this.sepal_width = sepal_width;
    }

    public float getPetal_length() {
        return petal_length;
    }

    public void setPetal_length(float petal_length) {
        this.petal_length = petal_length;
    }

    public float getPetal_width() {
        return petal_width;
    }

    public void setPetal_width(float petal_width) {
        this.petal_width = petal_width;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }
}
