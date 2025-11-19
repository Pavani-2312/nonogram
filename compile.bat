@echo off

REM Nonogram Compilation Script for Windows
echo Compiling Nonogram Game...

REM Create bin directory if it doesn't exist
if not exist bin mkdir bin

REM Compile all Java files including Main.java
javac -d bin src\nonogram\*.java src\nonogram\datastructures\*.java src\nonogram\model\*.java src\nonogram\view\*.java src\nonogram\controller\*.java

if %errorlevel% equ 0 (
    echo Compilation successful!
    echo To run the game: java -cp bin nonogram.Main
) else (
    echo Compilation failed!
)

pause
