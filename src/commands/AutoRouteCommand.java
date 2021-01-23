package commands;

import model.Model;

public class AutoRouteCommand implements Command {

    @Override
    public void executeCommand(final String[] commandRow) {
        Model.turn = false;
    }

}
