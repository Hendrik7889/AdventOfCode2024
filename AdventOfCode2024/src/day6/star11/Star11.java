package day6.star11;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Star11 {
    public static void main(String[] args) {
        String filePath = "src/day6/map.txt";

        ArrayList<ArrayList<Character>> mainList = new ArrayList<>();

            try{
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = br.readLine()) != null) {
                // Split the line by space
                ArrayList<Character> charArrayList = line.chars()
                        .mapToObj(c -> (char) c)
                        .collect(Collectors.toCollection(ArrayList::new));
                mainList.add(charArrayList);
            }
        } catch (
        IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }

        simulate(mainList);
        int count = 0;
        for (ArrayList<Character> list : mainList) {
            for (Character c : list) {
                if(c == 'X'){
                    count += 1;
                }
            }
        }
        System.out.println("Number of fields: " + count);


    }

    private static void simulate(ArrayList<ArrayList<Character>> mainList) {
        int playerX = 0;
        int playerY = 0;
        for(ArrayList<Character> list : mainList) {
            for (int i = 0; i <= list.size() - 1; i++) {
                if (list.get(i).equals('^')) {
                    playerX = i;
                    playerY = mainList.indexOf(list);
                }
            }
        }

        char currentDirection = '^';
        while (playerX <= mainList.getFirst().size()-1 && playerY <= mainList.size()-1 && playerX>=0 && playerY>=0) {
            if (currentDirection == '^') {
                if (playerY - 1 < 0) {
                    mainList.get(playerY).set(playerX, 'X');
                    return;
                }
                if (mainList.get(playerY - 1).get(playerX) == '#') {
                    currentDirection = '>';
                } else {
                    mainList.get(playerY).set(playerX, 'X');
                    playerY -= 1;
                }
            }
            if (currentDirection == '>') {
                if (playerX + 1 > mainList.get(playerY).size() - 1) {
                    mainList.get(playerY).set(playerX, 'X');
                    return;
                }
                if (mainList.get(playerY).get(playerX + 1) == '#') {
                    currentDirection = 'v';
                } else {
                    mainList.get(playerY).set(playerX, 'X');
                    playerX += 1;
                }
            }
            if(currentDirection == 'v'){
                if(playerY + 1 > mainList.size()-1){
                    mainList.get(playerY).set(playerX, 'X');
                    return;
                }
                if(mainList.get(playerY + 1).get(playerX) == '#'){
                    currentDirection = '<';
                }else{
                    mainList.get(playerY).set(playerX, 'X');
                    playerY += 1;
                }
            }
            if (currentDirection == '<') {
                if (playerX - 1 < 0) {
                    mainList.get(playerY).set(playerX, 'X');
                    return;
                }
                if (mainList.get(playerY).get(playerX - 1) == '#') {
                    currentDirection = '^';
                } else {
                    mainList.get(playerY).set(playerX, 'X');
                    playerX -= 1;
                }
            }
        }
    }
}
