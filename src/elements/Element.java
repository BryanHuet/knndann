package elements;

import jeigen.DenseMatrix;

public class Element {

    private DenseMatrix vector;
    private int classe;
    private int predict;

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
        return this.classe;
    }

    public void setClasse(int classe) {
        this.classe = classe;
    }

    public int getPredict(){
        return this.predict;
    }
    
    public void setPredict(int predict){
        this.predict = predict;
    }


    @Override
    public String toString(){
        return "vector :"+this.getVector() +"| classe :"+this.getClasse();
    }
}
