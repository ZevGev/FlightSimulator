package commands;

import expressions.ShuntingYard;
import interpreter.CompParser;
import interpreter.Var;

import java.util.Optional;

public class AssignCommand implements Command {

    private static final String BIND_STRING = "bind";

    @Override
    public void executeCommand(final String[] commandRow) {
        if (Optional.ofNullable(commandRow).isEmpty()) {
            return;
        }

        final Var firstVar = CompParser.symbolTable.get(commandRow[0]);

        if (BIND_STRING.equals(commandRow[2])) {
            setValueIfNeeded(commandRow);
            CompParser.symbolTable.get(commandRow[3]).addObserver(firstVar);
            firstVar.addObserver(CompParser.symbolTable.get(commandRow[3]));
        } else {
            final StringBuilder stringBuilder = new StringBuilder();

            for (int i = 2; i < commandRow.length; i++) {
                stringBuilder.append(commandRow[i]);
            }

            final double tempCalculation = ShuntingYard.calc(stringBuilder.toString());

            final String location = firstVar.getLocation();

            if (location != null) {
                ConnectCommand.printWriter.println("set " + location + " " + tempCalculation);
                ConnectCommand.printWriter.flush();
                System.out.println("set " + location + " " + tempCalculation);
            } else {
                firstVar.setValue(tempCalculation);
            }
        }
    }

    private void setValueIfNeeded(final String[] commandRow) {
        Optional.of(CompParser.symbolTable.get(commandRow[0]).getValue())
                .filter(val -> !val.equals(CompParser.symbolTable.get(commandRow[3]).getValue()))
                .ifPresent(value -> CompParser.symbolTable.get(commandRow[0]).setValue(CompParser.symbolTable.get(commandRow[3]).getValue()));
    }
}
