package interpreter;

import commands.*;

import java.util.*;


public class SimulatorParser implements Parser {
    private final Map<String, Command> commandTable = new HashMap<>();
    public static Map<String, Var> symbolTable = new HashMap<>();
    private List<String[]> lines;

    public SimulatorParser(final List<String[]> lines) {
        this.lines = lines;
        commandTable.put("openDataServer", new OpenDataServer());
        commandTable.put("connect", new ConnectCommand());
        commandTable.put("while", new LoopCommand());
    }


    public void parse() {
        for (int i = 0; i < lines.size(); i++) {
            Command c = commandTable.get(lines.get(i)[0]);
            if (c != null)
                if (lines.get(i)[0].equals("while")) {
                    int index = i;
                    while (!lines.get(i)[0].equals("}"))
                        i++;
                    this.parseCondition(new ArrayList<String[]>(lines.subList(index, i)));
                }
            Objects.requireNonNull(c).executeCommand(lines.get(i));
        }
    }

    private void parseCondition(final List<String[]> array) {
        List<Command> commands = new ArrayList<>();
        int i = 0;
        String[] tmp = array.get(i);
        ConditionCommand conditionCommand = (ConditionCommand) commandTable.get(tmp[0]);

        i++;
        for (; i < array.size(); i++) {
            Command com = commandTable.get(array.get(i)[0]);
            if (com != null)
                if (array.get(i)[0].equals("while")) {
                    int index = i;
                    while (!"}".equals(array.get(i)[0]))
                        i++;
                    this.parseCondition(new ArrayList<>(array.subList(index, i)));
                }
            commands.add(com);
        }

    }

}
