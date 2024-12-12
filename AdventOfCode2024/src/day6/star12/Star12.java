package day6.star12;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Star12 {
    public static void main(String[] args) {
        String filePath = "src\\day6\\map.txt";

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

        //simulate once to check if it is infinite already
        //if not, use it to check all possible fields if they can have an impact to construct
        //an infinite loop
        ArrayList<ArrayList<Character>> copy = new ArrayList<>();
        for (ArrayList<Character> innerList : mainList) {
            copy.add(new ArrayList<>(innerList));
        }
        boolean isInfinite = simulate(copy);
        if(isInfinite){
            System.out.println("The system is already infinite.");
        }
        if(!isInfinite){
            int count = 0;
            for (int i = 0; i < copy.size(); i++) {
                for (int j = 0; j < copy.get(i).size(); j++) {
                    // around the element are elements that are not '.' or '#' as we only set one additional element to '#'
                    if(i-1>=0                           && copy.get(i-1).get(j) != '.' && copy.get(i-1).get(j) != '#' ||
                       i+1<=copy.size()-1               && copy.get(i+1).get(j) != '.' && copy.get(i+1).get(j) != '#' ||
                       j-1>=0                           && copy.get(i).get(j-1) != '.' && copy.get(i).get(j-1) != '#' ||
                       j+1<=copy.get(i).size()-1        && copy.get(i).get(j+1) != '.' && copy.get(i).get(j+1) != '#' ||
                       copy.get(i).get(j) != '.' && copy.get(i).get(j) != '#' )
                    {
                        ArrayList<ArrayList<Character>> copyInfinite = new ArrayList<>();
                        for (ArrayList<Character> innerList : mainList) {
                            copyInfinite.add(new ArrayList<>(innerList));
                        }
                        copyInfinite.get(i).set(j, '#');
                        boolean isInfiniteCopy = simulate(copyInfinite);
                        if(isInfiniteCopy){
                            count += 1;
                        }
                    }
                }
            }
            System.out.println("Number of fields: " + count);
        }
    }

    private static boolean simulate(ArrayList<ArrayList<Character>> mainList) {
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
        mainList.get(playerY).set(playerX, '|');
        while (playerX <= mainList.getFirst().size()-1 && playerY <= mainList.size()-1 && playerX>=0 && playerY>=0) {
            char currentField = mainList.get(playerY).get(playerX);
            switch (currentDirection) {
                case '^':
                    if(mainList.get(playerY).get(playerX) == '-') {
                        mainList.get(playerY).set(playerX, '+');
                    }else if(mainList.get(playerY).get(playerX) == '|'){
                        mainList.get(playerY).set(playerX, '|');
                    } else if (mainList.get(playerY).get(playerX) == '+') {
                        mainList.get(playerY).set(playerX, '+');
                    } else if (mainList.get(playerY).get(playerX) == '.') {
                        mainList.get(playerY).set(playerX, '|');
                    }
                    if (playerY - 1 < 0) {
                        return false;
                    }
                    if (mainList.get(playerY - 1).get(playerX) == '#') {
                        if(currentField == '+'){
                            return true;
                        }
                        currentDirection = '>';
                    } else {
                        playerY -= 1;
                    }
                    break;
                case '>':
                    if(mainList.get(playerY).get(playerX) == '|') {
                        mainList.get(playerY).set(playerX, '+');
                    }else if(mainList.get(playerY).get(playerX) == '-'){
                        mainList.get(playerY).set(playerX, '-');
                    } else if (mainList.get(playerY).get(playerX) == '+') {
                        mainList.get(playerY).set(playerX, '+');
                    } else if (mainList.get(playerY).get(playerX) == '.') {
                        mainList.get(playerY).set(playerX, '-');
                    }
                    if(playerX + 1 > mainList.get(playerY).size()-1) {
                        return false;
                    }
                    if (mainList.get(playerY).get(playerX + 1) == '#') {
                        if(currentField == '+'){
                            return true;
                        }
                        currentDirection = 'v';
                    } else {
                        playerX += 1;
                    }
                    break;
                case 'v':
                    if(mainList.get(playerY).get(playerX) == '-') {
                        mainList.get(playerY).set(playerX, '+');
                    }else if(mainList.get(playerY).get(playerX) == '|'){
                        mainList.get(playerY).set(playerX, '|');
                    } else if (mainList.get(playerY).get(playerX) == '+') {
                        mainList.get(playerY).set(playerX, '+');
                    } else if (mainList.get(playerY).get(playerX) == '.') {
                        mainList.get(playerY).set(playerX, '|');
                    }
                    if (playerY + 1 > mainList.size()-1) {
                        return false;
                    }
                    if (mainList.get(playerY + 1).get(playerX) == '#') {
                        if(currentField == '+'){
                            return true;
                        }
                        currentDirection = '<';
                    } else {
                        playerY += 1;
                    }
                    break;
                case '<':
                    if(mainList.get(playerY).get(playerX) == '|') {
                        mainList.get(playerY).set(playerX, '+');
                    }else if(mainList.get(playerY).get(playerX) == '-'){
                        mainList.get(playerY).set(playerX, '-');
                    } else if (mainList.get(playerY).get(playerX) == '+') {
                        mainList.get(playerY).set(playerX, '+');
                    } else if (mainList.get(playerY).get(playerX) == '.') {
                        mainList.get(playerY).set(playerX, '-');
                    }
                    if(playerX - 1 < 0) {
                        return false;
                    }
                    if (mainList.get(playerY).get(playerX - 1) == '#') {
                        if(currentField == '+'){
                            return true;
                        }
                        currentDirection = '^';
                    } else {
                        playerX -= 1;
                    }
                    break;
            }

        }
        return false;
    }
}
