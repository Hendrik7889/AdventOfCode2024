package day20.star39;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Star39 {
    public static void main(String[] args) {
        String filePath = "src\\day20\\input.txt";

        int stepsSkippable=2;
        int minimumSavedTime=100;
        int[][] map = null;
        int startHeight = 0;
        int startLength = 0;
        int endHeight = 0;
        int endLength = 0;

        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;
            //figure out map
            int height = 0;
            int length = 0;
            while ((line = br.readLine()) != null && !line.isEmpty()) {
                if (line.length() > length) {
                    length = line.length();
                }
                height++;

            }
            map = new int[height][length];
            br = new BufferedReader(new BufferedReader(new FileReader(filePath)));
            height = 0;
            while ((line = br.readLine()) != null && !line.isEmpty()) {
                for(int i =0; i<=line.length()-1; i++){
                    switch(line.charAt(i)){
                        case 'S':
                            startLength=i;
                            startHeight=height;
                            map[height][i]=0;
                            break;
                        case 'E':
                            endLength=i;
                            endHeight=height;
                            map[height][i]=Integer.MAX_VALUE;
                            break;
                        case '.':
                            map[height][i]=Integer.MAX_VALUE;

                            break;
                        case'#':
                            map[height][i]=Integer.MIN_VALUE;
                            break;
                        default:
                            System.out.println("Unsupported input character");
                    }
                }
                height++;
            }

        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }

        ArrayList<int[]> path = solve(startHeight, startLength,endHeight,endLength, map);
        int[] cheatedTime = cheat(stepsSkippable, map, path);
        int result = 0;
        for(int i =minimumSavedTime; i<= path.size()-1; i++){
            result+=cheatedTime[i];
        }
        System.out.println("Result: " + result);

    }

    private static ArrayList<int[]> solve(int startHeight, int startLength, int endHeight, int endLength, int[][] map) {
        int currentHeight = startHeight;
        int currentLength = startLength;
        ArrayList<int[]> path = new ArrayList<>();
        while(true){
            path.add(new int[]{currentHeight, currentLength});
            //if we are in the position of the end
            if(currentHeight == endHeight && currentLength == endLength){
                return path;
            }

            //check below
            if(inBound(currentHeight-1,currentLength, map) && map[currentHeight-1][currentLength] == Integer.MAX_VALUE){
                map[currentHeight-1][currentLength] = map[currentHeight][currentLength]+ 1;
                currentHeight-=1;
                continue;
            }
            //check above
            if(inBound(currentHeight+1,currentLength, map) && map[currentHeight+1][currentLength] == Integer.MAX_VALUE){
                map[currentHeight+1][currentLength] = map[currentHeight][currentLength] + 1;
                currentHeight+=1;
                continue;
            }
            //check right
            if(inBound(currentHeight,currentLength+1, map) && map[currentHeight][currentLength+1] == Integer.MAX_VALUE){
                map[currentHeight][currentLength+1] = map[currentHeight][currentLength] + 1;
                currentLength+=1;
                continue;
            }
            //check left
            if(inBound(currentHeight,currentLength-1, map) && map[currentHeight][currentLength-1] == Integer.MAX_VALUE){
                map[currentHeight][currentLength-1] = map[currentHeight][currentLength] + 1;
                currentLength-=1;
                continue;
            }
            System.out.println("ERROR no path found:" + currentHeight +" " + currentLength);
        }
    }

    public static int[] cheat(int stepsSkippable, int[][] map, ArrayList<int[]> path){
        int[] cheatedTimeSave = new int[path.size()];
        int currentHeight;
        int currentLength;
        for(int[] p:path){
            currentHeight = p[0];
            currentLength = p[1];
            //calculate cheating values
            for(int i = currentHeight - stepsSkippable; i <= currentHeight + stepsSkippable; i++) {
                for (int j = currentLength - stepsSkippable; j <= currentLength + stepsSkippable; j++) {
                    //iterate over distance that can be skipped and check if the tile is a wall or a path
                    if (inBound(i, j, map) && map[i][j] > 0 && Math.abs((i - currentHeight)) + Math.abs((j - currentLength)) <= stepsSkippable) {
                        if(map[i][j]  > (map[currentHeight][currentLength] + Math.abs((i - currentHeight)) + Math.abs((j - currentLength)))){
                            int diff = map[i][j] - (map[currentHeight][currentLength] + Math.abs((i - currentHeight)) + Math.abs((j - currentLength)));
                            cheatedTimeSave[diff]++;
                        }
                    }
                }
            }
        }
        return cheatedTimeSave;
    }

    public static boolean inBound(int height, int length, int[][] map){
        return height <= map.length-1 && height >= 0 && length <= map[1].length-1 && length >= 0;
    }
}
