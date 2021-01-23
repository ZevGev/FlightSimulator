package commands;

import expressions.ConditionBuilder;


public class PredicateCommand implements Command {
    double value;

    public double getBoolean() {
        return value;
    }

    public void setBoolean(final double value) {
        this.value = value;
    }

    @Override
    public void executeCommand(final String[] commandRow) {
        final StringBuilder stringBuilder = new StringBuilder();

        for (int i = 1; i < commandRow.length - 1; i++) {
            stringBuilder.append(commandRow[i]);
        }

        value = ConditionBuilder.calc(stringBuilder.toString());
    }
}
