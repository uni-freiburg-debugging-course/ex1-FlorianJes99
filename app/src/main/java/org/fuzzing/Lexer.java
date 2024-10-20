package org.fuzzing;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Lexer
 */
public class Lexer {

  String line;
  int position;

  public Lexer(String line) {
    this.line = line;
    position = 0;
  }

  public List<Token> tokenizeInput() {
    List<Token> result = new ArrayList<>();
    while (position < line.length()) {
      char basic = line.charAt(position);
      var singleCharacterToken = getFromSingleChar(basic);
      if (singleCharacterToken.isPresent()) {
        result.add(singleCharacterToken.get());
        position++;
      } else if (Operator.getFromChar(basic).isPresent()) {
        result.add(Operator.getFromChar(basic).get());
        position++;
      } else if (basic != ' ') {
        var sb = new StringBuilder();
        while (position < line.length() && basic != ' ' && Bracket.getFromChar(basic).isEmpty()) {
          sb.append(basic);
          position++;
          basic = line.charAt(position);
        }
        var maybeOther = CommandToken.getFromInput(sb.toString())
            .or(() -> NumberToken.getNumber(sb.toString()));
        if (maybeOther.isEmpty()) {
          System.err.println("Error: Failed Parsing String: " + sb.toString());
        } else {
          result.add(maybeOther.get());
        }
      } else {
        position++;
      }
    }
    return result;
  }

  private Optional<Token> getFromSingleChar(char input) {
    return Bracket.getFromChar(input)
        .or(() -> Operator.getFromChar(input));
  }
}
