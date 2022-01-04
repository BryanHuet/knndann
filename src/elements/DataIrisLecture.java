package elements;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;

public class DataIrisLecture {
    public static HashSet<Iris> proceed(String path_to_file){
        HashSet<Iris> iris_set = new HashSet<>();
        BufferedReader reader;
        try{
            reader = new BufferedReader(new FileReader(path_to_file));
            String line = reader.readLine();
            while(line != null){
                String[] attributes = line.split(",");
                iris_set.add(new Iris(Float.parseFloat(attributes[0]),
                        Float.parseFloat(attributes[1]),
                        Float.parseFloat(attributes[2]),
                        Float.parseFloat(attributes[3]),
                        attributes[4]));
                line = reader.readLine();
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

        return iris_set;
    }
}
