package Stack;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpressionCalculator {

    private static Map<String, Integer> priorityMap = new HashMap<>();

    static {
        priorityMap.put("-", 0);
        priorityMap.put("+", 0);
        priorityMap.put("*", 1);
        priorityMap.put("/", 1);
        priorityMap.put("^", 2);
        priorityMap.put("(", 3);
        priorityMap.put(")", 3);
    }


    public static void main(String[] args) {
        String expression;
        int result;
        expression = "3+4";
        result = calculateExpression(expression);
        System.out.println("Expect 7 = " + result);

        expression = "3+4 * (1+5)";
        result = calculateExpression( expression);
        System.out.println("Expect 27 = " + result);

        expression = "2^(1+2) * 4";
        result = calculateExpression(expression);
        System.out.println("Expect 32 = " + result);

        expression = "3-4";
        result = calculateExpression(expression);
        System.out.println("Expect -1 = " + result);

        expression = "6 + ((1+3)-2) * 2";
        result = calculateExpression(expression);
        System.out.println("Expect 10 = " + result);

        expression = "(6 + ((1+3)-2) * 2)";
        result = calculateExpression(expression);
        System.out.println("Expect 10 = " + result);

        expression = "1 + (2 + 4) + ( 5 + 6) + 7";
        result = calculateExpression(expression);
        System.out.println("Expect 25 = " + result);
    }

    public static int calculateExpression(String expression) {
        String s = expression;
        int leftBracketIndex = s.indexOf('(');
        while (leftBracketIndex != -1) {
            int rightBracketIndex = getRightBracketIndex(s, leftBracketIndex);
            String subexpression = s.substring(leftBracketIndex + 1, rightBracketIndex);
            int subexpressionResult = calculateExpression(subexpression);
            String part1 = s.substring(0, leftBracketIndex);
            String part2 = s.substring(rightBracketIndex + 1);
            s = part1 + subexpressionResult + part2;
            leftBracketIndex = s.indexOf('(');

        }

        List<String> tokens = tokenize(s, priorityMap);
        TestStack<String> stack = new TestStack<>();
        if (expression.length() == 1) {
            return Integer.parseInt(expression);
        }
        int i = 0;
        int prevousPriority = -1;
        int result = 0;

        while (i < tokens.size()) {
            String c = tokens.get(i);
            i++;
            if (StringUtils.isNumeric(c)) { // if is number
                stack.push(c);
            } else if (priorityMap.containsKey(c)) {
                if (priorityMap.get(c) > prevousPriority) {
                    stack.push(c);
                    prevousPriority = priorityMap.get(c);
                    c = tokens.get(i);
                    stack.push(c);
                    i = i + 1;

                } else {
                    Integer operand2 = Integer.parseInt(stack.pop());
                    String operator = stack.pop();
                    Integer operand1 = Integer.parseInt(stack.pop());
                    if (operand1 == null || operand2 == null || operator == null) {
                        break;
                    }
                    result = calculate(operand1, operator, operand2);
                    stack.push(String.valueOf(result));
                    stack.push(c);
                    prevousPriority = priorityMap.get(c);
                }
            }
        }

        if (stack.peek() == null) {
            return result;
        }

        result = 0;
        while (true) {
            if (stack.getSize() == 1) {
                return Integer.parseInt(stack.pop());
            }
            Integer operand2 = Integer.parseInt(stack.pop());
            String operator = stack.pop();
            Integer operand1 = Integer.parseInt(stack.pop());
            if (operand1 == null || operand2 == null || operator == null) {
                break;
            }
            result = calculate(operand1, operator, operand2);
            stack.push(String.valueOf(result));
        }

        return result;
    }


    private static int getRightBracketIndex(String expression, int leftBracketIndex) {
        int rightBracketIndex = -1;
        int bracketCount = 1;
        for (int i = leftBracketIndex + 1; i < expression.length(); i++) {
            if (expression.charAt(i) == '(') {
                bracketCount++;
            } else if (expression.charAt(i) == ')') {
                if (bracketCount == 1) {
                    return i;
                } else {
                    bracketCount--;
                }
            }
        }

        return rightBracketIndex;
    }

    public static int calculate(int operand1, String operator, int operand2) {
        switch (operator) {
            case "+":
                return operand1 + operand2;
            case "-":
                return operand1 - operand2;
            case "*":
                return operand1 * operand2;
            case "/":
                return operand1 / operand2;
            default:
                return (int) Math.pow(operand1, operand2);
        }
    }

    public static List<String> tokenize(String expression, Map<String, Integer> priorityMap) {
        List<String> tokens = new ArrayList<>();
        StringBuilder number = new StringBuilder();
        for (int i = 0; i < expression.length(); i++) {
            if (expression.charAt(i) >= '0' && expression.charAt(i) <= '9') {
                number.append(expression.charAt(i));
            } else if (priorityMap.containsKey(String.valueOf(expression.charAt(i)))) {
                if (number.length() > 0) {
                    tokens.add(number.toString());
                }
                tokens.add(String.valueOf(expression.charAt(i)));
                number.setLength(0);
            }
        }

        if (number.length() > 0) {
            tokens.add(number.toString());
        }

        return tokens;
    }
}
