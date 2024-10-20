package org.fuzzing;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Random;

/**
 * Fuzzer
 */
public class Fuzzer {
  Random random = new Random();
  final private int maxDepth;

  public Fuzzer() {
    this.maxDepth = 3;
  }

  public Fuzzer(int maxDepth) {
    this.maxDepth = maxDepth;
  }

  public String generateRandomExpr() {
    var op = Operator.getRandom(random);
    var exprNode = new OperatorNode(
        op,
        generateNextNode(maxDepth),
        generateNextNode(maxDepth));
    var root = new CommandNode(CommandToken.SIMPLIFY, exprNode);
    return root.toString();
  }

  private ExpressionNode generateNextNode(int currentDepth) {
    if (random.nextBoolean() && currentDepth > 0) {
      var nextDepth = --currentDepth;
      System.out.println(currentDepth);
      return new OperatorNode(
          Operator.getRandom(random),
          generateNextNode(nextDepth),
          generateNextNode(nextDepth));
    } else {
      return getRandomNumber();
    }
  }

  private NumberNode getRandomNumber() {
    return new NumberNode(random.nextInt(10_000));
  }

  public static void main(String[] args) {
    Fuzzer fuzzer = new Fuzzer(0);
    File file = new File("output.txt");
    try {
      if (file.exists()) {
        Files.delete(file.toPath());
      }
      Files.createFile(file.toPath());
      for (int i = 0; i < 100; i++) {
        Files.writeString(
            file.toPath(),
            fuzzer.generateRandomExpr() + System.lineSeparator(),
            StandardOpenOption.APPEND);
      }
    } catch (IOException ex) {
      System.out.println("Fehler beim Schreiben ins File");
    }
  }
}
