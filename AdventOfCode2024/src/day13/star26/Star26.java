package day13.star26;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Star26 {
        public static void main(String[] args) {

            String filePath = "src\\day13\\input.txt";

            ArrayList<long[]> buttonA = new ArrayList<>();
            ArrayList<long[]> buttonB = new ArrayList<>();
            ArrayList<long[]> prize = new ArrayList<>();

            try {
                BufferedReader br = new BufferedReader(new FileReader(filePath));
                String line;
                while ((line = br.readLine()) != null) {
                    // Split the line by space
                    Pattern pattern = Pattern.compile("Button\\s(\\w):\\sX\\+(-?\\d+),\\sY\\+(-?\\d+)", Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        long[] a = new long[]{Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3))};
                        buttonA.add(a);
                    } else {
                        System.out.println("No buttonA found");
                    }

                    line = br.readLine();
                    matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        long[] b = new long[]{Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3))};
                        buttonB.add(b);
                    } else {
                        System.out.println("No button B found");
                    }

                    line = br.readLine();
                    pattern = Pattern.compile("Prize:\\sX=(-?\\d+),\\sY=(-?\\d+)", Pattern.CASE_INSENSITIVE);
                    matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        long[] p = new long[]{Integer.parseInt(matcher.group(1))+ 10000000000000L, Integer.parseInt(matcher.group(2))+ 10000000000000L};
                        prize.add(p);
                    } else {
                        System.out.println("No prize found");
                    }
                    br.readLine();
                }

            } catch (
                    IOException e) {
                System.out.println("Error reading the file: " + e.getMessage());
            }
            long result = 0;
            for(int i = 0; i < buttonA.size(); i++) {
                long numerator =  prize.get(i)[0] * buttonA.get(i)[1] - prize.get(i)[1] * buttonA.get(i)[0];
                long b = numerator / (buttonB.get(i)[0] * buttonA.get(i)[1] - buttonB.get(i)[1] * buttonA.get(i)[0]);
                long remX = prize.get(i)[0] - b * buttonB.get(i)[0];
                long l = buttonA.get(i)[0] == 0 ? prize.get(i)[1] : remX;
                long r = buttonA.get(i)[0] == 0 ? buttonA.get(i)[1] : buttonA.get(i)[0];
                long a = l / r;
                result +=  (a * buttonA.get(i)[1] + b * buttonB.get(i)[1] == prize.get(i)[1] && l % r == 0) ? 3 * a + b : 0;
            }
            System.out.println(result);
        }

}
