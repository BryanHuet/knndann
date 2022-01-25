package elements;
import jeigen.DenseMatrix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;

public class DataIrisLecture {
    public static HashSet<Element> proceed(String path_to_file){
        HashSet<Element> iris_set = new HashSet<>();
        BufferedReader reader;
        try{
            reader = new BufferedReader(new FileReader(path_to_file));
            String line = reader.readLine();
            while(line != null){
                String[] attributes = line.split(",");
                int classe = 0;
                DenseMatrix vector = new DenseMatrix(new double[][]{{
                        Double.parseDouble(attributes[0]),
                        Double.parseDouble(attributes[1]),
                        Double.parseDouble(attributes[2]),
                        Double.parseDouble(attributes[3]),
                }});
                switch (attributes[4]) {
                    case "Iris-versicolor":
                        classe=1;
                        break;
                    case "Iris-virginica":
                        classe=2;
                        break;
                    default:
                        break;
                }
                iris_set.add(new Element(vector,classe));
                line = reader.readLine();
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

        return iris_set;
    }
}
