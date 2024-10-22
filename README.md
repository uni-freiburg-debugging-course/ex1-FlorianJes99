[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/3MmVbb7f)
# Debugging 24/25 Exercise 1

Detailed instructions are in the exercise sheet. Following are your checkpoints:

- [ ] Implement parser and evaluator
- [ ] Implement a fuzzer
- [ ] Generate *lots* of random instances with your fuzzer. Your evaluator and z3 must return the same result on generated instances
- [ ] Provide detailed build instructions for your code so that we can evaluate it

## Build instructions

- jdk-21 is needed

Note: All `./gradlew` commands should be run from the top-level directory.

First build the project with the gradle wrapper.

``` ./gradlew clean build```

#### Run the fuzzer with 

```./gradlew runFuzzer```

This generates a file in the [output folder](app/output) containing 10.000 expressions with depth 0 (no nesting).

#### To run the evaluator of this expressions use

```./gradlew run --args=path/to/my/file```

Note that the program is ran from the [App](app) directory. 
So when specifying the relative path keep that in mind.

#### Examples:

```./gradlew run --args=output/output.smt2``` (only works if runFuzzer previously generated some input)

```./gradlew run --args=../simp.smt2``` (run the given simple test file)

#### Using z3 for comparison

When using z3 to compare, put the -q flag to the ./gradlew command to prevent some output 'spam' by gradle to the terminal, 
which would cause the z3 comparison to fail.
```z3 app/output/output.smt2 > res1 && ./gradlew run -q --args=output/output.smt2 > res2 && cmp res1 res2 && echo $?```