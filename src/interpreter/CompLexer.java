package interpreter;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CompLexer<V> implements Lexer {
    private static final String EQUAL = "=";
    private Scanner scan;
    private final List<String[]> lines = new ArrayList<>();
    private String[] arr = null;

    public CompLexer(String[] s) {
        arr = s;
    }

    public List<String[]> lexicalCheck() {
        if (arr != null) {
            for (String s : arr) {
                lines.add(s.replaceFirst(EQUAL, " = ").replaceFirst("\t", "").split("\\s+"));
            }

        } else {
            while (scan.hasNextLine()) {
                lines.add(scan.nextLine().replaceFirst(EQUAL, " = ").replaceFirst("\t", "").split("\\s+"));
            }
        }
        return lines;
    }
}
