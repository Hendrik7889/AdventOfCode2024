package day15.star30;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Star30 {
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
                    if(line.charAt(j) == '#'){
                        charList.add('#');
                        charList.add('#');
                    }else if(line.charAt(j) == '.'){
                        charList.add('.');
                        charList.add('.');
                    }else if(line.charAt(j) == '@'){
                        robotY = i;
                        robotX = (j)*2;
                        charList.add('@');
                        charList.add('.');
                    }else if(line.charAt(j) == 'O'){
                        charList.add('[');
                        charList.add(']');
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
            boolean stepped = step(robotY, robotX, map,c,false);
            if (stepped) {
                switch (c) {
                    case '^':
                        step(robotY, robotX, map,c,true);
                        robotY--;
                        break;
                    case 'v':
                        step(robotY, robotX, map,c,true);
                        robotY++;
                        break;
                    case '<':
                        robotX--;
                        break;
                    case '>':
                        robotX++;
                        break;
                }
            }
        }

        //calculate the result
        int result = 0;
        for(int i = 0; i< map.size(); i++){
            for(int j=0;j< map.getFirst().size();j++){
                System.out.print(map.get(i).get(j));
                if(map.get(i).get(j) == '['){

                    result = result + 100* i + j;
                }
            }
            System.out.println();
        }
        System.out.println("Result: " +result);
    }

    /**
     * This method is used to check if the current position is pushable
     * @param positionY the current position Y
     * @param positionX the current position X Exactly this position is checked and the method calls itself recursively
     * @param map the map
     * @param c the current character
     * @return
     */
    private static boolean step(int positionY, int positionX, ArrayList<ArrayList<Character>> map, Character c, boolean pushIt) {
        boolean push = true;
        int size = 0;
        switch (c) {
            case '^':
                //region ^
                if (map.get(positionY).get(positionX) == '[') {
                    boolean push1 = step(positionY - 1, positionX, map, '^', pushIt);
                    boolean push2 = step(positionY - 1, positionX + 1, map, '^', pushIt);
                    if (push1 && push2) {
                        if (pushIt) {
                            map.get(positionY - 1).set(positionX, '[');
                            map.get(positionY - 1).set(positionX + 1, ']');
                            map.get(positionY).set(positionX, '.');
                            map.get(positionY).set(positionX + 1, '.');
                        }
                        return true;
                    }
                }

                if (map.get(positionY).get(positionX) == ']') {
                    boolean push1 = step(positionY - 1, positionX, map, '^', pushIt);
                    boolean push2 = step(positionY - 1, positionX - 1, map, '^', pushIt);
                    if (push1 && push2) {
                        if (pushIt) {
                            map.get(positionY - 1).set(positionX - 1, '[');
                            map.get(positionY - 1).set(positionX, ']');
                            map.get(positionY).set(positionX, '.');
                            map.get(positionY).set(positionX - 1, '.');
                        }
                        return true;
                    }
                }

                if (map.get(positionY).get(positionX) == '@') {
                    boolean push1 = step(positionY - 1, positionX, map, '^', pushIt);
                    if (push1) {
                        if (pushIt) {
                            map.get(positionY - 1).set(positionX, '@');
                            map.get(positionY).set(positionX, '.');
                        }
                        return true;
                    }
                }

                if (map.get(positionY).get(positionX) == '.') {
                    return true;
                }
                return false;
            //endregion
            case 'v':
                //region v
                if (map.get(positionY).get(positionX) == '[') {
                    boolean push1 = step(positionY + 1, positionX, map, 'v', pushIt);
                    boolean push2 = step(positionY + 1, positionX + 1, map, 'v', pushIt);
                    if (push1 && push2) {
                        if (pushIt) {
                            map.get(positionY + 1).set(positionX, '[');
                            map.get(positionY + 1).set(positionX + 1, ']');
                            map.get(positionY).set(positionX, '.');
                            map.get(positionY).set(positionX + 1, '.');
                        }
                        return true;
                    }
                }

                if (map.get(positionY).get(positionX) == ']') {
                    boolean push1 = step(positionY + 1, positionX, map, 'v', pushIt);
                    boolean push2 = step(positionY + 1, positionX - 1, map, 'v', pushIt);
                    if (push1 && push2) {
                        if (pushIt) {
                            map.get(positionY + 1).set(positionX - 1, '[');
                            map.get(positionY + 1).set(positionX, ']');
                            map.get(positionY).set(positionX, '.');
                            map.get(positionY).set(positionX - 1, '.');
                        }
                        return true;
                    }
                }

                if (map.get(positionY).get(positionX) == '@') {
                    boolean push1 = step(positionY + 1, positionX, map, 'v', pushIt);
                    if (push1) {
                        if (pushIt) {
                            map.get(positionY + 1).set(positionX, '@');
                            map.get(positionY).set(positionX, '.');
                        }
                        return true;
                    }
                }

                if (map.get(positionY).get(positionX) == '.') {
                    return true;
                }
                return false;
                //endregion
            case '<':
                //region <
                for (int x = positionX - 1; x >= 0; x--) {
                    if (map.get(positionY).get(x) == '.') {
                        break;
                    }
                    if (map.get(positionY).get(x) == '#') {
                        push = false;
                        break;
                    }
                    size++;
                }

                if (push) {
                    if (size > 0) {
                        for (int x = positionX - 1; x > positionX - size - 1; x -= 2) {
                            map.get(positionY).set(x - 2, '[');
                            map.get(positionY).set(x - 1, ']');
                        }
                    }
                    map.get(positionY).set(positionX, '.');
                    map.get(positionY).set(positionX - 1, '@');
                } else {
                    return false;
                }
                break;
                //endregion
            case '>':
                //region >
                for (int x = positionX + 1; x < map.getFirst().size(); x++) {
                    if (map.get(positionY).get(x) == '.') {
                        break;
                    }
                    if (map.get(positionY).get(x) == '#') {
                        push = false;
                        break;
                    }
                    size++;
                }

                if (push) {
                    if (size > 0) {
                        for (int x = positionX + 1; x < positionX + size + 1; x += 2) {
                            map.get(positionY).set(x + 2, ']');
                            map.get(positionY).set(x + 1, '[');
                        }
                    }
                    map.get(positionY).set(positionX, '.');
                    positionX++;
                    map.get(positionY).set(positionX, '@');
                } else {
                    return false;
                }
                break;
                //endregion
        }
        return true;
    }
}