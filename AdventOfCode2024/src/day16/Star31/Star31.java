package day16.Star31;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Star31 {
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
            if(map.get(path.y).get(path.x) instanceof Finish) {
                System.out.println("The shortest path to the finish is: " + path.distance);
                found = true;
                continue;
            }
            path.setVisited(true);
            //endregion
            updateNeighbourList(map, nextNeighbour, path.y, path.x);
        }
    }

    /**
     * For the new tile added all neighbors to the queue and upadate there values if they allready exist
     * Exclude values that are in the visited list
     *
     * @param map           the map with the walls and open spaces
     * @param nextNeighbour the list of tiles that are found but not confirmed as the optimal path yet
     * @param Y             the y coordinate of the tile to update
     * @param X             the x coordinate of the tile to update
     */
    private static void updateNeighbourList(ArrayList<ArrayList<Tile>> map, ArrayList<Tile> nextNeighbour, int Y, int X) {
        //region tile to the top
        if(inBound(map, Y - 1, X) && map.get(Y - 1).get(X) instanceof Path || map.get(Y - 1).get(X) instanceof Finish ) {
            Path path = (Path) map.get(Y - 1).get(X);
            if(!path.visited) {
                int penalty = 0;
                if (!((Path) map.get(Y).get(X)).top) {
                    penalty = 1000;
                }
                if((!((Path) map.get(Y).get(X)).top) && (!((Path) map.get(Y).get(X)).left) && (!((Path) map.get(Y).get(X)).right)) {
                    penalty = 2000;
                }
                if (path.distance >= ((Path) map.get(Y).get(X)).distance + 1 + penalty) {
                    path.distance = ((Path) map.get(Y).get(X)).distance + 1 + penalty;
                    Path upperPath = (Path) map.get(Y - 1).get(X);
                    upperPath.top = true;
                }
                //add the tile to the list of nextNeighbour if it is not already in the list
                if (!nextNeighbour.contains(path)) {
                    nextNeighbour.add(path);
                }
            }
        }
        //endregion
        //region tile to the right
        if(inBound(map, Y, X + 1) && map.get(Y).get(X + 1) instanceof Path || map.get(Y).get(X + 1) instanceof Finish) {
            Path path = (Path) map.get(Y).get(X + 1);
            if (!path.visited) {
                int penalty = 0;
                if (!((Path) map.get(Y).get(X)).right) {
                    penalty = 1000;
                }
                if ((!((Path) map.get(Y).get(X)).top) && (!((Path) map.get(Y).get(X)).right) && (!((Path) map.get(Y).get(X)).bottum)) {
                    penalty = 2000;
                }
                if (path.distance >= ((Path) map.get(Y).get(X)).distance + 1 + penalty) {
                    path.distance = ((Path) map.get(Y).get(X)).distance + 1 + penalty;
                    Path rightPath = (Path) map.get(Y).get(X + 1);
                    rightPath.right = true;
                }
                //add the tile to the list of nextNeighbour if it is not already in the list
                if (!nextNeighbour.contains(path)) {
                    nextNeighbour.add(path);
                }
            }
        }
        //endregion
        //region tile to the bottum
        if(inBound(map,Y-1,X) && map.get(Y+1).get(X) instanceof Path || map.get(Y+1).get(X) instanceof Finish) {
            Path path = (Path) map.get(Y + 1).get(X);
            if (!path.visited) {
                int penalty = 0;
                if (!((Path) map.get(Y).get(X)).bottum) {
                    penalty = 1000;
                }
                if ((!((Path) map.get(Y).get(X)).bottum) && (!((Path) map.get(Y).get(X)).left) && (!((Path) map.get(Y).get(X)).right)) {
                    penalty = 2000;
                }
                if (path.distance >= ((Path) map.get(Y).get(X)).distance + 1 + penalty) {
                    path.distance = ((Path) map.get(Y).get(X)).distance + 1 + penalty;
                    Path bottumPath = (Path) map.get(Y + 1).get(X);
                    bottumPath.bottum = true;
                }
                //add the tile to the list of nextNeighbour if it is not already in the list
                if (!nextNeighbour.contains(path)) {
                    nextNeighbour.add(path);
                }
            }
        }
        //endregion
        //region tile to the left
        if(inBound(map,Y,X-1) && map.get(Y).get(X-1) instanceof Path || map.get(Y).get(X-1) instanceof Finish) {
            Path path = (Path) map.get(Y).get(X - 1);
            if(!path.visited) {
                int penalty = 0;
                if (!((Path) map.get(Y).get(X)).left) {
                    penalty = 1000;
                }
                if ((!((Path) map.get(Y).get(X)).top) && (!((Path) map.get(Y).get(X)).left) && (!((Path) map.get(Y).get(X)).bottum)) {
                    penalty = 2000;
                }
                if (path.distance >= ((Path) map.get(Y).get(X)).distance + 1 + penalty) {
                    path.distance = ((Path) map.get(Y).get(X)).distance + 1 + penalty;
                    Path leftPath = (Path) map.get(Y).get(X - 1);
                    leftPath.left = true;
                }
                //add the tile to the list of nextNeighbour if it is not already in the list
                if (!nextNeighbour.contains(path)) {
                    nextNeighbour.add(path);
                }
            }
        }
        //endregion
    }

    /**
     * Check if the tile is in the bounds of the map
     *
     * @param map the map with the walls and open spaces
     * @param Y   the y coordinate of the tile
     * @param X   the x coordinate of the tile
     * @return true if the tile is in the bounds of the map
     */
    private static boolean inBound(ArrayList<ArrayList<Tile>> map, int Y, int X) {
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

    public Path(int distance, boolean top, boolean right, boolean bottum, boolean left, int y, int x) {
        super(y, x);
        this.distance = distance;
        this.top = top;
        this.right = right;
        this.bottum = bottum;
        this.left = left;
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
