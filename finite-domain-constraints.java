import java.io.*;
import java.util.*;

public class Solution {
    private static int n;
    private static int[] expr;
    private static char comp;
    private static long possible;
    private static int[] varsstart;
    private static int[] varssize;
    private static HashMap<Character, Integer> varnames;
    
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            testCase(in);
        }
    }
    
    private static void testCase(Scanner in) {
        n = in.nextInt();
        possible = 1;
        varssize = new int[n];
        varsstart = new int[n];
        varnames = new HashMap<>();
        for (int i = 0; i < n; ++i) {
            varnames.put(in.next().charAt(0), i);
            int lower = in.nextInt();
            int upper = in.nextInt();
            possible *= (upper - lower + 1);
            varssize[i] = (upper - lower + 1);
            varsstart[i] = lower;
        }
        in.nextLine();
        parse(in.nextLine());
        in.nextInt();
        output();
    }
    
    private static void parse(String toParse) {
        Stack<Character> parentheses = new Stack<>();
        Stack<int[]> expressions = new Stack<>();
        Stack<Character> operations = new Stack<>();
        String[] tokens = toParse.split(" ");
        for (String token : tokens) {
            switch (token) {
                case "+": case "-": case "*":
                    operations.push(token.charAt(0));
                    break;
                case "(":
                    parentheses.push(token.charAt(0));
                    break;
                case ")":
                {
                    parentheses.pop();
                    int[] expr2 = expressions.pop();
                    int[] expr1 = expressions.pop();
                    char op = operations.pop();
                    expressions.push(operate(expr1, expr2, op));
                    break;
                }
                case "=": case "<":
                    comp = token.charAt(0);
                    break;
                default:
                {
                    int[] expression = new int[n+1];
                    for (int i = 0; i < n+1; ++i)
                        expression[i] = 0;
                    char name = token.charAt(0);
                    if (name >= 'A' && name <= 'Z') {
                        expression[varnames.get(name)] = 1;
                    } else {
                        expression[n] = Integer.parseInt(token);
                    }
                    expressions.push(expression);
                    break;
                }
            }
        }
        int[] expr2 = expressions.pop();
        int[] expr1 = expressions.pop();
        expr = operate(expr1, expr2, '-');
    }
    
    private static int[] operate(int[] expr1, int[] expr2, char op) {
        int[] result = new int[n+1];
        switch (op) {
            case '+':
                for (int i = 0; i < n+1; ++i)
                    result[i] = expr1[i] + expr2[i];
                break;
            case '-':
                for (int i = 0; i < n+1; ++i)
                    result[i] = expr1[i] - expr2[i];
                break;
            case '*':
            {
                boolean first = false;
                for (int i = 0; i < n; ++i)
                    if (expr1[i] != 0) {
                        first = true;
                        break;
                    }
                if (first)
                    for (int i = 0; i < n+1; ++i)
                        result[i] = expr1[i] * expr2[n];
                else
                    for (int i = 0; i < n+1; ++i)
                        result[i] = expr1[n] * expr2[i];           
                break;
            }
        }
        return result;
    }
    
    private static void output() {
        long count = 0;
        int[] indices = new int[n];
        for (int i = 0; i < n; ++i)
            indices[i] = 0;
        int[] values = new int[n];
        for (int i = 0; i < n; ++i) {
            varsstart[i] *= expr[i];
            values[i] = varsstart[i];
        }
        for (long i = 0; i < possible; ++i) {
            long eval = 0;
            for (int j = 0; j < n; ++j) {
                eval += values[j];
            }
            eval += expr[n];
            updateIndices(indices, values);
            switch (comp) {
                case '=':
                    if (eval == 0)
                        ++count;
                    break;
                case '<':
                    if (eval < 0)
                        ++count;
                    break;
            }
        }
        System.out.println(count);
    }
    
    private static void updateIndices(int[] indices, int[] values) {
        for (int i = 0; i < n; ++i) {
            ++indices[i];
            values[i] += expr[i];
            if (indices[i] == varssize[i]) {
                indices[i] = 0;
                values[i] = varsstart[i];
            } else {
                break;
            }
        }
    }
}