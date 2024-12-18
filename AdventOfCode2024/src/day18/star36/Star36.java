package day18.star36;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Star36 {

    public static void main(String[] args) {
        String filePath = "src\\day18\\input.txt";

        int xSize = 71; // The size of the X-axis. Distance from the left
        int ySize = 71; // The size of the Y-axis. Distance from the top
        ArrayList<ArrayList<Tile>> map;
        int numberOfBytesFallen = 1024; // number to start the bytes falling
        ArrayList<Tile> nextNeighbour = new ArrayList<>(); // List of tiles that are found but not confirmed as the optimal path yet
        ArrayList<int[]> positions = new ArrayList<>(); // List of positions on the current route

        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = br.readLine()) != null && !line.isEmpty()) {
                int x = Integer.parseInt(line.split(",")[0]);
                int y = Integer.parseInt(line.split(",")[1]);
                int[] newInt = {x, y};
                positions.add(newInt);
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }

        //region Build the map and set all to tiles
        map = new ArrayList<>();
        for (int i = 0; i < xSize; i++) {
            ArrayList<Tile> list = new ArrayList<>();
            for (int j = 0; j < ySize; j++) {
                list.add(new Path(Integer.MAX_VALUE, i, j));
            }
            map.add(list);
        }

        //set the walls
        for (int i = 0; i < numberOfBytesFallen; i++) {
            int[] position = positions.get(i);
            map.get(position[1]).set(position[0], new Wall(position[1], position[0]));
        }

        //endregion
        int[] lastHit = null;                   // The last hit position
        int additionalBytesFallen = numberOfBytesFallen;
        while (true) {
            map.get(0).set(0, new Path(0, 0, 0));
            map.get(xSize - 1).set(ySize - 1, new Finish(Integer.MAX_VALUE, xSize - 1, ySize - 1));
            updateNeighbourList(map, nextNeighbour, 0, 0);
            boolean found = false;
            while (!found) {
                //region Get the smallest value from the nextNeighbour list and set it to visited
                if (nextNeighbour.isEmpty()) {
                    if(lastHit != null){
                        System.out.println("The last hit was at: " + lastHit[0] + "," + lastHit[1]);
                        System.exit(0);
                    }else {
                        System.out.println("No path found to the finish");
                        System.exit(0);
                    }
                }
                int smallest = 0;
                for (int j = 0; j < nextNeighbour.size(); j++) {
                    if (nextNeighbour.get(j) instanceof Path path) {
                        if (path.distance <= ((Path) nextNeighbour.get(smallest)).distance) {
                            smallest = j;
                        }
                    }
                }

                //the smallest tile var visited is set to true
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

            //Execute here if a path is found to the Finish Tile.
            //Get the path from the finish to the start
            ArrayList<Tile> path = getPathFromFinishToStart(map, xSize, ySize);
            //add the additional bytes fallen until the path is blocked
            boolean hit = false;
            while (!hit) {
                additionalBytesFallen++;
                map.get(positions.get(additionalBytesFallen)[1]).set(positions.get(additionalBytesFallen)[0], new Wall(positions.get(additionalBytesFallen)[1], positions.get(additionalBytesFallen)[0]));
                for (Tile t : path) {
                    if (t.x == positions.get(additionalBytesFallen)[0] && t.y == positions.get(additionalBytesFallen)[1]) {
                        hit = true;
                        break;
                    }
                }
                lastHit = positions.get(additionalBytesFallen);
            }
            //reset the map
            for (int i = 0; i < xSize; i++) {
                for (int j = 0; j < ySize; j++) {
                    if (map.get(i).get(j) instanceof Path) {
                        ((Path) map.get(i).get(j)).distance = Integer.MAX_VALUE;
                        ((Path) map.get(i).get(j)).visited = false;
                        ((Path) map.get(i).get(j)).previous = null;
                    }
                }
            }
            //reset the nextNeighbour list
            nextNeighbour = new ArrayList<>();
        }
    }

    private static ArrayList<Tile> getPathFromFinishToStart(ArrayList<ArrayList<Tile>> map, int xSize, int ySize) {
        ArrayList<Tile> path = new ArrayList<>();
        // Get the path from the finish to the start and save it in the path list
        boolean done = false;
        Tile backtrackPath = map.get(xSize - 1).get(ySize - 1);
        while (!done) {
            path.add(backtrackPath);
            if (backtrackPath instanceof Path) {
                if (((Path) backtrackPath).previous != null) {
                    backtrackPath = ((Path) backtrackPath).previous;
                    if (backtrackPath instanceof Path && ((Path) backtrackPath).distance == 0) {
                        done = true;
                    }
                }
            }
        }
        return path;
    }

    private static void updateNeighbourList(ArrayList<ArrayList<Tile>> map, ArrayList<Tile> nextNeighbour, int Y, int X) {
        // Helper method to process neighbors
        checkAndAddNeighbor(map, nextNeighbour, Y - 1, X, Y, X); // Top
        checkAndAddNeighbor(map, nextNeighbour, Y + 1, X, Y, X); // Bottom
        checkAndAddNeighbor(map, nextNeighbour, Y, X + 1, Y, X); // Right
        checkAndAddNeighbor(map, nextNeighbour, Y, X - 1, Y, X); // Left
    }

    private static void checkAndAddNeighbor(ArrayList<ArrayList<Tile>> map, ArrayList<Tile> nextNeighbour, int neighborY, int neighborX, int currentY, int currentX) {
        if (inBound(map, neighborY, neighborX) &&
                (map.get(neighborY).get(neighborX) instanceof Path || map.get(neighborY).get(neighborX) instanceof Finish) && !((Path) map.get(neighborY).get(neighborX)).visited) {

            Path path = (Path) map.get(neighborY).get(neighborX);
            if (!path.visited) {
                if (path.distance > ((Path) map.get(currentY).get(currentX)).distance + 1) {
                    path.distance = ((Path) map.get(currentY).get(currentX)).distance + 1;
                    path.previous = map.get(currentY).get(currentX);
                }
                if (!nextNeighbour.contains(path)) {
                    nextNeighbour.add(path);
                }
            }
        }
    }

    private static boolean inBound(ArrayList<ArrayList<Tile>> map, int Y, int X) {
        return Y >= 0 && Y < map.size() && X >= 0 && X < map.get(0).size();
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
    boolean visited;
    Tile previous;

    public Path(int distance, int y, int x) {
        super(y, x);
        this.distance = distance;
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
    public Finish(int distance, int y, int x) {
        super(distance, y, x);
    }
}