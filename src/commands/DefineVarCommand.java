package commands;

import expressions.ShuntingYard;
import interpreter.CompParser;
import interpreter.Var;

public class DefineVarCommand implements Command {

    public static final String BIND = "bind";

    @Override
    public void executeCommand(final String[] commandRow) {
        if (commandRow.length == 0) {
            return;
        }

        Var var = new Var();

        if (commandRow.length > 2) {
            if (BIND.equals(commandRow[3])) {
                CompParser.symbolTable.put(commandRow[1], CompParser.symbolTable.get(commandRow[4]));
            } else {
                StringBuilder stringBuilder = new StringBuilder();

                for (int i = 3; i < commandRow.length; i++){
                    stringBuilder.append(commandRow[i]);
                }

                var.setValue(ShuntingYard.calc(stringBuilder.toString()));
                CompParser.symbolTable.put(commandRow[1], var);
            }
        } else
            CompParser.symbolTable.put(commandRow[1], new Var());

    }

}
