package day16.Star32;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Star32 {

    public static void main(String[] args) {
        String filePath = "src\\day16\\input.txt";

        ArrayList<ArrayList<Tile>> map = new ArrayList<>(); //Map of the maze
        ArrayList<Tile> nextNeighbour = new ArrayList<>(); // List of tiles that are found but not confirmed as the optimal path yet

        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;

            int lineCount = 0;
            while ((line = br.readLine()) != null && !line.isEmpty()) {
                ArrayList<Tile> charList = new ArrayList<>();
                for (int j = 0; j < line.length(); j++) {
                    switch (line.charAt(j)) {
                        case '#':
                            charList.add(new Wall(lineCount, j));
                            break;
                        case '.':
                            charList.add(new Path(Integer.MAX_VALUE, false, false, false, false, lineCount, j));
                            break;
                        case 'S':
                            Path path = new Path(0, false, true, false, false, lineCount, j);
                            path.setVisited(true);
                            charList.add(path);
                            break;
                        case 'E':
                            charList.add(new Finish(Integer.MAX_VALUE, false, false, false, false, lineCount, j));
                            break;
                    }
                }
                map.add(charList);
                lineCount++;
            }
        } catch (
                IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }

        //for every start tile execute the updateNeighbourList function
        for (int i = 0; i < map.size(); i++) {
            for (int j = 0; j < map.get(i).size(); j++) {
                if (map.get(i).get(j) instanceof Path path) {
                    if (path.distance == 0) {
                        updateNeighbourList(map, nextNeighbour, path.y, path.x);
                    }
                }
            }
        }

        dijstra(nextNeighbour, map);

        //find the path to the finish
        ArrayList<Finish> finishList = new ArrayList<>();
        for (ArrayList<Tile> tiles : map) {
            for (Tile tile : tiles) {
                if (tile instanceof Finish path) {
                    finishList.add(path);
                }
            }
        }

        //find the number of paths to the finish including the +1000 penalty paths
        ArrayList<Path> pathList = new ArrayList<>();
        for (Finish finish : finishList) {
            if(!pathList.contains(finish)){
                pathList.add(finish);
            }
            findNumberOfPaths(finish, pathList);
        }

        //correct the previousPlus1000 that are not the optimal path
        for(Tile t : pathList){
            correctPreviousPlus1000((Path) t);
        }

        //count the number of tiles on the path
        pathList = new ArrayList<>();
        for (Finish finish : finishList) {
            if(!pathList.contains(finish)){
                pathList.add(finish);
            }
            findNumberOfPaths(finish, pathList);
        }

        System.out.println("The number of paths to the finishes is: " + pathList.size());
    }

    private static void dijstra(ArrayList<Tile> nextNeighbour, ArrayList<ArrayList<Tile>> map) {
        boolean found = false;
        while (!found) {
            //region Get the smallest value from the nextNeighbour list and set it to visited
            if (nextNeighbour.isEmpty()) {
                System.out.println("No path found to the finish");
                break;
            }
            int smallest = 0;
            for (int j = 0; j < nextNeighbour.size(); j++) {
                if (nextNeighbour.get(j) instanceof Path path) {
                    if (path.distance <= ((Path) nextNeighbour.get(smallest)).distance) {
                        smallest = j;
                    }
                }
            }
            Path path = (Path) nextNeighbour.get(smallest);
            nextNeighbour.remove(smallest);
            if (map.get(path.y).get(path.x) instanceof Finish) {
                found = true;
                continue;
            }
            path.setVisited(true);
            //endregion
            updateNeighbourList(map, nextNeighbour, path.y, path.x);
        }
    }

    /**
     * Recursively find the number of paths to the finish.
     * Tiles are not counted twice.
     *
     * @param tile the tile to find the number of paths from finish to the start
     * @return the number of tiles on all fastest paths to the finish
     */
    public static void findNumberOfPaths(Path tile, ArrayList<Path> pathList) {
            for (Tile path : tile.previous) {
                if (pathList.contains(path)) {
                    continue;
                }
                pathList.add((Path) path);
                findNumberOfPaths((Path) path, pathList);
            }
            for(Tile path : tile.previousPlus1000){
                if (pathList.contains(path)) {
                    continue;
                }
                pathList.add((Path) path);
                findNumberOfPaths((Path) path, pathList);
            }
    }

    /**
     * For the new tile added all neighbors to the queue and upadate there values if they allready exist
     * Exclude values that are in the visited list
     *
     * @param map           the map with the walls and open spaces
     * @param nextNeighbour the list of tiles that are found but not confirmed as the optimal path yet
     * @param y             the y coordinate of the tile to update
     * @param x             the x coordinate of the tile to update
     */
    private static void updateNeighbourList (ArrayList <ArrayList <Tile>> map, ArrayList < Tile > nextNeighbour, int y, int x){
        //region tile to the top
        if (inBound(map, y - 1, x) && (map.get(y - 1).get(x) instanceof Path || map.get(y - 1).get(x) instanceof Finish)) {
            Path path = (Path) map.get(y - 1).get(x);
            int penalty = 0;
            if (!((Path) map.get(y).get(x)).top) {
                penalty = 1000;
            }
            if ((!((Path) map.get(y).get(x)).top) && (!((Path) map.get(y).get(x)).left) && (!((Path) map.get(y).get(x)).right)) {
                penalty = 2000;
            }
            //if the new distance to the tile is shorter than the current distance, update the distance and the previous tile
            updateDistanceAndVariables(map, y, x, path, penalty,Direction.TOP);
            //add the tile to the list of nextNeighbour if it is not already in the list
            if (!nextNeighbour.contains(path) && !path.visited) {
                nextNeighbour.add(path);
            }
        }
        //endregion
        //region tile to the right
        if (inBound(map, y, x + 1) && (map.get(y).get(x + 1) instanceof Path || map.get(y).get(x + 1) instanceof Finish)) {
            Path path = (Path) map.get(y).get(x + 1);
            int penalty = 0;
            if (!((Path) map.get(y).get(x)).right) {
                penalty = 1000;
            }
            if ((!((Path) map.get(y).get(x)).top) && (!((Path) map.get(y).get(x)).right) && (!((Path) map.get(y).get(x)).bottum)) {
                penalty = 2000;
            }
            //if the new distance to the tile is shorter than the current distance, update the distance and the previous tile
            updateDistanceAndVariables(map, y, x, path, penalty,Direction.RIGHT);
            //add the tile to the list of nextNeighbour if it is not already in the list
            if (!nextNeighbour.contains(path) && !path.visited) {
                nextNeighbour.add(path);
            }
        }
        //endregion
        //region tile to the bottom
        if (inBound(map, y + 1, x) && (map.get(y + 1).get(x) instanceof Path || map.get(y + 1).get(x) instanceof Finish)) {
            Path path = (Path) map.get(y + 1).get(x);
            int penalty = 0;
            if (!((Path) map.get(y).get(x)).bottum) {
                penalty = 1000;
            }
            if ((!((Path) map.get(y).get(x)).bottum) && (!((Path) map.get(y).get(x)).left) && (!((Path) map.get(y).get(x)).right)) {
                penalty = 2000;
            }
            //if the new distance to the tile is shorter than the current distance, update the distance and the previous tile
            updateDistanceAndVariables(map, y, x, path, penalty,Direction.BOTTOM);
            //add the tile to the list of nextNeighbour if it is not already in the list
            if (!nextNeighbour.contains(path) && !path.visited) {
                nextNeighbour.add(path);
            }
        }
        //endregion
        //region tile to the left
        if (inBound(map, y, x - 1) && (map.get(y).get(x - 1) instanceof Path || map.get(y).get(x - 1) instanceof Finish)) {
            Path path = (Path) map.get(y).get(x - 1);
            int penalty = 0;
            if (!((Path) map.get(y).get(x)).left) {
                penalty = 1000;
            }
            if ((!((Path) map.get(y).get(x)).top) && (!((Path) map.get(y).get(x)).left) && (!((Path) map.get(y).get(x)).bottum)) {
                penalty = 2000;
            }
            //if the new distance to the tile is shorter than the current distance, update the distance and the previous tile
            updateDistanceAndVariables(map, y, x, path, penalty,Direction.LEFT);
            //add the tile to the list of nextNeighbour if it is not already in the list
            if (!nextNeighbour.contains(path) && !path.visited) {
                nextNeighbour.add(path);
            }
        }
        //endregion
    }

    /**
     * removes the previousPlus1000 that are not the optimal path
     * @param path
     */
    private static void correctPreviousPlus1000(Path path) {
        if(path.x == 3 && path.y == 1){
            System.out.println("test");
        }
        //get the current direction and check if the direction matches previousPlus1000 of the previous tiles
        /**
         *  O
         *  OOOOOOOO
         *  O   X  O
         *  O   XXXO
         *  O      O
         *  OOOOOOOO
         *         O
         *
         *
         * Ignore the X's
         */
        for(int j =0 ; j< path.previous.size() ; j++){
            Path previous = (Path) path.previous.get(j);
            for (int i = 0; i < previous.previousPlus1000.size() ; i++) {
                Path previous2 = (Path) previous.previousPlus1000.get(i);
                if(   !(Math.abs(path.y -previous2.y) == 2 && Math.abs(path.x -previous2.x)==0 ||
                        Math.abs(path.y -previous2.y) == 0 && Math.abs(path.x -previous2.x)==2) ){
                    previous.previousPlus1000.remove(previous2);
                }
            }
        }
    }

    /**
     * Update the distance and the previous tile of the path if the new distance is shorter
     *
     * @param map     the map with the walls and open spaces
     * @param Y       the y coordinate of the tile
     * @param X       the x coordinate of the tile
     * @param path    the path to update
     * @param penalty the penalty for the tile
     */
    private static void updateDistanceAndVariables(ArrayList<ArrayList<Tile>> map, int Y, int X, Path path, int penalty,Direction direction) {
        if (path.distance > ((Path) map.get(Y).get(X)).distance + 1 + penalty) {
            path.distance = ((Path) map.get(Y).get(X)).distance + 1 + penalty;
            path.right = false;
            path.top = false;
            path.bottum = false;
            path.left = false;
            switch (direction){
                case TOP -> path.top = true;
                case RIGHT -> path.right = true;
                case BOTTOM -> path.bottum = true;
                case LEFT -> path.left = true;
            }
            ArrayList<Tile> previous = new ArrayList<>();
            previous.add(map.get(Y).get(X));
            path.previousPlus1000 = new ArrayList<>();
            path.previous = previous;
        } else if (path.distance == (((Path) map.get(Y).get(X)).distance + 1 + penalty)) {
            switch (direction){
                case TOP -> path.top = true;
                case RIGHT -> path.right = true;
                case BOTTOM -> path.bottum = true;
                case LEFT -> path.left = true;
            }
            path.previous.add(map.get(Y).get(X));
        } else if (path.distance + 1000 == ((Path) map.get(Y).get(X)).distance + 1 + penalty) {
            path.previousPlus1000.add(map.get(Y).get(X));
        }
    }

    /**
         * Check if the tile is in the bounds of the map
         *
         * @param map the map with the walls and open spaces
         * @param Y   the y coordinate of the tile
         * @param X   the x coordinate of the tile
         * @return true if the tile is in the bounds of the map
         */
        private static boolean inBound (ArrayList < ArrayList < Tile >> map,int Y, int X){
            return Y >= 0 && Y < map.size() && X >= 0 && X < map.getFirst().size();
        }
    }


abstract class Tile {
    int y;
    int x;

    public Tile(int y, int x) {
        this.y = y;
        this.x = x;
    }
}

class Path extends Tile {
    int distance;
    boolean top;
    boolean right;
    boolean bottum;
    boolean left;
    boolean visited;
    ArrayList<Tile> previous;
    ArrayList<Tile> previousPlus1000;

    public Path(int distance, boolean top, boolean right, boolean bottum, boolean left, int y, int x) {
        super(y, x);
        this.distance = distance;
        this.top = top;
        this.right = right;
        this.bottum = bottum;
        this.left = left;
        previous = new ArrayList<>();
        previousPlus1000 = new ArrayList<>();
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }
}

class Wall extends Tile {
    public Wall(int y, int x) {
        super(y, x);
    }
}

class Finish extends Path {
    public Finish(int distance, boolean top, boolean right, boolean bottum, boolean left, int y, int x) {
        super(distance, top, right, bottum, left, y, x);
    }
}

enum Direction {
    TOP,
    RIGHT,
    BOTTOM,
    LEFT
}