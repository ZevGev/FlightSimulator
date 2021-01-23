package commands;

import interpreter.CompParser;

public class PrintCommand implements Command {

    @Override
    public void executeCommand(final String[] commandRow) {
        for (int i = 1; i < commandRow.length; i++) {
            if (CompParser.symbolTable.containsKey(commandRow[i])) {
                System.out.print(commandRow[i] + CompParser.symbolTable.get(commandRow[i]).getValue());
            }
            else {
                System.out.print(commandRow[i]);
            }
        }
        System.out.println("");
    }
}
