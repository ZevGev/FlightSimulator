package commands;

public class LoopCommand extends ConditionCommand {

    @Override
    public void executeCommand(final String[] commandRow) {
        PredicateCommand command = (PredicateCommand) commands.get(0).getCommand();
        commands.get(0).calculate();

        while (command.getBoolean() != 0) {
            for (int i = 1; i < commands.size(); i++) {
                commands.get(i).calculate();
            }
            commands.get(0).calculate();
        }
    }
}
