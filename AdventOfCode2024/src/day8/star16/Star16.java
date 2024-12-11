package day8.star16;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Star16 {
    public static void main(String[] args) {
        String filePath = "src/day8/map.txt";

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
                        boolean inBound = true;
                        int i = 0;
                        //antinodes are at positions a+i*(a-b)
                        while(inBound) {
                            int newRow = field1[0] + i * (field1[0] - field2[0]);
                            int newCol = field1[1] + i * (field1[1] - field2[1]);
                            if (isWithinBounds(newRow, newCol, map.size(), map.getFirst().size())) {
                                copy.get(newRow).set(newCol, '#');
                                i += 1;
                            }else {
                                inBound = false;
                            }
                        }
                        inBound = true;
                        i = 0;
                        //and b+i*(b-a)
                        while (inBound) {
                            int newRow = field2[0] + i * (field2[0] - field1[0]);
                            int newCol = field2[1] + i * (field2[1] - field1[1]);
                            if (isWithinBounds(newRow, newCol, map.size(), map.getFirst().size())) {
                                copy.get(newRow).set(newCol, '#');
                                i += 1;
                            }else {
                                inBound = false;
                            }
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

    private static boolean isWithinBounds(int row, int col, int numRows, int numCols) {
        return row >= 0 && row < numRows && col >= 0 && col < numCols;
    }
}