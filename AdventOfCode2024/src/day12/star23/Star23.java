package day12.star23;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Star23 {
    public static void main(String[] args) {
        String filePath = "C:\\Users\\HendrikBru\\Documents\\GitHub\\AdventOfCode2024\\AdventOfCode2024\\src\\day12\\map.txt";

        ArrayList<ArrayList<Character>> map = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = br.readLine()) != null) {
                ArrayList<Character> row = new ArrayList<>();
                for (char c : line.toCharArray()) {
                        row.add(c);
                }
                map.add(row);
            }

            //go over the map and only start if the value is 0
            int price = 0;
            for (int i = 0; i <= map.size() - 1; i++) {
                for (int j = 0; j <= map.getFirst().size() - 1; j++) {
                    //found characters will be set to lowercase
                    if ( Character.isUpperCase(map.get(i).get(j))) {
                        int[] result = recursiveSearch(map, i, j, map.get(i).get(j));
                        System.out.println("Found tiles: " + result[0] + " and walls: " + result[1]);
                        price += result[0] * result[1];
                    }
                }
            }

            System.out.println("The price is: " + price);

        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
    }

    public static int[] recursiveSearch(ArrayList<ArrayList<Character>> map, int i, int j, char currentChar){
        int[] results = new int[]{1,0};
        map.get(i).set(j, Character.toLowerCase(currentChar));
        if(inBound(map,i,j-1) && map.get(i).get(j-1) == currentChar){
            int [] a = recursiveSearch(map, i, j-1, currentChar);
            results[0] += a[0];
            results[1] += a[1];
        } else{
            if(!inBound(map,i,j-1) || Character.toUpperCase(map.get(i).get(j-1)) != currentChar){
                results[1] +=1;
            }
        }

        if(inBound(map,i,j+1) && map.get(i).get(j+1)== currentChar){
            int [] a = recursiveSearch(map, i, j+1, currentChar);
            results[0] += a[0];
            results[1] += a[1];
        } else {
            if(!inBound(map,i,j+1) || Character.toUpperCase(map.get(i).get(j+1)) != currentChar){
                results[1] +=1;
            }
        }

        if(inBound(map,i-1,j) && map.get(i-1).get(j)== currentChar){
            int [] a = recursiveSearch(map, i-1, j, currentChar);
            results[0] += a[0];
            results[1] += a[1];
        } else {
            if(!inBound(map,i-1,j) || Character.toUpperCase(map.get(i-1).get(j)) != currentChar){
                results[1] +=1;
            }
        }

        if(inBound(map,i+1,j) && map.get(i+1).get(j)== currentChar){
            int [] a = recursiveSearch(map, i+1, j, currentChar);
            results[0] += a[0];
            results[1] += a[1];
        } else {
            if(!inBound(map,i+1,j) || Character.toUpperCase(map.get(i+1).get(j)) != currentChar){
                results[1] +=1;
            }
        }
        return results;
    }

    public static boolean inBound(ArrayList<ArrayList<Character>> map, int i, int j){
        return i >= 0 && i < map.size() && j >= 0 && j < map.get(0).size();
    }
}
