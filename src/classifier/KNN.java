package classifier;
import elements.Iris;

import java.util.*;

public class KNN {

    public static String majorClasse(List<Result> list,int k){
        String soluce="";
        HashMap<String, Integer> dico = new HashMap<>();
        for(int i=0;i<k;i++){
            String classe = list.get(i).getClasse();
            if(dico.containsKey(classe)){
                dico.put(classe,dico.get(classe)+1);
            }else{
                dico.put(classe,1);
            }
        }
        for (Map.Entry<String, Integer> entry : dico.entrySet()){
            if(entry.getValue().equals(Collections.max(dico.values()))){
                soluce=entry.getKey();
            }
        }
        return soluce;
    }

    public static String proceed(Iris query,int k, HashSet<Iris> data){
        List<Result> result = new ArrayList<>();

        for(Iris iris : data){
            double dist;

            dist = Math.pow(iris.getPetal_length()-query.getPetal_length(),2);
            dist += Math.pow(iris.getPetal_width()- query.getPetal_width(), 2);
            dist += Math.pow(iris.getSepal_length()- query.getPetal_length(), 2);
            dist += Math.pow(iris.getSepal_width()- query.getSepal_width(), 2);
            double distance = Math.sqrt(dist);
            result.add(new Result(distance, iris.getClasse()));
        }
        result.sort(new DistanceComparator());
        /*
        for(int x =0; x<k;x++){
            System.out.println(result.get(x).getClasse() + " " + result.get(x).getDistance());
        }*/
        return majorClasse(result,k);
    }


}
