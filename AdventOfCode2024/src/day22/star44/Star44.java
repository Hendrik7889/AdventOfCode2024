package day22.star44;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Star44 {
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

        //region calculate all contained sequences and all Codes
        ArrayList<Sequence> allSequences = new ArrayList<>();
        ArrayList<Data[]> allData = new ArrayList<>();
        for (long l : codes) {
            System.out.println("Calculating sequences for code: " + l);
            Data[] data = new Data[2000];
            short shortValue;
            long calculatedCode = l;
            for (int i = 0; i <= 1999; i++) {
                calculatedCode = calculateNextSecretNumber(calculatedCode);
                shortValue = (short) (calculatedCode % 10);
                Data d = new Data();
                d.value = shortValue;
                if (i > 0) {
                    d.sequence.sequence1 = (short) (d.value - data[i - 1].value );
                    d.sequence.sequence2 = data[i - 1].sequence.sequence1;
                    d.sequence.sequence3 = data[i - 1].sequence.sequence2;
                    d.sequence.sequence4 = data[i - 1].sequence.sequence3;
                }
                data[i] = d;
                boolean contains = false;
                //replace the sequence with those in the list if it is already there
                if(i>3) {
                    for (Sequence s : allSequences) {
                        if (s.equals(d.sequence)) {
                            d.sequence = s;
                            contains = true;
                            break;
                        }
                    }
                    if (!contains) {
                        allSequences.add(d.sequence);
                    }
                }
            }
            allData.add(data);
        }
        //endregion
        int result = 0;
        Sequence bestSequence = null;
        for(Sequence s: allSequences) {
            System.out.print("Checking sequence: " + s.sequence1 + "," + s.sequence2 + "," + s.sequence3 + "," + s.sequence4);

            long localResult = 0;
            codes:
            for (Data[] d: allData) {
                for (int i = 3; i <= 1999; i++) {
                    if (s.equals(d[i].sequence)) {
                        localResult += d[i].value;
                        continue codes;
                    }
                }
            }
            System.out.println(" -> " + localResult +" highest: " + result);
            if(localResult > result){
                result = (int) localResult;
                bestSequence = s;
            }
        }
        assert bestSequence != null;
        System.out.println();
        System.out.println("The result is: " + result + " with sequence: " + bestSequence.sequence1 + " " + bestSequence.sequence2 + " " + bestSequence.sequence3 + " " + bestSequence.sequence4);
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

class Data{
    short value;
    Sequence sequence;

    public Data(){
        sequence = new Sequence();
    }
}

class Sequence{
    int frequency;
    short sequence1;
    short sequence2;
    short sequence3;
    short sequence4;

    public boolean equals(Sequence sequence){
        return this.sequence1 == sequence.sequence1 && this.sequence2 == sequence.sequence2 && this.sequence3 == sequence.sequence3 && this.sequence4 == sequence.sequence4;
    }
}
