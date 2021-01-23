package commands;

import expressions.ShuntingYard;
import interpreter.CompParser;

public class ReturnCommand implements Command {

    @Override
    public void executeCommand(final String[] commandRow) {
        final StringBuilder stringBuilder = new StringBuilder();

        for (int i = 1; i < commandRow.length; i++) {
            stringBuilder.append(commandRow[i]);
        }

        CompParser.returnValue = ShuntingYard.calc(stringBuilder.toString());
    }
}
