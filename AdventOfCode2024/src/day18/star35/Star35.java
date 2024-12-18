package day18.star35;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Star35 {

    public static void main(String[] args) {
        String filePath = "src\\day18\\input.txt";

        int xSize = 71; // The size of the x axis. Distance from the left
        int ySize = 71; // The size of the y axis. Distance from the top
        ArrayList<ArrayList<Tile>> map = new ArrayList<>();
        int numberOfBytesFallen = 1024;
        ArrayList<Tile> nextNeighbour = new ArrayList<>(); // List of tiles that are found but not confirmed as the optimal path yet
        ArrayList<int[]> positions = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = br.readLine()) != null && !line.isEmpty()) {
                int x = Integer.valueOf(line.split(",")[0]);
                int y = Integer.valueOf(line.split(",")[1]);
                int[] newInt = {x,y};
                positions.add(newInt);
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }


        //build the map and set all to tiles
        for(int i = 0; i<xSize; i++) {
            ArrayList<Tile> list = new ArrayList<>();
            for(int j = 0; j<ySize; j++) {
                list.add(new Path(Integer.MAX_VALUE, i, j));
            }
            map.add(list);
        }

        //set the walls
        for(int i = 0 ; i<numberOfBytesFallen; i++) {
            int[] position = positions.get(i);
            map.get(position[0]).set(position[1], new Wall(position[0], position[1]));
        }

        //set start and finish
        map.get(0).set(0, new Path(0, 0, 0));
        map.get(xSize-1).set(ySize-1, new Finish(Integer.MAX_VALUE, xSize-1, ySize-1));
        updateNeighbourList(map, nextNeighbour, 0, 0);

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

            //the smallest tile is set to visited
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

    private static void updateNeighbourList(ArrayList<ArrayList<Tile>> map, ArrayList<Tile> nextNeighbour, int Y, int X) {
        // Helper method to process neighbors
        checkAndAddNeighbor(map, nextNeighbour, Y - 1, X, Y, X); // Top
        checkAndAddNeighbor(map, nextNeighbour, Y + 1, X, Y, X); // Bottom
        checkAndAddNeighbor(map, nextNeighbour, Y, X + 1, Y, X); // Right
        checkAndAddNeighbor(map, nextNeighbour, Y, X - 1, Y, X); // Left

        //print the map
        for(int i = 0; i<7; i++) {
            for(int j = 0; j<7; j++) {
                if(map.get(i).get(j) instanceof Path path) {
                    if(path.visited) {
                        System.out.print("O");
                    } else {
                        System.out.print(".");
                    }
                } else if(map.get(i).get(j) instanceof Wall wall) {
                    System.out.print("#");
                } else if(map.get(i).get(j) instanceof Finish finish) {
                    System.out.print("E");
                }
            }
            System.out.println();
        }
        System.out.println("~~~~~~~~~");
    }

    private static void checkAndAddNeighbor(ArrayList<ArrayList<Tile>> map, ArrayList<Tile> nextNeighbour, int neighborY, int neighborX, int currentY, int currentX) {
        if (inBound(map, neighborY, neighborX) &&
                (map.get(neighborY).get(neighborX) instanceof Path || map.get(neighborY).get(neighborX) instanceof Finish) && !((Path) map.get(neighborY).get(neighborX)).visited) {

            Path path = (Path) map.get(neighborY).get(neighborX);
            if (!path.visited) {
                if (path.distance > ((Path) map.get(currentY).get(currentX)).distance + 1) {
                    path.distance = ((Path) map.get(currentY).get(currentX)).distance + 1;
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