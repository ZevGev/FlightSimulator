package expressions;

import interpreter.CompParser;

import java.util.LinkedList;
import java.util.Stack;

public class ShuntingYardPredicate {

    private static final char ZERO = '0';
    private static final char NINE = '9';
    private static final char SMALLER_THAN = '<';
    private static final char BIGGER_THAN = '>';
    private static final char NOT = '!';
    private static final char EQUAL = '=';
    private static final char CAPITAL_A = 'A';
    private static final char CAPITAL_Z = 'Z';
    private static final char SMALL_A = 'a';
    private static final char SMALL_Z = 'z';

    public static double calc(final String expression) {

        final LinkedList<String> queue = new LinkedList<>();
        final Stack<String> stack = new Stack<>();
        final int len = expression.length();

        String token = "";
        String tmp = null;

        for (int i = 0; i < len; i++) {
            if (isBetweenTwoLetters(expression, i, '0', '9')) {
                token = expression.charAt(i) + "";
                while ((i + 1 < len && expression.charAt(i + 1) >= ZERO && expression.charAt(i + 1) <= NINE)
                        || (i + 1 < len && expression.charAt(i + 1) == '.'))
                    token = token + expression.charAt(++i);
            } else if (isBetweenTwoLetters(expression, i, SMALLER_THAN, BIGGER_THAN) || expression.charAt(i) == NOT) {
                if (expression.charAt(i + 1) == EQUAL) {
                    token = expression.charAt(i) + "";
                    token = token + expression.charAt(++i);
                } else
                    token = expression.charAt(i) + "";
            } else if ((isBetweenTwoLetters(expression, i, CAPITAL_A, CAPITAL_Z)) || (isBetweenTwoLetters(expression, i, SMALL_A, SMALL_Z))) {
                token = expression.charAt(i) + "";
                while (i < expression.length() - 1 && ((isBetweenTwoLetters(expression, i + 1, CAPITAL_A, CAPITAL_Z)) || (isBetweenTwoLetters(expression, i + 1, SMALL_A, SMALL_Z))))
                    token = token + expression.charAt(++i);
                token = CompParser.symbolTable.get(token).getValue() + "";
            } else
                token = expression.charAt(i) + "";


            switch (token) {

                case "+":
                case "-":
                    while (!stack.isEmpty() && !stack.peek().equals("(")) {
                        queue.addFirst(stack.pop());
                    }
                    stack.push(token);
                    break;
                case "||":
                case "&&":
                case "*":
                case "/":
                    while (!stack.isEmpty() && (stack.peek().equals("*") || stack.peek().equals("/"))) {
                        queue.addFirst(stack.pop());
                    }
                    stack.push(token);
                    break;
                case "<":
                case "<=":
                case ">":
                case ">=":
                case "!=":
                case "==":
                    tmp = token;
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
                default: // Always a number
                    queue.addFirst(token);
                    break;
            }
        }
        while (!stack.isEmpty()) {
            queue.addFirst(stack.pop());
        }
        queue.addFirst(tmp);
        final Expression finalExpression = buildExpression(queue);
        final double answer = finalExpression.calculate();
        return Double.parseDouble(String.format("%.3f", answer));
    }

    private static boolean isBetweenTwoLetters(final String expression, final int i,
                                               final char smallerThan, final char biggerThan) {
        return expression.charAt(i) >= smallerThan && expression.charAt(i) <= biggerThan;
    }

    private static Expression buildExpression(final LinkedList<String> queue) {
        Expression returnedExpression = null;
        Expression right = null;
        Expression left = null;
        String currentExpression = queue.removeFirst();
        if (currentExpression.equals("+") || currentExpression.equals("-") || currentExpression.equals("*")
                || currentExpression.equals("/") || currentExpression.equals("<") || currentExpression.equals(">")
                || currentExpression.equals("<=") || currentExpression.equals(">=") || currentExpression.equals("==") ||
                currentExpression.equals("!=")) {
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
            case "<=":
            case ">":
            case ">=":
            case "==":
            case "!=":
            case "<":
                returnedExpression = new PredicateExp(left, right, currentExpression);
                break;
            default:
                returnedExpression = new Number(
                        Double.parseDouble(String.format("%.2f", Double.parseDouble(currentExpression))));
                break;
        }

        return returnedExpression;
    }

}
