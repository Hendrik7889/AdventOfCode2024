package day3.star5;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Star5 {
    public static void main(String[] args) {
        String filePath = "src/day3/input.txt";
        int multiplications = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = br.readLine()) != null) {
                // Split the line by space
                Pattern pattern = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)", Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(line);
                while (matcher.find()) {
                    multiplications += Integer.parseInt(matcher.group(1))*Integer.parseInt((matcher.group(2)));
                }
            }
        } catch (
        IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
        System.out.println("The result of the multiplications is: " + multiplications);
    }
}
