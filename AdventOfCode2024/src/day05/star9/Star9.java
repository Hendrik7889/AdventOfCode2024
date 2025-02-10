package day05.star9;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Star9 {
    public static void main(String[] args) {
        String filePath = "src\\day5\\map.txt";

        List<int[]> productionRules = new ArrayList<>();
        List<List<Integer>> pagesToBeUpdated = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;
            boolean firstSection = true;
            while ((line = br.readLine()) != null) {
                if(firstSection) {
                    //on empty line, we know we are done with the first section
                    if(line.isEmpty()){
                        firstSection = false;
                        continue;
                    }
                    //the first section is of Format <Number>|<Number>
                    int[] innerArray = Arrays.stream(line.split("\\|")).map
                            (Integer::parseInt).mapToInt(i -> i).toArray();
                    productionRules.add(innerArray);
                }else {
                    //the second section is of Format <Number>,<Number>,<Number>,<Number> ...
                    List<Integer> numbers = Arrays.stream(line.split(","))
                            .map(Integer::parseInt)
                            .toList();
                    pagesToBeUpdated.add(numbers);
                }
            }
        } catch (
                IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }

        int SumOfMiddleElementOfCorrectPages = 0;
        for (List<Integer> page : pagesToBeUpdated) {
            if(isCorrectPage(page, productionRules)){
                SumOfMiddleElementOfCorrectPages += page.get(page.size()/2);
            }
        }
        System.out.println("The sum of the middle elements of the correct pages is: " + SumOfMiddleElementOfCorrectPages);

    }

    private static boolean isCorrectPage(List<Integer> page, List<int[]> productionRules) {
        boolean isCorrect = true;
        for (int[] rule : productionRules) {
            if(page.contains(rule[0]) && page.contains(rule[1])){
                boolean firstElementFound = false;
                for(int i = 0; i<= page.size()-1; i++){
                    if(page.get(i) == rule[0]){
                        firstElementFound = true;
                    }else if(page.get(i) == rule[1] && !firstElementFound){
                        isCorrect = false;
                        break;
                    }
                }
            }
        }
        return isCorrect;
    }
}
