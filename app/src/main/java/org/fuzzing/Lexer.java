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
    private boolean isNegative = false;
    private final List<Token> result = new ArrayList<>();

    public Lexer(String line) {
        this.line = line;
    }

    List<Token> tokenizeInput() {
        while (position < line.length()) {
            char singleChar = line.charAt(position);
            switch (singleChar) {
                case '(' -> result.add(Bracket.LEFTBRACKET);
                case ')' -> result.add(Bracket.RIGHTBRACKET);
                case '+' -> result.add(Operator.ADD);
                case '*' -> result.add(Operator.MULTIPLY);
                case '-' -> {
                    if (peek().isPresent() && peek().get() == ' ') {
                        result.add(Operator.SUBTRACT);
                    } else {
                        isNegative = true;
                    }
                }
                case ' ' -> {}
                default -> parseToToken(singleChar);
            }
            position++;
        }
        return result;
    }

    private void parseToToken(char singleChar) {
        var sb = new StringBuilder();
        sb.append(singleChar);
        while (peek().isPresent() && peek().get() != ' ' && Bracket.getFromChar(peek().get()).isEmpty()) {
            position++;
            singleChar = line.charAt(position);
            sb.append(singleChar);
        }
        if (Keyword.getFromInput(sb.toString()).isPresent()) {
            result.add(Keyword.getFromInput(sb.toString()).get());
            return;
        }
        var numOpt = NumberToken.getNumber(sb.toString());
        if (numOpt.isPresent()) {
            result.add(isNegative ? numOpt.get().getNegativeNumber() : numOpt.get());
            isNegative = false;
            return;
        }
        throw new IllegalArgumentException("Could not parse: " + sb);
    }

    private Optional<Character> peek() {
        if (position >= line.length() -2) {
            return Optional.empty();
        }
        return Optional.of(line.charAt(position + 1));
    }
}
