package day12.star24;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Star24 {
    public static void main(String[] args) {
        String filePath = "src\\day12\\map.txt";

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

            int price = 0;
            for (int i = 0; i <= map.size() - 1; i++) {
                for (int j = 0; j <= map.getFirst().size() - 1; j++) {
                    //found characters will be set to lowercase
                    if (Character.isUpperCase(map.get(i).get(j)) && map.get(i).get(j) != '.') {
                        //In the first method, the area is searched recursively, therefore not including areas that are not connected.
                        //The fields of this area are changed to lowercase.
                        //The second method searches for walls that are straight and not connected to the area.
                        //It is then marked with a dot.
                        int area = areaSearch(map, i, j, map.get(i).get(j));
                        int wall = wallSearch(map,map.get(i).get(j));
                        price += area*wall;
                    }
                }
            }

            System.out.println("The price is: " + price);

        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
    }

    public static int areaSearch(ArrayList<ArrayList<Character>> map, int i, int j, char currentChar){
        int results = 1;
        map.get(i).set(j, Character.toLowerCase(currentChar));
        if(inBound(map,i,j-1) && map.get(i).get(j-1) == currentChar) {
            results += areaSearch(map, i, j - 1, currentChar);
        }

        if(inBound(map,i,j+1) && map.get(i).get(j+1)== currentChar){
            results += areaSearch(map, i, j+1, currentChar);
        }

        if(inBound(map,i-1,j) && map.get(i-1).get(j)== currentChar){
            results += areaSearch(map, i-1, j, currentChar);
        }

        if(inBound(map,i+1,j) && map.get(i+1).get(j)== currentChar){
            results += areaSearch(map, i+1, j, currentChar);
        }
        return results;
    }

    /**
     * This method searches for the number of straight walls
     * @param map 2D map
     * @param currentChar the character that is being searched for
     * @return the number of straight walls
     */
    public static int wallSearch(ArrayList<ArrayList<Character>> map, Character currentChar){
        //The method observes 2x2 tiles of the map and counts the number of tiles with the correct character.
        //If there are 3 or 1 tiles with the correct character, the 2x2 tile is an outside or an inside corner.
        //->An additional wall is added.
        //If there are 2 tiles with the correct character, then the method checks if they are diagonal from each other.
        //As the complete area has to be connected additionally 2 walls are added.
        //->Two additional walls are added.
        int result = 0;
        for(int i =-1; i<= map.size(); i++){
            for(int j = -1; j<= map.getFirst().size(); j++){
                int count = 0;
                if (inBound(map,i,j) && map.get(i).get(j) == currentChar) count++;
                if (inBound(map,i,j+1) && map.get(i).get(j+1) == currentChar) count++;
                if (inBound(map,i+1,j) && map.get(i+1).get(j) == currentChar) count++;
                if (inBound(map,i+1,j+1) && map.get(i+1).get(j+1) == currentChar) count++;
                //3 is an inside corner 1 is an outside corner
                if (count == 3 || count == 1) {
                    result +=1;
                }
                //if 2 are diagonal from each other, then the area has 2 more walls
                if(count == 2){
                    if(inBound(map,i,j) && inBound(map,i+1,j+1) && map.get(i).get(j) == currentChar && map.get(i+1).get(j+1) == currentChar){
                        result += 2;
                    }
                    if(inBound(map,i,j+1) && inBound(map,i+1,j) && map.get(i).get(j+1) == currentChar && map.get(i+1).get(j) == currentChar){
                        result += 2;
                    }
                }
                if(inBound(map,i,j) && map.get(i).get(j) == currentChar){
                    map.get(i).set(j, '.');
                }
            }
        }
        return result;
    }

    public static boolean inBound(ArrayList<ArrayList<Character>> map, int i, int j){
        return i >= 0 && i < map.size() && j >= 0 && j < map.get(0).size();
    }
}
