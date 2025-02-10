package day09.star18;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;

public class Star18 {
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

            //build up the array
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

            //the smallest position of the element
            int nextFullMemoryPosition=memory.length-1;
            //the size of the element strictly >= 1
            int size = 0;
            do {
                size = 0;
                for (int i = nextFullMemoryPosition; i > 0; i--) {
                    if (memory[i] == -1) {
                        nextFullMemoryPosition = i;
                    } else {
                        size = 1;
                        while (i-1 >= 0 && memory[i] == memory[i - 1] ) {
                            i--;
                            size++;
                        }
                        nextFullMemoryPosition = i;

                        break;
                    }
                }

                //find the next free memory position for the size of the element
                if (nextFullMemoryPosition > 0) {
                    boolean found = false;
                    for (int i = 0; i <= nextFullMemoryPosition - size; i++) {
                        for (int j = 0; j < size; j++) {
                            if (memory[i + j] != -1) {
                                break;
                            }
                            if (j == size - 1) {
                                found = true;
                                break;
                            }
                        }
                        if (found) {
                            for (int j = 0; j < size; j++) {
                                int temp = memory[i + j];
                                int newTemp = memory[nextFullMemoryPosition  + j];
                                memory[i + j] = memory[nextFullMemoryPosition + j];
                                memory[nextFullMemoryPosition  + j] = -1;
                            }
                            break;
                        }
                    }
                    if(!found) {
                        nextFullMemoryPosition = nextFullMemoryPosition-1;
                    }
                }
            }while (nextFullMemoryPosition > size);


            BigInteger checkSum = BigInteger.valueOf(0);
            for(int i = 0; i<=memory.length-1; i++){
                if(memory[i] < 0){
                    continue;
                }
                checkSum = checkSum.add(BigInteger.valueOf((long) memory[i] *i));
            }

            System.out.println("The checksum is: " + checkSum);

        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
    }
}