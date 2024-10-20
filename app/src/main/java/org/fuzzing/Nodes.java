package org.fuzzing;

/**
 * Node
 */
sealed interface Node permits ExpressionNode, NestedNode {
}

sealed interface ExpressionNode extends Node permits NumberNode, OperatorNode {
  int evaluate();
}

sealed interface NestedNode extends Node {
  void addChild(ExpressionNode node);
}

record NumberNode(int number) implements ExpressionNode {
  @Override
  public int evaluate() {
    return number();
  }

  @Override
  public String toString() {
    return String.format("%d", number());
  }
}

final class CommandNode implements NestedNode {
  // List<ExpressionNode> children = new ArrayList<>();
  ExpressionNode child = null;
  CommandToken command;

  public CommandNode(CommandToken command) {
    this.command = command;
  }

  public CommandNode(CommandToken command, ExpressionNode child) {
    this.command = command;
    this.child = child;
  }

  @Override
  public void addChild(ExpressionNode child) {
    if (this.child != null) {
      throw new IllegalArgumentException();
    }
    this.child = child;
  }

  public CommandToken getCommand() {
    return command;
  }

  public ExpressionNode getChild() {
    return child;
  }

  @Override
  public String toString() {
    return String.format("( %s %s )", command.toString().toLowerCase(), child.toString());
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
  public int evaluate() {
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
