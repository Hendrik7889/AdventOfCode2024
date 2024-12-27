package day19.star37;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Star37 {
    public static void main(String[] args){
        String filePath = "src\\day19\\input.txt";

        ArrayList<String> towels = new ArrayList<>();
        ArrayList<String> patterns = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line = br.readLine();
            //line of all towels
            if(line != null && !line.isEmpty()){
                towels = new ArrayList<>(List.of(line.split(", ")));
            }

            br.readLine(); // empty line

            //lies of combination of towels
            while ((line = br.readLine()) != null && !line.isEmpty()) {
                patterns.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }

        //start here
        int count =0;
        for(String pattern: patterns){
            if(recursiveMatch(pattern,towels)){
                count++;
            }
        }
        System.out.println("Result is :" + count);
    }

    public static boolean recursiveMatch(String s, ArrayList<String> towels){
        if(s.isEmpty()){
            return true;
        }
        for(String towel: towels){
            if(s.startsWith(towel)){
                if(recursiveMatch(s.substring(towel.length()),towels)){
                    return true;
                }
            }
        }
        return false;
    }
}