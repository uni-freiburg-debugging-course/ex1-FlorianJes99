package org.fuzzing;

/**
 * NodeEvaluator
 */
public class NodeEvaluator {

    static int eval(Node root) {
        if (!(root instanceof CommandNode command)) {
            throw new IllegalArgumentException();
        }
        return switch (command.getCommand()) {
            case CommandToken.SIMPLIFY -> command.getChild().evaluate();
        };
    }
}
