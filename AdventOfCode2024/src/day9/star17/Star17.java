package day9.star17;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;

public class Star17 {
    public static void main(String[] args) {
        String filePath = "src\\day9\\input.txt";

        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line = br.readLine();
            int newArrayLength = 0;
            for(Character c : line.toCharArray()){
                newArrayLength += Integer.parseInt(c+"");
            }
            int[] memory = new int[newArrayLength];
            for (int i = 0; i<=memory.length-1; i++){
                memory[i] = -1;
            }

            boolean empty = false;
            int process = 0;
            int position = 0;
            for(Character c : line.toCharArray()){
                int length = Integer.parseInt(c+"");
                if(empty){
                    position += length;
                    empty = false;
                } else {
                    for(int i = 0; i<length; i++){
                        memory[position + i] = process;
                    }
                    position += length;
                    process +=1;
                    empty = true;
                }
            }

            int nextFullMemoryPosition=memory.length-1;
            int nextFreeMemoryPosition=0;
            do {
                while (memory[nextFreeMemoryPosition] != -1 && nextFreeMemoryPosition < nextFullMemoryPosition) {
                    nextFreeMemoryPosition++;
                }
                while (memory[nextFullMemoryPosition] == -1 && nextFullMemoryPosition > nextFreeMemoryPosition) {
                    nextFullMemoryPosition--;
                }
                if(nextFreeMemoryPosition < nextFullMemoryPosition){
                    memory[nextFreeMemoryPosition] = memory[nextFullMemoryPosition];
                    memory[nextFullMemoryPosition] = -1;
                    nextFreeMemoryPosition++;
                    nextFullMemoryPosition--;
                }
            } while (nextFreeMemoryPosition < nextFullMemoryPosition);

            BigInteger checkSum = BigInteger.valueOf(0);
            for(int i = 0; i<=memory.length-1; i++){
                if(memory[i] < 0){
                    break;
                }
                checkSum = checkSum.add(BigInteger.valueOf((long) memory[i] *i));
            }

            System.out.println("The checksum is: " + checkSum);

        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
    }
}
