package day11.star22;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

public class Star22 {
    public static void main(String[] args) {
        String filePath = "src/day11/input.txt";

        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line = br.readLine();
            ArrayList<Stone> numbers = new ArrayList<>();

            // Parse the input line into a list of BigInteger values
            for(String numStr : line.split(" ")) {
                Stone stone = new Stone();
                stone.number = new BigInteger(numStr);
                stone.count = 1;
                numbers.add(stone);
            }

            // Iterate 75 times
            long total = 0;
            int numberOfIterations = 75;
            for (int n = 0; n < numberOfIterations; n++) {
                for (int i = 0; i < numbers.size(); i++) {
                    if (numbers.get(i).number.equals(BigInteger.ZERO)) {
                        numbers.get(i).number = BigInteger.ONE;
                    } else {
                        String numStr = numbers.get(i).number.toString();
                        if (numStr.length() % 2 == 0) {
                            // Split the number if its length is even
                            String leftPart = numStr.substring(0, numStr.length() / 2);
                            String rightPart = numStr.substring(numStr.length() / 2);
                            Stone newStoneLeft = new Stone();
                            newStoneLeft.number = new BigInteger(leftPart);
                            newStoneLeft.count = numbers.get(i).count;
                            Stone newStoneRight = new Stone();
                            newStoneRight.number = new BigInteger(rightPart);
                            newStoneRight.count = numbers.get(i).count;
                            numbers.set(i, newStoneLeft);
                            numbers.add(i + 1, newStoneRight);
                            i++; // Skip the newly added number
                        } else {
                            // Multiply by 2024 if its length is odd
                            numbers.get(i).number =  numbers.get(i).number.multiply(BigInteger.valueOf(2024));
                        }
                    }
                }

                //count equal numbers
                for (int i = 0; i < numbers.size(); i++) {
                    for (int j = i + 1; j < numbers.size(); j++) {
                        if (numbers.get(i).number.equals(numbers.get(j).number)) {
                            numbers.get(i).count = numbers.get(i).count + numbers.get(j).count;
                            numbers.remove(j);
                            j--;
                        }
                    }
                }
            }

            for (Stone number : numbers) {
                total += number.count;
            }

            System.out.println("The number of elements in the list is: " + total);

        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
    }
}

class Stone {
    public BigInteger number;
    public long count;
}