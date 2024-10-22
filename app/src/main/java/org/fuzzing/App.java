package org.fuzzing;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class App {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("""
          Es muss genau ein Argument angegeben werden.
          Dies muss eine Datei mit je einem einfachen LISP-artigen
          Ausdruck pro Zeile sein.
          """);
            System.exit(1);
            return;
        }
        File file = new File(args[0]);
        if (!file.exists()) {
            System.err.println("Specified file does not exist. Expected: " +
                    file.getAbsolutePath());
            System.exit(1);
            return;
        }
        try {
            Files.readAllLines(file.toPath()).stream()
                    .filter(line -> !line.isBlank())
                    .map(Parser::parse)
                    //.peek(System.out::println)
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
