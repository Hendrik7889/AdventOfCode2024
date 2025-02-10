package day07.star14;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Star14{
    public static void main(String[] args) {
        String filePath = "src\\day7\\map.txt";

        ArrayList<Long> results = new ArrayList<>();
        ArrayList<ArrayList<Long>> numbers = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = br.readLine()) != null) {
                results.add(Long.valueOf(line.split(": ")[0]));
                numbers.add(Arrays.stream(line.split(": ")[1].split(" "))
                        .map(Long::parseLong)
                        .collect(Collectors.toCollection(ArrayList::new)));

            }

        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }

        long sumOfTestValues = getSumOfTestValues(results, numbers);
        System.out.println("The sum of the test values is: " + sumOfTestValues);
    }

    private static long getSumOfTestValues(ArrayList<Long> results, ArrayList<ArrayList<Long>> numbers) {
        long sumOfTestValues = 0;
        // For each line in the input file
        for (int i = 0; i < results.size(); i++) {
            boolean found = false;
            int n = numbers.get(i).size();
            int maxCombinations = (int) Math.pow(3, n - 1); // 3 choices (+, *, ||) for n-1 gaps
            // For each combination of operations
            for (int j = 0; j < maxCombinations; j++) {
                long sum = numbers.get(i).get(0);
                int currentCombination = j;

                for (int k = 1; k < n; k++) {
                    int operation = currentCombination % 3; // 0: +, 1: *, 2: ||
                    currentCombination /= 3;

                    if (operation == 0) {
                        sum += numbers.get(i).get(k);
                    } else if (operation == 1) {
                        sum *= numbers.get(i).get(k);
                    } else { // Concatenation
                        String concatenated = String.valueOf(sum) + numbers.get(i).get(k);
                        sum = Long.parseLong(concatenated);
                    }
                }

                if (sum == results.get(i)) {
                    sumOfTestValues += results.get(i);
                    break;
                }
            }
        }
        return sumOfTestValues;
    }
}
