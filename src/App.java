import elements.NumericalParser;
import elements.Element;
import validator.*;
import java.util.*;

public class App {

    public static void main(String[] args) {

        /* VERIFICATION DES INPUTS  */

        if (args.length == 0) {
            throw new IllegalArgumentException("Usage : App < chemin fichier > < ensemble des k voisins à tester > < nombre de découpes CV >" +
                    "\n" + "ex: java App /data/iris.data 3 5 8 5 5");
        }
        if (args.length != 3) {
            throw new IllegalArgumentException("Le nombre d'arguments est incorrect");
        }

        String chemin = args[0];

        ArrayList<Integer> k = new ArrayList<>();
        for (String s : args[1].split(" ")) {
            try {
                int number = Integer.parseInt(s);
                if (number < 1) {
                    throw new ArithmeticException("k doit comporter uniquement des nombres >= à 1");
                }
                k.add(number);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        int nb_blocs = Integer.parseInt(args[2]);

        if (nb_blocs < 2) {
            throw new IllegalArgumentException("Le nombre de blocs ne peut pas être < à 2");
        }

        /* "PARSAGE" DES DONNEES */

        NumericalParser data_parse = new NumericalParser(chemin);
        HashSet<Element> data = data_parse.proceed();

        if (nb_blocs > data.size() / 2) {
            throw new IllegalArgumentException("Le nombre de blocs ne peut pas être > à la longueur du dataset/2");
        }


        /* APPLICATION */

        CrossValidation bc = new CrossValidation(nb_blocs, data, new HashSet<>(k), data_parse.getParametersNumber(), data_parse.getClassNumber());
        bc.crossValidation();
        bc.displayResults(chemin);


    }
}
