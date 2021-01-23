package expressions;

import interpreter.CompParser;

import java.util.LinkedList;
import java.util.Stack;

public class ShuntingYard {

    private static final char ZERO = '0';
    private static final char NINE = '9';
    private static final char CAPITAL_A = 'A';
    private static final char CAPITAL_Z = 'Z';
    private static final char SMALL_A = 'a';
    private static final char SMALL_Z = 'z';

    public static double calc(final String expression) {

        final LinkedList<String> queue = new LinkedList<>();
        final Stack<String> stack = new Stack<>();
        int len = expression.length();
        String token = "";

        for (int i = 0; i < len; i++) {
            if (isBetweenLetters(expression, i, ZERO, NINE)) {
                token = expression.charAt(i) + "";
                while ((i + 1 < len && isBetweenLetters(expression, i + 1, ZERO, NINE))
                        || (i + 1 < len && expression.charAt(i + 1) == '.')) {
                    token = token + expression.charAt(++i);
                }
            } else if (isBetweenLetters(expression, i, CAPITAL_A, CAPITAL_Z) || (isBetweenLetters(expression, i, SMALL_A, SMALL_Z))) {
                token = expression.charAt(i) + "";
                while (i < expression.length() - 1 &&
                        ((isBetweenLetters(expression, i + 1, CAPITAL_A, CAPITAL_Z)) ||
                                (isBetweenLetters(expression, i + 1, SMALL_A, SMALL_Z)))) {
                    token = token + expression.charAt(++i);
                }
                token = CompParser.symbolTable.get(token).getValue() + "";
            } else {
                token = expression.charAt(i) + "";
            }

            switch (token) {
                case "+":
                case "-":
                    while (!stack.isEmpty() && !stack.peek().equals("(")) {
                        queue.addFirst(stack.pop());
                    }
                    stack.push(token);
                    break;
                case "*":
                case "/":
                    while (!stack.isEmpty() && (stack.peek().equals("*") || stack.peek().equals("/"))) {
                        queue.addFirst(stack.pop());
                    }
                    stack.push(token);
                    break;
                case "(":
                    stack.push(token);
                    break;
                case ")":
                    while (!stack.isEmpty() && !(stack.peek().equals("("))) {
                        queue.addFirst(stack.pop());
                    }
                    stack.pop();
                    break;
                default:
                    queue.addFirst(token);
                    break;
            }
        }

        while (!stack.isEmpty()) {
            queue.addFirst(stack.pop());
        }
        final Expression finalExpression = buildExpression(queue);
        final double answer = finalExpression.calculate();
        return Double.parseDouble(String.format("%.3f", answer));
    }

    private static boolean isBetweenLetters(final String expression, final int i,
                                            final char firstLetter, final char secondLetter) {
        return expression.charAt(i) >= firstLetter && expression.charAt(i) <= secondLetter;
    }

    private static Expression buildExpression(final LinkedList<String> queue) {
        Expression returnedExpression;
        Expression right = null;
        Expression left = null;
        String currentExpression = queue.removeFirst();

        if (currentExpression.equals("+") || currentExpression.equals("-") || currentExpression.equals("*")
                || currentExpression.equals("/")) {
            right = buildExpression(queue);
            left = buildExpression(queue);
        }

        switch (currentExpression) {
            case "+":
                returnedExpression = new Add(left, right);
                break;
            case "-":
                returnedExpression = new Subtract(left, right);
                break;
            case "*":
                returnedExpression = new Multiply(left, right);
                break;
            case "/":
                returnedExpression = new Divide(left, right);
                break;
            default:
                returnedExpression = new Number(
                        Double.parseDouble(String.format("%.2f", Double.parseDouble(currentExpression))));
                break;
        }

        return returnedExpression;
    }

}

