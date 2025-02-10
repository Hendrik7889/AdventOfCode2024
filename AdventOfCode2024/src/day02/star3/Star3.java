package day02.star3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Star3 {
    public static void main(String[] args) {
        String filePath = "src\\day2\\ListOfLists.txt";

        List<List<Integer>> mainList = new ArrayList<>();

        try{
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = br.readLine()) != null) {
                // Split the line by space
                List<Integer> parts = Arrays.stream(line.split(" ")).map(Integer::parseInt).toList();
                mainList.add(parts);
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
        int save = getSave(mainList);
        System.out.println("The number of lists that are save is: " + save);
    }

    private static int getSave(List<List<Integer>> mainList) {
        int save = 0;
        for(List<Integer> list : mainList){
            int isBigger = 0;
            int isSmaller = 0;
            for(int i = 0; i<=list.size()-2; i++){
                // Check if the difference between the two numbers between 1 and 3
                if(Math.abs(list.get(i) - list.get(i+1)) <= 3 && Math.abs(list.get(i) - list.get(i+1)) >= 1) {
                    if (list.get(i) > list.get(i + 1)) {
                        isBigger += 1;
                    }
                    if (list.get(i) < list.get(i + 1)) {
                        isSmaller += 1;
                    }
                }
            }
            if(isBigger == list.size()-1 || isSmaller == list.size()-1){
                save += 1;
            }
        }
        return save;
    }


}
