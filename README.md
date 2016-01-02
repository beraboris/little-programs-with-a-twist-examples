Little programs with a twist
============================

This is a set of hopefully realistic examples that show the usage of the Option,
Either, Eventually (Future), and Collection monads.

These examples were written in combination with a blog article I wrote
explaining monads. (TODO: add article url) The article itself looks into the
high level concepts and does not go into examples. This repository is meant to
serve as a set examples for the article.

Using the examples
------------------

To run, build and play with these examples, you need to have java and sbt
installed. Here are the steps to follow:

1. If it's not already done, install java
1. Download and install sbt (http://www.scala-sbt.org/download.html)
1. Clone this repository

    ```
    $ git clone https://github.com/dotboris/little-programs-with-a-twist
    ```

1. Start the sbt console

    ```
    $ cd little-programs-with-a-twist
    $ sbt
    ```

Once you're in the sbt console, there's a number of things you can do.

### Run all the examples

This one is pretty simple:

```
> run
[info] Compiling 4 Scala sources to /home/boris/code/examples/little-programs-with-a-twist-examples/target/scala-2.11/classes...
[info] Running littleprograms.App
Looking for the biggest contributor of dotboris's most popular repository using Option
And the winner is...
Some(dotboris/eldritch: kerrizor)
```

### Play with the code

With sbt, you can launch a REPL that lets you run code interactively. This is
the perfect tool for you to play around with the code and test things out.

```
> console
[info] Starting scala interpreter...
[info]
Welcome to Scala version 2.11.7 (Java HotSpot(TM) 64-Bit Server VM, Java 1.8.0_66).
Type in expressions to have them evaluated.
Type :help for more information.

scala> import littleprograms._
import littleprograms._

scala>
```

From here, you can just run whatever code you want and the REPL will print out
the results on the next line.
