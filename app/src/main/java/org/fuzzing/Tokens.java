package org.fuzzing;

import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

/**
 * Tokens
 */
sealed interface Token permits Bracket, NumberToken, CommandToken, Operator {
}

enum Operator implements Token {
  MULTIPLY('*'),
  SUBTRACT('-'),
  ADD('+');

  char symbol;

  private Operator(char symbol) {
    this.symbol = symbol;
  }

  public static Optional<Token> getFromChar(char input) {
    return Arrays.stream(Operator.values())
        .filter(op -> op.symbol == input)
        .findFirst()
        .map(op -> (Token) op);
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

  char symbol;

  private Bracket(char symbol) {
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

enum CommandToken implements Token {
  SIMPLIFY("simplify");

  String command;

  private CommandToken(String command) {
    this.command = command;
  }

  public static Optional<Token> getFromInput(String input) {
    return Arrays.stream(CommandToken.values())
        .filter(command -> command.command.equals(input))
        .findFirst()
        .map(token -> (Token) token);
  }
}

final class NumberToken implements Token {
  int value;

  private NumberToken(int value) {
    this.value = value;
  }

  public static Optional<Token> getNumber(String value) {
    try {
      return Optional.of(new NumberToken(Integer.parseInt(value)));
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  @Override
  public String toString() {
    return "" + value;

  }
}
