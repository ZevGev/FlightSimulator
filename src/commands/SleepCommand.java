package commands;

import expressions.ShuntingYard;

public class SleepCommand implements Command {
    @Override
    public void executeCommand(final String[] commandRow) {
        try {
            Thread.sleep((long) ShuntingYard.calc(commandRow[1]));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
