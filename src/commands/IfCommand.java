package commands;

public class IfCommand extends ConditionCommand {

    @Override
    public void executeCommand(final String[] commandRow) {
        PredicateCommand command = (PredicateCommand) commands.get(0).getCommand();
        commands.get(0).calculate();

        if (command.getBoolean() != 0) {
            for (int i = 1; i < commands.size(); i++) {
                commands.get(i).calculate();
            }
            commands.get(0).calculate();
        }
    }
}
