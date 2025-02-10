package day07.star13;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Star13 {
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

        for (int i = 0; i < results.size(); i++) {
            int maxCombinations = (int) Math.pow(2, numbers.get(i).size()-1);

            for (int j = 0; j < maxCombinations; j++) {
                long sum = numbers.get(i).get(0); // Start with the first number
                for (int k = 1; k < numbers.get(i).size(); k++) {
                    if (getBit(j, k-1)) { // If bit k is set, add
                        sum += numbers.get(i).get(k);
                    } else { // If bit k is not set, multiply
                        sum *= numbers.get(i).get(k);
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

    public static boolean getBit(int number, int position) {
        // Shift the bit at the given position to the least significant bit position
        return ((number >> position) & 1) == 1;
    }
}
