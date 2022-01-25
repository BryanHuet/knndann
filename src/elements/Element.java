package elements;

import jeigen.DenseMatrix;

public class Element {

    private DenseMatrix vector;
    private int classe;

    public Element(DenseMatrix vector, int classe){
        this.vector = vector;
        this.classe = classe;
    }

    public DenseMatrix getVector() {
        return vector;
    }

    public void setVector(DenseMatrix vector) {
        this.vector = vector;
    }

    public int getClasse() {
        return classe;
    }

    public void setClasse(int classe) {
        this.classe = classe;
    }
}
