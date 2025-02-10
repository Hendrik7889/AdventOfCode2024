package day22.star43;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Star43 {
    public static void main(String[] args) {
        ArrayList<Long> codes = new ArrayList<>();
        String filePath = "src/day22/input.txt";


        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null && !line.isEmpty()) {
                codes.add(Long.valueOf((line)));
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }

        long result = 0;
        for (long l : codes) {
            for (int i = 0; i <= 1999; i++) {
                l = calculateNextSecretNumber(l);
            }
            result += l;
        }
        System.out.println("The result is: " + result);
    }

    /**
     * Return the XOR of the two values
     * @return the XOR of the two values
     */
    public static long mix (long pValue1,long pValue2){
        return pValue1 ^ pValue2;
    }

    /**
     * Return the value modulo 16777216
     * @return the value modulo 16777216
     */
    public static long prune(long pValue){
        return pValue % 16777216;
    }

    /**
     * Calculate the next secret number
     * @return the next secret number
     */
    public static long calculateNextSecretNumber(long pSecretNumber){
        //1
        pSecretNumber = mix(pSecretNumber * 64, pSecretNumber);
        pSecretNumber = prune(pSecretNumber);
        //2
        pSecretNumber = mix(pSecretNumber, pSecretNumber / 32);
        pSecretNumber = prune(pSecretNumber);
        //3
        pSecretNumber = mix(pSecretNumber * 2048, pSecretNumber);
        pSecretNumber = prune(pSecretNumber);
        return pSecretNumber;
    }
}
