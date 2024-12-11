package day5.star10;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Star10 {
    public static void main(String[] args) {
        String filePath = "src/day5/map.txt";
        ArrayList<int[]> productionRules = new ArrayList<>();
        ArrayList<ArrayList<Integer>> pagesToBeUpdated = new ArrayList<>();

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
                    ArrayList<Integer> numbers = Arrays.stream(line.split(","))
                            .map(Integer::parseInt)
                            .collect(Collectors.toCollection(ArrayList::new));
                    pagesToBeUpdated.add(numbers);
                }
            }
        } catch (
                IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }

        int SumOfMiddleElementOfCorrectedPages = 0;
        for (List<Integer> page : pagesToBeUpdated) {
            if(isNotCorrectPage(page, productionRules)){
                // Keep correcting the page until it is correct
                while (isNotCorrectPage(page, productionRules)){
                }
                SumOfMiddleElementOfCorrectedPages += page.get(page.size()/2);
            }
        }
        System.out.println("The sum of the middle elements of the corrected pages is: " + SumOfMiddleElementOfCorrectedPages);

    }

    private static boolean isNotCorrectPage(List<Integer> page, List<int[]> productionRules) {
        boolean isCorrect = true;
        for (int[] rule : productionRules) {
            int wrongOrderPosition = -1;
            if(page.contains(rule[0]) && page.contains(rule[1])){
                boolean firstElementFound = false;
                for(int i = 0; i<= page.size()-1; i++){
                    if(page.get(i) == rule[0]){
                        firstElementFound = true;
                        if(wrongOrderPosition != -1){
                            // Swap elements
                            int hSwap = page.get(wrongOrderPosition);
                            page.set(wrongOrderPosition, page.get(i));
                            page.set(i, hSwap);
                            isCorrect = false;
                        }
                    }else if(page.get(i) == rule[1] && !firstElementFound){
                        wrongOrderPosition = i;
                    }
                }
            }
        }
        return !isCorrect;
    }
}
