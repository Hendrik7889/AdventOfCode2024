package day25.star49;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Star59 {
    public static void main(String[] args) throws InterruptedException {
        String filePath = "src/day25/input.txt";
        List<int[]> locks = new ArrayList<>();
        List<int[]> keys = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null && !line.isEmpty()) {
                char[][] matrix = new char[7][5];
                for (int i = 0; i < 7; i++) {
                    matrix[i] = line.toCharArray();
                    line = br.readLine();
                }
                convertToHeights(matrix,locks,keys);

                // empty line
            }

        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }

        int countMatches = 0;
        for(int[] lock : locks) {
            for(int[] key : keys) {
                if (isMatch(lock, key)) {
                    countMatches++;
                }
            }
        }
        System.out.println("No match found " + countMatches);
    }

    private static boolean isMatch(int[] lock, int[] key) {
        for (int i = 0; i < 5; i++) {
            if (lock[i] + key[i] >= 6) {
                return false;
            }
        }
        return true;
    }

    private static void convertToHeights(char[][] matrix, List<int[]> locks, List<int[]> keys) {
        int[] heights = new int[5];
        for (int col = 0; col < 5; col++) {
            int height = -1;
            for (int row = 0; row < 7; row++) {
                if (matrix[row][col] == '#') {
                    height++;
                }
            }
            heights[col] = height;
        }
        if(matrix[0][0] == '#' && matrix[0][1] == '#' && matrix[0][2] == '#' && matrix[0][3] == '#' && matrix[0][4] == '#') {
            locks.add(heights);
        }else if (matrix[6][0] == '#' && matrix[6][1] == '#' && matrix[6][2] == '#' && matrix[6][3] == '#' && matrix[6][4] == '#') {
            keys.add(heights);
        }else {
            System.out.println("Invalid input");
        }
    }
}