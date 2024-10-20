/*
 * This source file was generated by the Gradle 'init' task
 */
package org.fuzzing;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AppTest {
    @Test void checkExprParse() {
      var input = "(simplify (+ 2 5))";
      assertEquals(7, NodeEvaluator.eval(Parser.parse(input)));
    }
}
