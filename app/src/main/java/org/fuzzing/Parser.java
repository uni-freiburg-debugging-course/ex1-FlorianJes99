package org.fuzzing;

import java.util.List;

/**
 * Parser
 */
public class Parser {

    private final List<Token> tokens;

    private Parser(String line) {
        this.tokens = new Lexer(line).tokenizeInput();
    }


    static Node parse(String line) {
        return new Parser(line).parseExpr();
    }

    private Node parseExpr() {
        return switch (tokens.removeFirst()) {
            case Bracket bracket -> {
                if (bracket == Bracket.RIGHTBRACKET) {
                    System.out.println("Test");
                    throw new IllegalArgumentException();
                }
                yield parseExpr();
            }
            case NumberToken num -> new NumberNode(num.value);
            case Operator op -> computeTree(new OperatorNode(op));
            case Keyword comm -> computeTree(new KeywordNode(comm));
        };
    }

    private NestedNode computeTree(NestedNode node) {
        while (!tokens.getFirst().equals(Bracket.RIGHTBRACKET)) {
            var expr = parseExpr();
            if (!(expr instanceof ExpressionNode exprNode)) {
                throw new IllegalArgumentException();
            }
            node.addChild(exprNode);
        }
        tokens.removeFirst();
        return node;
    }
}
