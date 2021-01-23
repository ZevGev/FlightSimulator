package commands;

import expressions.Expression;

public class CommandExpression implements Expression {
    private Command command;
    private String[] commandRow;

    public CommandExpression(final Command command) {
        this.command = command;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(final Command command) {
        this.command = command;
    }

    public String[] getCommandRow() {
        return commandRow;
    }

    public void setCommandRow(final String[] commandRow) {
        this.commandRow = commandRow;
    }

    @Override
    public double calculate() {
        command.executeCommand(commandRow);
        return 0;
    }
}
