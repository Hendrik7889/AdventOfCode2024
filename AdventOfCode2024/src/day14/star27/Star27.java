package day14.star27;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Star27 {

    public static void main(String[] args) {
        String filePath = "src\\day14\\input.txt";

        int mapX = 101;
        int mapY = 103;
        int steps = 100;

        int[][] grid = new int[mapX][mapY];
        clearGrid(grid);

        ArrayList<Robot> robots = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = br.readLine()) != null) {
                // Split the line by space
                //p=66,90 v=-50,37
                Pattern pattern = Pattern.compile("p=(-?\\d+),(-?\\d+)\\s+v=(-?\\d+),(-?\\d+)", Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    Robot robot = new Robot(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4)));
                    robots.add(robot);
                    System.out.println("Robot added");
                } else {
                    System.out.println("No buttonA found");
                }
            }

        } catch (
                IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }

        //make the steps
        for (int i = 0; i < steps; i++) {
            for (Robot robot : robots) {
                robot.x = (robot.x + robot.xv);
                while (robot.x < 0) {
                    robot.x = mapX + robot.x;
                }
                robot.x = robot.x % mapX;

                robot.y = (robot.y + robot.yv);
                while (robot.y < 0) {
                    robot.y = mapY + robot.y;
                }
                robot.y = robot.y % mapY;
            }
        }

        //fill the grid
        for (Robot robot : robots) {
            grid[robot.x][robot.y] += 1;
        }

        int result = countQuadrants(grid);

        System.out.println("Result: " + result);
    }


    /**
     * Count the quadrants ignore the middle if it exists
     * @param grid the grid to count the quadrants from
     * @return the result
     */
    private static int countQuadrants(int[][] grid) {
        int countTopLeft = 0;
        int countTopRight = 0;
        int countBottomLeft = 0;
        int countBottomRight = 0;

        for (int k = 0; k < grid[0].length / 2; k++) {
            for (int j = 0; j < grid.length - (grid.length / 2) -1 ; j++) {
                countTopLeft+=grid[j][k];
            }
        }

        for (int k = 0; k < grid[0].length / 2; k++) {
            for (int j = grid.length - grid.length/2; j < grid.length; j++) {
                countTopRight+=grid[j][k];
            }
        }

        for (int k = grid[0].length / 2 +1; k < grid[0].length; k++) {
            for (int j = 0; j < grid.length - (grid.length / 2) - 1; j++) {
                countBottomLeft += grid[j][k];
            }
        }
        for (int k = grid[0].length / 2 +1; k < grid[0].length; k++) {
            for (int j = grid.length - grid.length/2; j < grid.length; j++) {
                countBottomRight+=grid[j][k];
            }
        }

        return countTopLeft * countTopRight * countBottomLeft * countBottomRight;
    }


    /**
     * Clear the grid
     * @param grid the grid to clear
     */
    private static void clearGrid(int[][] grid) {
        for (int[] lines : grid) {
            Arrays.fill(lines, 0);
        }
    }


    /**
     * dataclass for the robot
     */
    private static class Robot {
        public Robot(int x, int y, int xv, int yv) {
            this.x = x;
            this.y = y;
            this.xv = xv;
            this.yv = yv;
        }
        int x;
        int y;
        int xv;
        int yv;
    }
}