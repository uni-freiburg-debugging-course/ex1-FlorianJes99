package org.fuzzing;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class App {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("""
          There needs to be an input file specified containing lisp
          statements with the 'simplify' keyword.
          Every line in this file needs to be a single expression.
          """);
            System.exit(1);
            return;
        }
        File file = new File(args[0]);
        if (!file.exists()) {
            System.err.println("Specified file does not exist. Specified: " +
                    file.getAbsolutePath());
            System.exit(1);
            return;
        }
        try {
            Files.readAllLines(file.toPath()).stream()
                    .filter(line -> !line.isBlank())
                    .map(Parser::parse)
                    .map(NodeEvaluator::eval)
                    .forEach(val -> System.out.println(val >= 0
                            ? String.format("%d", val)
                            : String.format("(- %d)", Math.abs(val))));
        } catch (IOException ex) {
            System.err.println("An error occured while reading the provided file");
            System.exit(1);
        }
    }
}
