package day14.star28;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Star28 {

    public static void main(String[] args) {
        String filePath = "src\\day14\\input.txt";

        int mapX = 101;
        int mapY = 103;

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


        boolean notFound = true;
        int steps = 0;
        while (notFound) {
            steps++;
            //region make a step
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
            //endregion
            //region fill the grid
            clearGrid(grid);
            for (Robot robot : robots) {
                grid[robot.x][robot.y] += 1;
            }
            //endregion
            //region check if there is a spot with a value bigger then 1
            boolean foundBiggerThenOne = false;
            boolean foundRowOfTen = false;
            for (int i = 0; i < mapX - 1; i++) {
                for (int j = 0; j < mapY - 1; j++) {
                    if (grid[i][j] >= 2) {
                        foundBiggerThenOne = true;
                        break;
                    }
                }
                if (foundBiggerThenOne) {
                    break;
                }
            }
            //endregion
            //region check for a row of 10
            for (int i = 0; i < mapY - 1; i++) {
                int count = 0;
                for (int j = 0; j < mapX - 1; j++) {
                    if (grid[j][i] >= 1) {
                        count++;
                    }
                }
                if (count >= 10) {
                    foundRowOfTen = true;
                    break;
                }
            }
            //endregion
            if (!foundBiggerThenOne && foundRowOfTen) {
                notFound = false;
            }
        }

        //print grid to see the result
        for (int i = 0; i < mapY; i++) {
            for (int j = 0; j < mapX; j++) {
                System.out.print(grid[j][i]);
            }
            System.out.println();
        }

        System.out.println("Result: " + steps);
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