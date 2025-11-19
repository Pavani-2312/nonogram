#!/bin/bash

# Nonogram Compilation Script
echo "Compiling Nonogram Game..."

# Create bin directory if it doesn't exist
mkdir -p bin

# Compile all Java files including Main.java
javac -d bin src/nonogram/*.java src/nonogram/**/*.java

if [ $? -eq 0 ]; then
    echo "Compilation successful!"
    echo "To run the game: java -cp bin nonogram.Main"
else
    echo "Compilation failed!"
fi
