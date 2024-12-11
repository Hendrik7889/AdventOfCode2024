package day4.star7;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Star7 {
    public static void main(String[] args) {
        List<List<Character>> mainList = new ArrayList<>();
        String filePath = "src/day4/map.txt";
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

        int found = 0;
        for(int i =0; i<=mainList.size()-1; i++){
            for(int j = 0; j<=mainList.get(i).size()-1; j++){
                //check for linear forward XMAX:
                if(j+3<=mainList.get(i).size()-1){
                    if(mainList.get(i).get(j) == 'X' && mainList.get(i).get(j+1) == 'M' && mainList.get(i).get(j+2) == 'A' && mainList.get(i).get(j+3) == 'S'){
                        found += 1;
                    }
                }
                //check for linear backwards XMAS:
                if(j-3>=0){
                    if(mainList.get(i).get(j) == 'X' && mainList.get(i).get(j-1) == 'M' && mainList.get(i).get(j-2) == 'A' && mainList.get(i).get(j-3) == 'S'){
                        found += 1;
                    }
                }
                //check for vertical forward XMAS:
                if(i+3<=mainList.size()-1){
                    if(mainList.get(i).get(j) == 'X' && mainList.get(i+1).get(j) == 'M' && mainList.get(i+2).get(j) == 'A' && mainList.get(i+3).get(j) == 'S'){
                        found += 1;
                    }
                }
                //check for vertical backwards XMAS:
                if(i-3>=0){
                    if(mainList.get(i).get(j) == 'X' && mainList.get(i-1).get(j) == 'M' && mainList.get(i-2).get(j) == 'A' && mainList.get(i-3).get(j) == 'S'  ){
                        found += 1;
                    }
                }
                //check for diagonal upwards right XMAS:
                if(i-3>=0 && j+3<=mainList.get(i).size()-1){
                    if(mainList.get(i).get(j) == 'X' && mainList.get(i-1).get(j+1) == 'M' && mainList.get(i-2).get(j+2) == 'A' && mainList.get(i-3).get(j+3) == 'S'){
                        found += 1;
                    }
                }
                //check for diagonal upwards left XMAS:
                if(i-3>=0 && j-3>=0){
                    if(mainList.get(i).get(j) == 'X' && mainList.get(i-1).get(j-1) == 'M' && mainList.get(i-2).get(j-2) == 'A' && mainList.get(i-3).get(j-3) == 'S'){
                        found += 1;
                    }
                }
                //check for diagonal downwards right XMAS:
                if(i+3<=mainList.size()-1 && j+3<=mainList.get(i).size()-1){
                    if(mainList.get(i).get(j) == 'X' && mainList.get(i+1).get(j+1) == 'M' && mainList.get(i+2).get(j+2) == 'A' && mainList.get(i+3).get(j+3) == 'S'){
                        found += 1;
                    }
                }
                //check for diagonal downwards left XMAS:
                if(i+3<=mainList.size()-1 && j-3>=0){
                    if(mainList.get(i).get(j) == 'X' && mainList.get(i+1).get(j-1) == 'M' && mainList.get(i+2).get(j-2) == 'A' && mainList.get(i+3).get(j-3) == 'S'){
                        found += 1;
                    }
                }

            }
        }
        System.out.println("The word XMAS was found " + found + " times.");
    }
}
