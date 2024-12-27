package day19.star38;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Star38 {
    public static void main(String[] args){
        String filePath = "src\\day19\\input.txt";

        ArrayList<String> towels = new ArrayList<>();
        ArrayList<String> patterns = new ArrayList<>();
        ArrayList<String> parts = new ArrayList<>();

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

        int maxLength =0;
        for(String s: towels){
            if(maxLength< s.length()){
                maxLength = s.length();
            }
        }

        //calculate for every position in the pattern how it can be reached
        long count =0;
        for(String pattern: patterns){
            long[] positions = new long[pattern.length()+1];
            positions[0] = 1;
            for(int i =0 ; i <= pattern.length()-1 ; i++){
                //calculate all future positions that can be reached from the current
                //NOTE that the array is shifted to the right by one as the init position 0 with the value 1 is needed
                calculatePosition(pattern,i,towels,positions);
            }
            count+= positions[pattern.length()];
        }
        System.out.println();
        System.out.println("Result is :" + count);
    }

    private static void calculatePosition(String s, int pos, ArrayList<String> towels, long[] positions ){
        for(String t: towels){
            String b = s.substring(pos);
            //add the number of possible combinations if a future position can be reached
            if(b.startsWith(t)){
                positions[pos+t.length()] += positions[pos];
            }
        }
    }
}