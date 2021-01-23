package interpreter;

import java.util.List;

public interface Lexer<V> {
    List<String[]> lexicalCheck();
}
