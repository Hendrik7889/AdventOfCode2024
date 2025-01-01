package day21.star42;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Star42 {

    private Map<String, List<String>> moves1;
    private Map<String, List<String>> moves2;
    private Map<String, Long> memo;

    public static void main(String[] args) {
        String filePath = "AdventOfCode2024/src/day21/input.txt";

        ArrayList<String> codes = new ArrayList<>();
        String[] keypad1 = {"789", "456", "123", "#0A"};
        String[] keypad2 = {"#^A", "<v>"};

        Star42 solution = new Star42();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null && !line.isEmpty()) {
                codes.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }

        solution.moves1 = solution.parseMoves(keypad1);
        solution.moves2 = solution.parseMoves(keypad2);
        solution.memo = new HashMap<>();

        long totalComplexity = 0;

        for (String code : codes) {
            long minLen = solution.translate(code, 25, solution.moves1, solution.moves2);
            totalComplexity += minLen * Long.parseLong(code.substring(0, code.length() - 1));
        }

        System.out.println("Total Complexity: " + totalComplexity);
    }

    public Map<String, List<String>> parseMoves(String[] keypad) {
        Map<String, int[]> pos = getKeyPos(keypad);
        List<String> keys = pos.keySet().stream().sorted().collect(Collectors.toList());

        Map<String, List<String>> moves = new HashMap<>();
        for (String key1 : keys) {
            for (String key2 : keys) {
                if (key1.equals("#") || key2.equals("#") || key1.equals(key2)) {
                    continue;
                }
                int[] p1 = pos.get(key1);
                int[] p2 = pos.get(key2);

                if (p1[0] == p2[0]) {
                    String d = p2[1] > p1[1] ? ">" : "<";
                    moves.computeIfAbsent(key1 + key2, k -> new ArrayList<>()).add(d.repeat(Math.abs(p2[1] - p1[1])) + "A");
                } else if (p1[1] == p2[1]) {
                    String d = p2[0] > p1[0] ? "v" : "^";
                    moves.computeIfAbsent(key1 + key2, k -> new ArrayList<>()).add(d.repeat(Math.abs(p2[0] - p1[0])) + "A");
                } else {
                    if (p1[0] != pos.get("#")[0] || p2[1] != pos.get("#")[1]) {
                        String d1 = p2[1] > p1[1] ? ">" : "<";
                        String d2 = p2[0] > p1[0] ? "v" : "^";
                        moves.computeIfAbsent(key1 + key2, k -> new ArrayList<>()).add(d1.repeat(Math.abs(p2[1] - p1[1])) + d2.repeat(Math.abs(p2[0] - p1[0])) + "A");
                    }
                    if (p1[1] != pos.get("#")[1] || p2[0] != pos.get("#")[0]) {
                        String d1 = p2[0] > p1[0] ? "v" : "^";
                        String d2 = p2[1] > p1[1] ? ">" : "<";
                        moves.computeIfAbsent(key1 + key2, k -> new ArrayList<>()).add(d1.repeat(Math.abs(p2[0] - p1[0])) + d2.repeat(Math.abs(p2[1] - p1[1])) + "A");
                    }
                }
            }
        }
        return moves;
    }

    private Map<String, int[]> getKeyPos(String[] keypad) {
        Map<String, int[]> pos = new HashMap<>();
        for (int r = 0; r < keypad.length; r++) {
            for (int c = 0; c < keypad[r].length(); c++) {
                pos.put(String.valueOf(keypad[r].charAt(c)), new int[]{r, c});
            }
        }
        return pos;
    }

    public static List<List<String>> buildCombinations(List<List<String>> arrays, List<String> current, int index) {
        if (index == arrays.size()) {
            return Collections.singletonList(current);
        }
        List<List<String>> results = new ArrayList<>();
        for (String value : arrays.get(index)) {
            List<String> newCurrent = new ArrayList<>(current);
            newCurrent.add(value);
            results.addAll(buildCombinations(arrays, newCurrent, index + 1));
        }
        return results;
    }

    public long translate(String code, int depth, Map<String, List<String>> moves1, Map<String, List<String>> moves2) {
        String memoKey = code + "-" + depth;
        if (memo.containsKey(memoKey)) {
            return memo.get(memoKey);
        }

        List<List<String>> moves;
        if (Character.isDigit(code.charAt(0))) {
            moves = translateNumpad(code, moves1);
        } else {
            moves = translateKeypad(code, moves2);
        }

        long result;
        if (depth == 0) {
            result = moves.stream().mapToLong(move -> move.stream().mapToLong(String::length).sum()).min().orElse(Long.MAX_VALUE);
        } else {
            result = moves.stream().mapToLong(move -> move.stream().mapToLong(currCode -> translate(currCode, depth - 1, moves1, moves2)).sum()).min().orElse(Long.MAX_VALUE);
        }

        memo.put(memoKey, result);
        return result;
    }

    public List<List<String>> translateNumpad(String code, Map<String, List<String>> moves1) {
        code = "A" + code;
        List<List<String>> moves = new ArrayList<>();
        for (int i = 0; i < code.length() - 1; i++) {
            moves.add(moves1.get(code.substring(i, i + 2)));
        }
        return buildCombinations(moves, new ArrayList<>(), 0);
    }

    public List<List<String>> translateKeypad(String code, Map<String, List<String>> moves2) {
        code = "A" + code;
        List<List<String>> moves = new ArrayList<>();
        for (int i = 0; i < code.length() - 1; i++) {
            if (code.charAt(i) != code.charAt(i + 1)) {
                moves.add(moves2.get(code.substring(i, i + 2)));
            } else {
                moves.add(Collections.singletonList("A"));
            }
        }
        return buildCombinations(moves, new ArrayList<>(), 0);
    }
}