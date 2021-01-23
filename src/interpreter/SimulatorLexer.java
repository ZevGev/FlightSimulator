package interpreter;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SimulatorLexer<V> implements Lexer {

    private Scanner scanner;
    private final List<String[]> lines = new ArrayList<>();

    public SimulatorLexer(final String v) {
        scanner = new Scanner(v);
    }

    public SimulatorLexer(final V v) {
        scanner = new Scanner((Readable) v);
    }

    public List<String[]> lexicalCheck() {
        while (scanner.hasNextLine()) {
            lines.add(scanner.nextLine().split(" "));
        }
        return lines;
    }
}
