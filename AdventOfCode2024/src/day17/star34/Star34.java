package day17.star34;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Star34 {

    static long success = Long.MAX_VALUE;

    public static void main(String[] args) {
        String filePath = "src\\day17\\input.txt";

        // Registers and program storage
        long registerB = 0;
        long registerC = 0;
        ArrayList<Long> program = new ArrayList<>();

        // Read the input file
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                if (line.startsWith("Register B:")) {
                    registerB = Integer.parseInt(line.split(":")[1].trim());
                } else if (line.startsWith("Register C:")) {
                    registerC = Integer.parseInt(line.split(":")[1].trim());
                } else if (line.startsWith("Program:")) {
                    String[] programValues = line.split(":")[1].trim().split(",");
                    for (String value : programValues) {
                        program.add((long) Integer.parseInt(value.trim()));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }

        dfs(program, 0, 0);

        System.out.println("Output: " + success);
    }

    static void dfs(List<Long> program, long cur, int pos) {
        for (int i = 0; i < 8; i++) {
            long nextNum = (cur << 3) + i;
            List<Long> execResult = execProgram(nextNum, program);
            if (!execResult.equals(program.subList(program.size() - pos - 1, program.size()))) {
                continue;
            }
            if (pos == program.size() - 1) {
                success = Math.min(success, nextNum);
                return;
            }
            dfs(program, nextNum, pos + 1);
        }
    }

    static List<Long> execProgram(long A, List<Long> program) {
        int pointer = 0;
        List<Long> output = new ArrayList<>();
        long B = 0, C = 0;
        while (pointer >= 0 && pointer < program.size()) {
            long opcode = program.get(pointer);
            long literalOperand = program.get(pointer + 1);
            long combo = literalOperand < 4 ? literalOperand : (literalOperand == 4 ? A : (literalOperand == 5 ? B : C));
            switch ((int) opcode) {
                case 0:
                    A = (long) (A / Math.pow(2, combo));
                    pointer += 2;
                    break;
                case 1:
                    B = B ^ literalOperand;
                    pointer += 2;
                    break;
                case 2:
                    B = combo % 8;
                    pointer += 2;
                    break;
                case 3:
                    if (A != 0) {
                        pointer = (int) literalOperand;
                    } else {
                        pointer += 2;
                    }
                    break;
                case 4:
                    B ^= C;
                    pointer += 2;
                    break;
                case 5:
                    output.add(combo % 8);
                    pointer += 2;
                    break;
                case 6:
                    B = (long) (A / Math.pow(2, combo));
                    pointer += 2;
                    break;
                case 7:
                    C = (long) (A / Math.pow(2, combo));
                    pointer += 2;
                    break;
            }
        }

        return output;
    }
}
