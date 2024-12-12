package day10.star19;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class Star19 {
    public static void main(String[] args) {
        String filePath = "src\\day10\\map.txt";

        ArrayList<ArrayList<Integer>> map = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = br.readLine()) != null) {
                ArrayList<Integer> row = new ArrayList<>();
                for (char c : line.toCharArray()) {
                    if (Character.isDigit(c)) {
                        row.add(Character.getNumericValue(c));
                    }
                }
                map.add(row);
            }
            //go over the map and only start if the value is 0
            int numberOfPaths = 0;
            for(int i = 0 ; i<= map.size()-1; i++){
                for(int j = 0; j<= map.getFirst().size()-1; j++){
                    if(map.get(i).get(j) == 0){
                        ArrayList<int[]> arrayList= new ArrayList<>();
                        recursiveHiking(map, i, j, 0, arrayList);
                        numberOfPaths += arrayList.size();
                    }
                }
            }
            System.out.println("Number of paths: " + numberOfPaths);
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
    }

    public static void recursiveHiking(ArrayList<ArrayList<Integer>> map, int i, int j, int height, Collection<int[]> foundPaths){
        if(height == 9){
            int[] arr = new int[]{i, j};
            if(foundPaths.stream().anyMatch(array -> Arrays.equals(array, arr))){
                return;
            }
            foundPaths.add(new int[]{i, j});
            return;
        }
        if(inBound(map, i-1, j) && map.get(i-1).get(j) == height+1){
            recursiveHiking(map, i-1, j, height+1,foundPaths);
        }
        if(inBound(map, i+1, j) && map.get(i+1).get(j) == height+1){
            recursiveHiking(map, i+1, j, height+1,foundPaths);
        }
        if(inBound(map, i, j-1) && map.get(i).get(j-1) == height+1){
            recursiveHiking(map, i, j-1, height+1,foundPaths);
        }
        if(inBound(map, i, j+1) && map.get(i).get(j+1) == height+1){
            recursiveHiking(map, i, j+1, height+1,foundPaths);
        }
    }

    public static boolean inBound(ArrayList<ArrayList<Integer>> map, int i, int j){
        return i<= map.size()-1 && j<= map.getFirst().size()-1 && i>=0 && j>=0;
    }
}
