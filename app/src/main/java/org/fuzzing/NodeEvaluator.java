package org.fuzzing;

/**
 * NodeEvaluator
 */
public class NodeEvaluator {

    static long eval(Node root) {
        if (!(root instanceof KeywordNode keyword)) {
            throw new IllegalArgumentException();
        }
        return switch (keyword.getKeyword()) {
            case Keyword.SIMPLIFY -> keyword.getChild().evaluate();
        };
    }
}
