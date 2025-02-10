package day04.star8;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Star8 {
    public static void main(String[] args) {
        String filePath = "src\\day4\\map.txt";

        List<List<Character>> mainList = new ArrayList<>();

        try{
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = br.readLine()) != null) {
                ArrayList<Character> charArray = new ArrayList<>();
                for (int i = 0; i < line.length(); i++) {
                    charArray.add(i, line.charAt(i));
                }
                mainList.add(charArray);
            }
        } catch (
                IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }

        int found = getFound(mainList);
        System.out.println("The MAS in X-shape was found " + found + " times.");
    }

    private static int getFound(List<List<Character>> mainList) {
        int found = 0;
        //iterate over matrix find x with A at position i,j
        for(int i = 0; i<= mainList.size()-1; i++){
            for(int j = 0; j<= mainList.get(i).size()-1; j++){
                //check for possible positions M and S
                if(i-1>=0 && j-1 >= 0 && i+1<= mainList.size()-1 && j+1<= mainList.get(i).size()-1){
                    //check for A in the middle
                    if(mainList.get(i).get(j)=='A'){
                        //check for down diagonal seen from the right side
                        if(mainList.get(i-1).get(j-1) == 'M' &&  mainList.get(i+1).get(j+1) == 'S' || mainList.get(i-1).get(j-1) == 'S' &&  mainList.get(i+1).get(j+1) == 'M'){
                            //check for up diagonal seen from the right side
                            if(mainList.get(i-1).get(j+1) == 'M' &&  mainList.get(i+1).get(j-1) == 'S' || mainList.get(i-1).get(j+1) == 'S' &&  mainList.get(i+1).get(j-1) == 'M'){
                                found += 1;
                            }
                        }
                    }
                }
            }
        }
        return found;
    }
}
