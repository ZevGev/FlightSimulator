package commands;

public class DisconnectCommand implements Command {

    public static final String BYE = "bye";

    @Override
    public void executeCommand(final String[] commandRow) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        OpenDataServer.stop = true;
        ConnectCommand.stop = true;
        System.out.println(BYE);
    }
}
