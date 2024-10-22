package org.fuzzing;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Lexer
 */
public class Lexer {

    private final String line;
    private int position = 0;

    public Lexer(String line) {
        this.line = line;
    }

    List<Token> tokenizeInput() {
        List<Token> result = new ArrayList<>();
        while (position < line.length()) {
            char singleChar = line.charAt(position);
            var bracket = getBracket(singleChar);
            if (bracket.isPresent()) {
                result.add(bracket.get());
                position++;
            } else if (Operator.getFromChar(singleChar).isPresent()) {
                result.add(Operator.getFromChar(singleChar).get());
                position++;
            } else if (singleChar != ' ') {
                var sb = new StringBuilder();
                while (position < line.length() && singleChar != ' ' && Bracket.getFromChar(singleChar).isEmpty()) {
                    sb.append(singleChar);
                    position++;
                    singleChar = line.charAt(position);
                }
                var maybeOther = Keyword.getFromInput(sb.toString())
                        .or(() -> NumberToken.getNumber(sb.toString()));
                if (maybeOther.isEmpty()) {
                    System.err.println("Error: Failed Parsing String: " + sb);
                } else {
                    result.add(maybeOther.get());
                }
            } else {
                position++;
            }
        }
        return result;
    }

    private Optional<Token> getBracket(char input) {
        return Bracket.getFromChar(input)
                .or(() -> Operator.getFromChar(input));
    }
}
