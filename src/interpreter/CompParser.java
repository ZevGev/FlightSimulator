package interpreter;

import commands.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class CompParser implements Parser {
    private final HashMap<String, CommandExpression> commandTable = new HashMap<>();
    private final GenericFactory commandFactory = new GenericFactory();
    public static Map<String, Var> symbolTable;
    public final List<String[]> lines;
    public List<CommandExpression> expressions;
    public static double returnValue;
    public static ArrayList<String> vars;

    public CompParser(final List<String[]> lines) {
        this.expressions = new ArrayList<>();
        this.lines = lines;
        symbolTable = new HashMap<>();
        commandFactory.insertProduct("openDataServer", OpenDataServer.class);
        commandFactory.insertProduct("connect", ConnectCommand.class);
        commandFactory.insertProduct("while", LoopCommand.class);
        commandFactory.insertProduct("var", DefineVarCommand.class);
        commandFactory.insertProduct("return", ReturnCommand.class);
        commandFactory.insertProduct("=", AssignCommand.class);
        commandFactory.insertProduct("disconnect", DisconnectCommand.class);
        commandFactory.insertProduct("print", PrintCommand.class);
        commandFactory.insertProduct("sleep", SleepCommand.class);
        commandFactory.insertProduct("predicate", PredicateCommand.class);
        commandFactory.insertProduct("autoroute", AutoRouteCommand.class);
        commandFactory.insertProduct("if", IfCommand.class);
        commandTable.put("openDataServer", new CommandExpression(new OpenDataServer()));
        commandTable.put("connect", new CommandExpression(new ConnectCommand()));
        commandTable.put("while", new CommandExpression(new LoopCommand()));
        commandTable.put("var", new CommandExpression(new DefineVarCommand()));
        commandTable.put("return", new CommandExpression(new ReturnCommand()));
        commandTable.put("=", new CommandExpression(new AssignCommand()));
        commandTable.put("disconnect", new CommandExpression(new DisconnectCommand()));
        Scanner scanner = null;

        try {
            scanner = new Scanner(new BufferedReader(new FileReader("simulator_vars.txt")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        vars = new ArrayList<>();

        Optional.ofNullable(scanner).ifPresent(s -> {
            while (s.hasNext()) {
                vars.add(s.nextLine());
            }
        });

        for (String str : vars) {
            symbolTable.put(str, new Var(str));
        }
    }

    private Command parseCondition(final List<String[]> commandsList) {
        ConditionCommand conditionCommand = (ConditionCommand) commandFactory.getNewProduct(commandsList.get(0)[0]);
        int i = 0;
        List<CommandExpression> tmp = new ArrayList<>();
        CommandExpression cmdTmp = new CommandExpression((Command) commandFactory.getNewProduct("predicate"));
        cmdTmp.setCommandRow(commandsList.get(0));
        tmp.add(cmdTmp);
        conditionCommand.setCommands(tmp);
        conditionCommand.getCommands().addAll(1, this.parseCommands(new ArrayList<>(commandsList.subList(i + 1, commandsList.size()))));
        return conditionCommand;
    }


    private List<CommandExpression> parseCommands(final List<String[]> commandList) {
        List<CommandExpression> commands = new ArrayList<>();

        for (int i = 0; i < commandList.size(); i++) {
            CommandExpression expression = new CommandExpression((Command) commandFactory.getNewProduct(commandList.get(i)[0]));
            if (expression.getCommand() != null) {
                if (commandList.get(i)[0].equals("while") || commandList.get(i)[0].equals("if")) {
                    int index = i;
                    i += findCloser(new ArrayList<>(commandList.subList(i + 1, commandList.size()))) + 1;
                    expression.setCommand(this.parseCondition(new ArrayList<>(commandList.subList(index, i))));
                }
            } else {

                expression = new CommandExpression((Command) commandFactory.getNewProduct(commandList.get(i)[1]));
            }
            expression.setCommandRow(commandList.get(i));
            commands.add(expression);
        }
        return commands;
    }

    private int findCloser(final List<String[]> commandList) {
        final Stack<String> stack = new Stack<>();
        stack.push("{");

        for (int i = 0; i < commandList.size(); i++) {
            if (commandList.get(i)[0].equals("while") || commandList.get(i)[0].equals("if"))
                stack.push("{");
            if (commandList.get(i)[0].equals("}")) {
                stack.pop();
                if (stack.isEmpty())
                    return i;
            }
        }
        return 0;
    }


    public void parse() {
        this.expressions = this.parseCommands(lines);

    }

    public double execute() {
        for (CommandExpression e : expressions) {
            e.calculate();
        }
        return returnValue;
    }


}
