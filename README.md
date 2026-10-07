# MicroJava Compiler

A compiler for the **MicroJava programming language**, implemented in Java.

The compiler processes MicroJava source code through lexical, syntax, and semantic analysis, performs symbol-table and type checking, and generates executable bytecode for the **MicroJava Virtual Machine**.

## Features

- Lexical analysis using **JFlex**
- Syntax analysis using **AST-CUP**
- Abstract Syntax Tree (AST) generation
- Semantic analysis using the Visitor pattern
- Symbol table management
- Type checking
- MicroJava bytecode generation
- Error detection and reporting
- Support for arrays and built-in functions
- Conditional statements and loops
- User-defined methods and parameter checking
- Classes and inheritance
- Abstract classes and methods
- Method overriding
- Object creation
- Polymorphic method calls
- Short-circuit evaluation
- Ternary expressions
- `findAny` array operation
- `map` array operation

## Compiler Pipeline

```text
MicroJava Source (.mj)
        │
        ▼
   Lexical Analysis
       JFlex
        │
        ▼
   Syntax Analysis
      AST-CUP
        │
        ▼
 Abstract Syntax Tree
        │
        ▼
  Semantic Analysis
        │
        ├── Symbol Table
        ├── Type Checking
        └── Context Checks
        │
        ▼
  Bytecode Generation
        │
        ▼
 MicroJava Object File (.obj)
        │
        ▼
 MicroJava Virtual Machine
```

## Language Support

The implementation includes functionality across multiple levels of the MicroJava language specification.

### Core Language Features

- Constants and variables
- Integer, character, and boolean types
- Arithmetic expressions
- Assignment
- Increment and decrement operators
- `read` and `print`
- One-dimensional arrays
- Array `length`
- Built-in functions
- Ternary operator

### Control Flow

- `if / else`
- Logical operators `&&` and `||`
- Short-circuit evaluation
- `for` loops
- `do / while` loops
- `break`
- `continue`
- Labeled loop control

### Methods

- Global methods
- Formal and actual parameters
- Return values
- Nested method calls
- Parameter type checking

### Object-Oriented Features

- Classes
- Abstract classes
- Fields and methods
- Inheritance
- Method overriding
- Object creation
- Arrays of objects
- Member access
- Substitution
- Polymorphic method calls

### Additional Operations

#### `findAny`

Searches an array for a value and stores the result in a boolean variable.

#### `map`

Creates a transformed array by applying an expression to each element of a source array.


## Main Components

### `Compiler`

The main entry point of the compiler.

It:

1. Loads a MicroJava source file
2. Starts the lexer and parser
3. Generates the abstract syntax tree
4. Performs semantic analysis
5. Prints the symbol table
6. Generates MicroJava bytecode
7. Writes the resulting `.obj` file

### `SemanticAnalyzer`

Performs semantic analysis by traversing the AST.

Responsibilities include:

- Symbol-table management
- Scope handling
- Declaration checking
- Type checking
- Method parameter validation
- Assignment compatibility
- Class inheritance
- Method overriding
- Abstract class validation
- Object and array validation
- `findAny` validation
- `map` validation

### `CodeGenerator`

Traverses the AST and generates executable MicroJava bytecode.

It handles:

- Arithmetic operations
- Assignments
- Arrays
- Method calls
- `read` and `print`
- Conditional statements
- Loops
- `break` and `continue`
- Ternary expressions
- Object creation
- Virtual method tables
- Polymorphic method calls
- `findAny`
- `map`

### `Yylex`

The lexical analyzer generated from the JFlex specification.

It recognizes MicroJava tokens and forwards them to the parser.

### `MJParser`

The syntax analyzer generated from the AST-CUP grammar.

It parses the token stream and constructs the Abstract Syntax Tree.

## Technologies

- **Java**
- **JFlex**
- **AST-CUP / CUP**
- **Apache Ant**
- **Visitor Pattern**
- **MicroJava VM**
- **Log4j**

## Building and Running

The project uses **Apache Ant** and the `build.xml` file located in the project root.

### Generate the Lexer

```bash
ant lexerGen
```

Generates `Yylex.java` from:

```text
spec/mjlexer.flex
```

### Generate the Parser and AST

```bash
ant parserGen
```

Generates:

- `MJParser.java`
- `sym.java`
- AST classes

from:

```text
spec/mjparser.cup
```

### Generate All Compiler Sources

```bash
ant repackage
```

Generates the lexer and parser and adjusts the package structure of the generated AST classes.

### Compile the Project

```bash
ant compile
```

Generates the required Java sources and compiles the project into the `bin` directory.

### Compile a MicroJava Program

```bash
ant compileProgram
```

If lexical, syntax, and semantic analysis complete successfully, the compiler generates a MicroJava `.obj` file.

A specific input and output file can also be provided:

```bash
ant compileProgram -Dsource.file=test/test302.mj -Dobj.file=test/test302.obj
```

### View Generated Bytecode

```bash
ant disasm
```

Compiles the selected MicroJava program and displays the generated bytecode.

### Run on the MicroJava VM

```bash
ant runObj
```

Compiles the program, displays the generated bytecode, and executes the resulting `.obj` file on the MicroJava Virtual Machine.

### Run in a Separate Terminal

```bash
ant runTerminal
```

Example:

```bash
ant runTerminal -Dsource.file=test/test302.mj -Dobj.file=test/test302.obj
```

This is particularly useful for programs that use `read`.

### Debug

```bash
ant debug
```

Runs the generated object file in debug mode.

### Clean Generated Files

```bash
ant clean
```

Removes generated lexer, parser, AST, and compiled `.class` files.

## Testing

The project contains tests covering both complete language levels and individual compiler features.

The test suite includes cases for:

- Arithmetic expressions
- Variables and constants
- Arrays
- `findAny`
- `map`
- Ternary expressions
- `if / else`
- Short-circuit evaluation
- `for` loops
- `do / while` loops
- `break` and `continue`
- Methods
- Method parameters
- Classes
- Inheritance
- Abstract classes
- Polymorphism

The compiler reports lexical, syntax, and semantic errors before bytecode generation.

## Output

For a valid MicroJava source program, the compiler produces:

```text
program.mj
     │
     ▼
MicroJava Compiler
     │
     ▼
program.obj
```

The generated `.obj` file can then be executed using the MicroJava Virtual Machine.
