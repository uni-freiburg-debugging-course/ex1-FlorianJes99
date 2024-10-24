package org.fuzzing;

import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

/**
 * Tokens
 */
sealed interface Token permits Bracket, NumberToken, Keyword, Operator {
}

enum Operator implements Token {
    MULTIPLY('*'),
    SUBTRACT('-'),
    ADD('+');

    final char symbol;

    Operator(char symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return Character.toString(this.symbol);
    }

    public static Operator getRandom(Random random) {
        var op = Operator.values();
        return op[random.nextInt(op.length)];
    }
}

enum Bracket implements Token {
    LEFTBRACKET('('),
    RIGHTBRACKET(')');

    final char symbol;

    Bracket(char symbol) {
        this.symbol = symbol;
    }

    public static Optional<Token> getFromChar(char input) {
        if (input == '(')
            return Optional.of(LEFTBRACKET);
        if (input == ')')
            return Optional.of(RIGHTBRACKET);
        return Optional.empty();
    }
}

enum Keyword implements Token {
    SIMPLIFY("simplify");

    final String keywordString;

    Keyword(String keywordString) {
        this.keywordString = keywordString;
    }

    public static Optional<Keyword> getFromInput(String input) {
        return Arrays.stream(Keyword.values())
                .filter(keyword -> keyword.keywordString.equals(input))
                .findFirst();
    }
}

final class NumberToken implements Token {
    int value;

    private NumberToken(int value) {
        this.value = value;
    }

    public static Optional<NumberToken> getNumber(String value) {
        try {
            return Optional.of(new NumberToken(Integer.parseInt(value)));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public NumberToken getNegativeNumber() {
        return new NumberToken(-value);
    }

    @Override
    public String toString() {
        return "" + value;

    }
}
