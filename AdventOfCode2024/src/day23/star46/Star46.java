package day23.star46;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Star46 {
    public static void main(String[] args) {
        String filePath = "src/day23/connections.txt";
        Graph graph = new Graph();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null && !line.isEmpty()) {
                Pattern pattern = Pattern.compile("^([a-z]+)-([a-z]+)$", Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    graph.addEdge(matcher.group(1), matcher.group(2));
                } else {
                    System.out.println("No connection found");
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }

        int sizeOfFullSubGraph = 3;
        List<List<Vertex>> lastResult = new ArrayList<>();
        boolean setFound = true;
        while (setFound){
            sizeOfFullSubGraph++;
            System.out.println("Checking size: " + sizeOfFullSubGraph);
            List<List<Vertex>> result = findNumberOfFullSubGraphs(graph,sizeOfFullSubGraph);
            if(result.isEmpty()){
                setFound = false;
            } else {
                lastResult = result;
            }
        }
        String[] labels = lastResult.get(0).stream().map(v -> v.label).toArray(String[]::new);
        String result = Arrays.stream(labels).sorted().collect(Collectors.joining(","));
        System.out.println("Result:" + result);
    }

    public static List<List<Vertex>> findNumberOfFullSubGraphs(Graph graph, int size) {
        List<List<Vertex>> fullSubGraphs = new ArrayList<>();
        for (Vertex v : graph.getAdjVertices().keySet()) {
            List<List<Vertex>> combinations = generateCombinations(graph.getAdjVertices(v), size - 1);
            for(List<Vertex> combination : combinations) {
                combination.add(v);
                // Check if the combination is a full subgraph
                boolean isFullSubGraph = isFullSubGraph(graph, combination);
                if (isFullSubGraph) {
                    fullSubGraphs.add(combination);
                }
            }
        }

        return fullSubGraphs;
    }

    /**
     * Method to check if the combination is a full subgraph
     * @param graph the graph
     * @param combination an arbitrary combination of vertices
     * @return true if the combination is a full subgraph, false otherwise
     */
    private static boolean isFullSubGraph(Graph graph, List<Vertex> combination) {
        boolean isFullSubGraph = true;
        for (Vertex vertex1 : combination) {
            for(Vertex vertex2: combination) {
                if (vertex1 != vertex2 && !graph.getAdjVertices(vertex1).contains(vertex2)) {
                    isFullSubGraph = false;
                    break;
                }
            }
            if(!isFullSubGraph) {
                break;
            }
        }
        return isFullSubGraph;
    }

    public static List<List<Vertex>> generateCombinations(List<Vertex> set, int x) {
        List<List<Vertex>> result = new ArrayList<>();
        backtrack(set, x, 0, new ArrayList<>(), result);
        return result;
    }

    private static void backtrack(List<Vertex> set, int x, int start, List<Vertex> current, List<List<Vertex>> result) {
        if (current.size() == x) {
            result.add(new ArrayList<>(current)); // Add copy of the current combination
            return;
        }

        for (int i = start; i < set.size(); i++) {
            current.add(set.get(i));  // Choose
            backtrack(set, x, i + 1, current, result); // Explore
            current.removeLast();
        }
    }
}

class Graph {
    private Map<Vertex, List<Vertex>> adjVertices;

    /**
     * Method to add an edge between two vertices and add the vertices to the adjVertices map if they do not exist
     * @param label1 the label of the first vertex
     * @param label2 the label of the second vertex
     */
    void addEdge(String label1, String label2) {
        //create vertices if they do not exist
        Vertex v1 = getOrCreateVertex(label1);
        Vertex v2 = getOrCreateVertex(label2);

        // Add the edge if it does not already exist
        if (!adjVertices.get(v1).contains(v2)) {
            adjVertices.get(v1).add(v2);
        }
        if (!adjVertices.get(v2).contains(v1)) {
            adjVertices.get(v2).add(v1);
        }
    }

    /**
     * Method to create a Vertex when there exists no vertex with the given label determined by the equals method
     * Adds the vertex to the adjVertices map
     * @param label the label of the vertex
     * @return the existing vertex or a new vertex if it does not exist with the given label
     */
    private Vertex getOrCreateVertex(String label) {
        for (Vertex v : adjVertices.keySet()) {
            if (v.label.equals(label)) {
                return v;
            }
        }
        Vertex newVertex = new Vertex(label);
        adjVertices.put(newVertex, new ArrayList<>());
        return newVertex;
    }

    //region Constructor
    public Graph() {
        this.adjVertices = new HashMap<>();
    }
    //endregion
    //region Getter
    public Map<Vertex, List<Vertex>> getAdjVertices() {
        return adjVertices;
    }

    List<Vertex> getAdjVertices(Vertex v) {
        return adjVertices.get(v);
    }
    //endregion
}

class Vertex {
    String label;
    Vertex(String label) {
        this.label = label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex vertex = (Vertex) o;
        return label.equals(vertex.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label);
    }
}