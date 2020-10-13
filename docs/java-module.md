# Java Module

This package is so light on dependencies that I have not bothered modifying the
Maven build to "properly" enable module support. Instead, I simply put the
`module-info.java` file in `src/main/java11` and manually compiled it into the
`src/main/resources` directory so that it gets included in the JAR file.

```bash
javac -source 11 -target 11 -encoding utf8 \
      -d src/main/resources \
      -sourcepath src/main/java:src/main/java11 \
      src/main/java11/module-info.java
```

The only non-core dependency in the production code is the `@Generated`
annotation on the `CountryCodesData` class. This annotation has "source"
retention, and therefore has no effect on the compiled module.

## Testing

Here's a simple testing project, ran using JDK 14.0.2:

```text
.
+-- module/
|   +-- iban-1.7.0.jar
+-- src/
|   +-- module-info.java
|   +-- com/
|       +-- example/
|           +-- Main.java
+-- target/
```

```java
// module-info.java
module com.example {
  requires nl.garvelink.iban;
}
```

```java
// Main.java
package com.example;

import nl.garvelink.iban.*;

public class Main {
  public static void main(String[] args) {
    if (args.length < 1) {
      System.err.println("Need at least one command-line argument");
      System.exit(1);
    }
    for (String arg : args) {
      try {
        IBAN iban = IBAN.valueOf(arg);
        System.out.println("Valid: " + arg + " -> " + iban);
      } catch (Exception e) {
        System.out.println("Not valid: " + arg + " -> " + e.toString());
      }
    }
  }
}
```

Compile and test:
```bash
javac -d target -source 11 -target 11 --source-path src --module-path ./module **/*.java
java --module-path ./module/:./target --module com.example/com.example.Main NL0012345
```
