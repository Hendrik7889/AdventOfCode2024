package day11.star21;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

public class Star21 {
    public static void main(String[] args) {
        String filePath = "src\\day11\\input.txt";

        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line = br.readLine();
            ArrayList<BigInteger> numbers = new ArrayList<>();

            // Parse the input line into a list of BigInteger values
            Arrays.stream(line.split(" "))
                    .map(BigInteger::new)
                    .forEach(numbers::add);

            // Iterate 25 times
            int numberOfIterations = 25;
            for (int n = 0; n < numberOfIterations; n++) {
                for (int i = 0; i < numbers.size(); i++) {
                    if (numbers.get(i).equals(BigInteger.ZERO)) {
                        numbers.set(i, BigInteger.ONE);
                    } else {
                        String numStr = numbers.get(i).toString();
                        if (numStr.length() % 2 == 0) {
                            // Split the number if its length is even
                            String leftPart = numStr.substring(0, numStr.length() / 2);
                            String rightPart = numStr.substring(numStr.length() / 2);
                            numbers.set(i, new BigInteger(leftPart));
                            numbers.add(i + 1, new BigInteger(rightPart));
                            i++; // Skip the newly added number
                        } else {
                            // Multiply by 2024 if its length is odd
                            numbers.set(i, numbers.get(i).multiply(BigInteger.valueOf(2024)));
                        }
                    }
                }
            }
            System.out.println("The number of elements in the list is: " + numbers.size());

        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
    }
}
