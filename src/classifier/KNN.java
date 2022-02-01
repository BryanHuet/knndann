package classifier;
import elements.Element;
import jeigen.DenseMatrix;

import java.util.*;

public class KNN {

    public static int majorClasse(List<Result> list,int k){
        int soluce=-1;
        HashMap<Integer, Integer> dico = new HashMap<>();
        for(int i=0;i<k;i++){
            int classe = list.get(i).getClasse();
            if(dico.containsKey(classe)){
                dico.put(classe,dico.get(classe)+1);
            }else{
                dico.put(classe,1);
            }
        }
        for (Map.Entry<Integer, Integer> entry : dico.entrySet()){
            if(entry.getValue().equals(Collections.max(dico.values()))){
                soluce=entry.getKey();
            }
        }
        return soluce;
    }
    public static double distanceEuclidienne(Element xi, Element x0){
        double result=0;
        DenseMatrix calcul = xi.getVector().sub(x0.getVector());
        calcul = calcul.mul(calcul);
        calcul = calcul.sumOverCols();
        result = calcul.getValues()[0];
        return Math.sqrt(result);
    }

    public static int proceed(Element query, int k, HashSet<Element> data){
        List<Result> result = new ArrayList<>();

        for(Element e : data){
            result.add(new Result(distanceEuclidienne(e,query), e.getClasse()));
        }
        result.sort(new DistanceComparator());
        /*
        for(int x =0; x<k;x++){
            System.out.println(result.get(x).getClasse() + " " + result.get(x).getDistance());
        }*/
        return majorClasse(result,k);
    }


}
