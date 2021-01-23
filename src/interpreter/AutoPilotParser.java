package interpreter;

import java.util.List;

public class AutoPilotParser {
    CompParser parser;
    public static volatile boolean stop = true;
    public static volatile boolean close = false;
    public static Thread thread;
    public int i = 0;

    public AutoPilotParser(CompParser parser) {
        this.parser = parser;
    }

    public void parse() {
        parser.parse();
        i = 0;
    }

    public void execute() {
        thread = new Thread(() -> {
            while (!close) {
                while (!stop && i < parser.expressions.size()) {
                    parser.expressions.get(i).calculate();
                    i++;
                }
            }
        });

        thread.start();
    }

    public void add(List<String[]> lines) {
        parser.lines.clear();
        parser.lines.addAll(lines);
        CompParser.symbolTable.put("stop", new Var(1));
        for (String[] s : parser.lines) {
            if (s[0].equals("while")) {
                s[s.length - 2] = s[s.length - 2] + "&&stop!=0";
            }
        }
    }

    public void stop() {
        Var var = CompParser.symbolTable.get("stop");
        if (var != null) {
            var.setValue(0);
        }
        AutoPilotParser.stop = true;
    }

    public void Continue() {
        CompParser.symbolTable.get("stop").setValue(1);
    }
}
