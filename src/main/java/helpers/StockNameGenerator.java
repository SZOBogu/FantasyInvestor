package helpers;

import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class StockNameGenerator {
    private static List<String> prefixes = new ArrayList<>(Arrays.asList("Global", "International", "Worldwide", "Trans Atlantic", "United", "Scrooge McDuck's"));
    private static List<String> words = new ArrayList<>(Arrays.asList("Sawmill", "Quarry", "Mine", "Bank", "Fund", "Insurance", "Entertainments", "Factory", "Club", "Transports", "Airport"));
    private static List<String> suffixes = new ArrayList<>(Arrays.asList("of Russia", "from Texas", "of Poland", "LTD", "LLC", "Company", "Enterprises", "FC", "S.P. z.o.o."));

    public static String generateName(){
        Random random = new Random();

        String name = "";

        if(random.nextInt(2) == 1){
            name = prefixes.get(random.nextInt(prefixes.size())) + " ";
        }
        name += words.get(random.nextInt(words.size()));

        if(random.nextInt(2) == 1){
            name += " " + suffixes.get(random.nextInt(prefixes.size()));
        }

        return name;
    }

}
