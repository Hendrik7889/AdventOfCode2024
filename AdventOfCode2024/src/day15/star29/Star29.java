package day15.star29;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Star29 {
    public static void main(String[] args) {
        String filePath = "src\\day15\\input.txt";

        int robotX=0;
        int robotY=0;
        ArrayList<ArrayList<Character>> map = new ArrayList<>();
        ArrayList<Character> sequence = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;
            int i = 0;
            while (!(line = br.readLine()).isEmpty()) {
                ArrayList<Character> charList = new ArrayList<>();
                for (int j = 0; j < line.length(); j++) {
                    charList.add(line.charAt(j));
                    if(line.charAt(j) == '@'){
                        robotY = i;
                        robotX = charList.size()-1;
                    }
                }
                map.add(charList);
                i++;
            }
            while ((line = br.readLine()) != null) {
                for (char c : line.toCharArray()) {
                    sequence.add(c);
                }
            }

        } catch (
                IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }

        //steps
        for (Character c : sequence) {
            boolean push = true;
            int size = 0;
            switch (c) {
                case '^':
                    for (int y = robotY; y >= 0; y--) {
                        if (map.get(y).get(robotX) == '.') {
                            break;
                        }
                        if (map.get(y).get(robotX) == '#') {
                            push = false;
                            break;
                        }
                        size++;
                    }

                    if (push) {
                        for (int y = robotY - 1; y > robotY - size - 1; y--) {
                            map.get(y).set(robotX, 'O');
                        }
                        map.get(robotY).set(robotX, '.');
                        robotY--;
                        map.get(robotY).set(robotX, '@');


                    }
                    break;
                case 'v':
                    for (int y = robotY; y < map.size(); y++) {
                        if (map.get(y).get(robotX) == '.') {
                            break;
                        }
                        if (map.get(y).get(robotX) == '#') {
                            push = false;
                            break;
                        }
                        size++;
                    }

                    if (push) {
                        for (int y = robotY + 1; y < robotY + size + 1; y++) {
                            map.get(y).set(robotX, 'O');
                        }
                        map.get(robotY).set(robotX, '.');
                        robotY++;
                        map.get(robotY).set(robotX, '@');
                    }
                    break;
                case '<':
                    for (int x = robotX; x >= 0; x--) {
                        if (map.get(robotY).get(x) == '.') {
                            break;
                        }
                        if (map.get(robotY).get(x) == '#') {
                            push = false;
                            break;
                        }
                        size++;
                    }

                    if (push) {
                        for (int x = robotX - 1; x > robotX - size - 1; x--) {
                            map.get(robotY).set(x, 'O');
                        }
                        map.get(robotY).set(robotX, '.');
                        robotX--;
                        if(robotX < 0){
                            robotX = 0;
                        }
                        map.get(robotY).set(robotX, '@');
                    }
                    break;
                case '>':
                    for (int x = robotX; x < map.get(robotY).size(); x++) {
                        if (map.get(robotY).get(x) == '.') {
                            break;
                        }
                        if (map.get(robotY).get(x) == '#') {
                            push = false;
                            break;
                        }
                        size++;
                    }

                    if (push) {
                        for (int x = robotX + 1; x < robotX + size + 1; x++) {
                            map.get(robotY).set(x, 'O');
                        }
                        map.get(robotY).set(robotX, '.');
                        robotX++;
                        map.get(robotY).set(robotX, '@');
                    }
                    break;
            }
        }

        //calculate the result
        int result = 0;
        for(int i = 0; i< map.size(); i++){
            for(int j=0;j< map.getFirst().size();j++){
                if(map.get(i).get(j) == 'O'){
                    result = result + 100* i + j;
                }
            }
        }
        System.out.println("Result: " +result);
    }
}