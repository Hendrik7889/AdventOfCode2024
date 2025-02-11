package day24.star47;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Star47 {
    public static void main(String[] args) throws InterruptedException {
        String filePath = "src/day24/input.txt";
        List<Variable> variables = new ArrayList<>();
        List<Equations> equations = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null && !line.isEmpty()) {
                Pattern pattern = Pattern.compile("([a-zA-Z0-9]+): ([01])", Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    variables.add(new Variable(matcher.group(1), Integer.parseInt(matcher.group(2)) == 1,true));
                }else {
                    System.out.println("Invalid input");
                }
            }
            while ((line = br.readLine()) != null && !line.isEmpty()) {
                Pattern pattern = Pattern.compile("([a-zA-Z0-9]+) ([a-zA-Z0-9]+) ([a-zA-Z0-9]+) -> ([a-zA-Z0-9]+)$", Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(line);
                if(matcher.find()){
                    boolean variable1Exists = false;
                    boolean variable2Exists = false;
                    boolean variable3Exists = false;
                    Variable variable1 = null;
                    Variable variable2 = null;
                    Variable variable3 = null;

                    for(Variable v : variables) {
                        if (v.name.equals(matcher.group(1))) {
                            variable1 = v;
                            variable1Exists = true;
                            break;
                        }
                    }
                    for(Variable v : variables) {
                        if (v.name.equals(matcher.group(3))) {
                            variable2 = v;
                            variable2Exists = true;
                            break;
                        }
                    }
                    for (Variable v : variables) {
                        if (v.name.equals(matcher.group(4))) {
                            variable3 = v;
                            variable3Exists = true;
                            break;
                        }
                    }

                    if(!variable1Exists){
                        variable1 = new Variable(matcher.group(1), false, false);
                        variables.add(variable1);
                    }
                    if(!variable2Exists){
                        variable2 = new Variable(matcher.group(3), false, false);
                        variables.add(variable2);
                    }
                    if(!variable3Exists){
                        variable3 = new Variable(matcher.group(4), false, false);
                        variables.add(variable3);
                    }

                    String operation = matcher.group(2);
                    equations.add(new Equations(variable1, operation, variable2, variable3));
                }else {
                    System.out.println("Invalid input");
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }

        while(!equations.isEmpty()){
            System.out.println(equations.size());
            for(Equations e : equations){
                if (e.variable1.isSet && e.variable2.isSet) {
                    switch (e.operation) {
                        case "AND" -> e.result.value = e.variable1.value && e.variable2.value;
                        case "OR" -> e.result.value = e.variable1.value || e.variable2.value;
                        case "XOR" -> e.result.value = (e.variable1.value && !e.variable2.value) || (!e.variable1.value && e.variable2.value);
                    }
                    e.result.isSet = true;
                    variables.add(e.result);
                    equations.remove(e);
                    break;
                }
            }
        }
        List<Variable> zVariables = new ArrayList<>();
        for(Variable v : variables){
            if(v.name.startsWith("z")){
                zVariables.add(v);
                System.out.println(v.name + " " + v.value);
            }
        }
        String s = zVariables.stream()
                .distinct()
                .sorted(Comparator.comparing(v -> v.name))
                .map(v -> v.value ? "1" : "0")
                .collect(Collectors.joining());
        s = new StringBuilder(s).reverse().toString();
        System.out.println(s);
        System.out.println(StringBinaryToDecimal(s));
    }

    public static long StringBinaryToDecimal(String s) {
        long ans = 0, i, p = 0;
        int len = s.length();

        for (i = len - 1; i >= 0; i--) {
            if (s.charAt((int) i) == '1') {
                ans += (long) Math.pow(2, p);
            }
            p++;
        }
        return ans;
    }
}

class Variable {
    String name;
    boolean value;
    boolean isSet;

    public Variable(String name, boolean value, boolean isSet) {
        this.name = name;
        this.value = value;
        this.isSet = isSet;
    }
}

class Equations{
    Variable variable1;
    Variable variable2;
    String operation;
    Variable result;

    public Equations(Variable variable1, String operation, Variable variable2, Variable result) {
        this.variable1 = variable1;
        this.variable2 = variable2;
        this.operation = operation;
        this.result = result;
    }
}
