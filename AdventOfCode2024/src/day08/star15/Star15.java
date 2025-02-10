package day08.star15;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Star15 {
    public static void main(String[] args) {
        String filePath = "src\\day8\\map.txt";

        ArrayList<ArrayList<Character>> map = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = br.readLine()) != null) {
                // Split the line by space
                ArrayList<Character> charArrayList = line.chars()
                        .mapToObj(c -> (char) c)
                        .collect(Collectors.toCollection(ArrayList::new));
                map.add(charArrayList);
            }
        } catch (
                IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }

        //find all frequencies
        ArrayList<Character> frequencies = new ArrayList<>();
        for (ArrayList<Character> list : map) {
            for (Character c : list) {
                if(c != '.' && !frequencies.contains(c)){
                    frequencies.add(c);
                }
            }
        }

        ArrayList<ArrayList<Character>> copy = new ArrayList<>();
        for (ArrayList<Character> innerList : map) {
            copy.add(new ArrayList<>(innerList));
        }

        for(Character c: frequencies){
            //find all fields with the character
            ArrayList<int[]> fields = new ArrayList<>();
            for (int i = 0; i<= map.size()-1; i++) {
                for (int j = 0; j<= map.get(i).size()-1; j++) {
                    if(map.get(i).get(j) == c){
                        fields.add(new int[]{i, j});
                    }
                }
            }

            for (int[] field1: fields) {
               for (int[] field2: fields){
                   if(field1!=field2){
                       //antinodes are at positions a+(a-b)
                       if(field1[0]+(field1[0]-field2[0])>=0 && field1[0]+(field1[0]-field2[0])<=map.size()-1 &&
                               field1[1]+(field1[1]-field2[1])>=0 && field1[1]+(field1[1]-field2[1])<=map.get(field1[0]).size()-1){
                           copy.get(field1[0]+(field1[0]-field2[0])).set(field1[1]+(field1[1]-field2[1]), '#');
                       }
                       //and b+(b-a)
                       if(field2[0]+(field2[0]-field1[0])>=0 && field2[0]+(field2[0]-field1[0])<=map.size()-1 &&
                               field2[1]+(field2[1]-field1[1])>=0 && field2[1]+(field2[1]-field1[1])<=map.get(field2[0]).size()-1){
                           copy.get(field2[0]+(field2[0]-field1[0])).set(field2[1]+(field2[1]-field1[1]), '#');
                       }
                   }
                }
            }
        }

        //count '#' in copy
        int count = 0;
        for (ArrayList<Character> list : copy) {
            for (Character c : list) {
                if(c == '#'){
                    count += 1;
                }
            }
        }
        System.out.println("Number of fields: " + count);
    }
}