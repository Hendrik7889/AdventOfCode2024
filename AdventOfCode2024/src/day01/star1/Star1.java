package day01.star1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.sort;

public class Star1 {
    public static void main(String[] args) {
        String filePath = "src\\day1\\table.txt";

        List<Integer> leftList = new ArrayList<>();
        List<Integer> rightList = new ArrayList<>();

        try{
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = br.readLine()) != null) {
                // Split the line by space
                String[] parts = line.split("   ");
                if (parts.length == 2) { // Ensure the line has exactly two numbers.txt
                    // Parse the numbers.txt and add to the lists
                    leftList.add(Integer.parseInt(parts[0]));
                    rightList.add(Integer.parseInt(parts[1]));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }

        sort(leftList);
        sort(rightList);
        int absoluteDifference = 0;
        for(int i =0; i<=leftList.size()-1; i++){
            absoluteDifference += Math.abs(leftList.get(i) - rightList.get(i));
        }
        System.out.println("The sum of the absolute differences is: " + absoluteDifference);
    }
}
