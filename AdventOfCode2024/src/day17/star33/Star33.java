package day17.star33;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Star33 {

    public static void main(String[] args) {
        String filePath = "src\\day17\\input.txt";

        int instructionPointer = 0; // Instruction Pointer
        List<Long> output = new ArrayList<>();
        long registerA = 0;
        long registerB = 0;
        long registerC = 0;
        // Program input
        ArrayList program = new ArrayList<Integer>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;

            while ((line = br.readLine()) != null) {
                if (line.startsWith("Register A:")) {
                    registerA = Integer.parseInt(line.split(":")[1].trim());
                } else if (line.startsWith("Register B:")) {
                    registerB = Integer.parseInt(line.split(":")[1].trim());
                } else if (line.startsWith("Register C:")) {
                    registerC = Integer.parseInt(line.split(":")[1].trim());
                } else if (line.startsWith("Program:")) {
                    String[] programValues = line.split(":")[1].trim().split(",");
                    for (String value : programValues) {
                        program.add(Integer.parseInt(value.trim()));
                    }
                }
            }
        } catch (
                IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }


        // Create and run the 3-bit computer
        int[] programMemory = new int[program.size()];
        for (int i = 0; i < program.size(); i++) {
            programMemory[i] = (int) program.get(i);
        }
        executeProgram(programMemory, registerA, registerB, registerC, instructionPointer, output);

        // Output result
        String s = getOutputString(output);
        System.out.println("Output: " + s);
    }

    private static String getOutputString(List<Long> output) {
        StringBuilder sb = new StringBuilder();
        for (Long value : output) {
            sb.append(value);
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }



    public static void executeProgram(int[] program, long A, long B, long C, int instructionPointer, List<Long> output) {
        while (instructionPointer < program.length) {
            int opcode = program[instructionPointer];
            int operand = program[instructionPointer + 1];
            instructionPointer += 2; // Default increment

            switch (opcode) {
                case 0: // adv
                    A = A / (1 << resolveComboOperand(operand,A,B,C));
                    break;
                case 1: // bxl
                    B = B ^ operand;
                    break;
                case 2: // bst
                    B = resolveComboOperand(operand,A,B,C) % 8;
                    break;
                case 3: // jnz
                    if (A != 0) {
                        instructionPointer = operand;
                    }
                    break;
                case 4: // bxc
                    B = B ^ C;
                    break;
                case 5: // out
                    output.add(resolveComboOperand(operand,A,B,C) % 8);
                    break;
                case 6: // bdv
                    B = A / (1 << resolveComboOperand(operand,A,B,C));
                    break;
                case 7: // cdv
                    C = A / (1 << resolveComboOperand(operand,A,B,C));
                    break;
                default:
                    throw new IllegalArgumentException("Unknown opcode: " + opcode);
            }
        }
    }

    private static long resolveComboOperand(int operand, long A, long B, long C) {
        return switch (operand) {
            case 0, 1, 2, 3 -> operand;
            case 4 -> A;
            case 5 -> B;
            case 6 -> C;
            default -> throw new IllegalArgumentException("Invalid combo operand: " + operand);
        };
    }
}
