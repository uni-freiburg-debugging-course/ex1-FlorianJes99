# Build instructions

Duplicate of the [Readme](README.md) section, in case that the readme gets overridden by some github classroom commits (i don't know if this can actually happen).

- jdk-21 is needed

Note: All `./gradlew` commands should be run from the top-level directory.

First build the project with the gradle wrapper.

``` ./gradlew clean build```

#### Run the fuzzer with

```./gradlew runFuzzer```

This generates a file in the [output folder](app/output) containing 10.000 expressions with depth 0 (no nesting).
If something does not work, please create the app/output/ directory yourself. Otherwise the fuzzer won't work at its current state.

#### To run the evaluator of this expressions use

```./gradlew run --args=path/to/my/file```

Note that the program is run from the [Root Directory]().
So when specifying the relative path keep that in mind.

#### Examples:

```./gradlew run --args=app/output/output.smt2``` (only works if runFuzzer previously generated some input)

```./gradlew run --args=simp.smt2``` (run the given simple test file)

#### Using z3 for comparison

When using z3 to compare, put the -q flag to the ./gradlew command to prevent some output 'spam' by gradle to the terminal,
which would cause the z3 comparison to fail.
```z3 app/output/output.smt2 > res1 && ./gradlew run -q --args=app/output/output.smt2 > res2 && cmp res1 res2 && echo $?```
