package org.fuzzing;

/**
 * Node
 */
sealed interface Node permits ExpressionNode, NestedNode {
}

sealed interface ExpressionNode extends Node permits NumberNode, OperatorNode {
    long evaluate();
}

sealed interface NestedNode extends Node {
    void addChild(ExpressionNode node);
}

record NumberNode(int number) implements ExpressionNode {
    @Override
    public long evaluate() {
        return number();
    }

    @Override
    public String toString() {
        return String.format("%d", number());
    }
}

final class KeywordNode implements NestedNode {
    ExpressionNode child = null;
    Keyword keyword;

    public KeywordNode(Keyword keyword) {
        this.keyword = keyword;
    }

    public KeywordNode(Keyword keyword, ExpressionNode child) {
        this.keyword = keyword;
        this.child = child;
    }

    @Override
    public void addChild(ExpressionNode child) {
        if (this.child != null) {
            throw new IllegalArgumentException();
        }
        this.child = child;
    }

    public Keyword getKeyword() {
        return keyword;
    }

    public ExpressionNode getChild() {
        return child;
    }

    @Override
    public String toString() {
        return String.format("( %s %s )", keyword.toString().toLowerCase(), child.toString());
    }
}

final class OperatorNode implements ExpressionNode, NestedNode {
    ExpressionNode leftChild = null;
    ExpressionNode rightChild = null;
    Operator operator;

    public OperatorNode(Operator operator) {
        this.operator = operator;
    }

    public OperatorNode(Operator operator, ExpressionNode left, ExpressionNode right) {
        this.operator = operator;
        this.leftChild = left;
        this.rightChild = right;
    }

    @Override
    public void addChild(ExpressionNode child) {
        if (leftChild == null) {
            leftChild = child;
            return;
        }
        if (rightChild == null) {
            rightChild = child;
            return;
        }
        throw new IllegalArgumentException();
    }

    @Override
    public long evaluate() {
        return switch (operator) {
            case Operator.ADD -> leftChild.evaluate() + rightChild.evaluate();
            case Operator.SUBTRACT -> leftChild.evaluate() - rightChild.evaluate();
            case Operator.MULTIPLY -> leftChild.evaluate() * rightChild.evaluate();
        };
    }

    @Override
    public String toString() {
        return String.format("( %s %s %s )", operator.symbol, leftChild.toString(), rightChild.toString());
    }
}
