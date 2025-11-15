# Build Process Documentation

## Compilation Overview

This document provides comprehensive instructions for compiling, testing, and running the Nonogram game project using manual Java compilation commands.

## Prerequisites

### Required Tools
- **Java Development Kit (JDK)**: Version 8 or higher
- **Command Line Interface**: Terminal (Linux/Mac) or Command Prompt (Windows)
- **Text Editor or IDE**: For viewing and editing source files

### Environment Verification
```bash
# Verify Java installation
java -version
javac -version

# Check current directory
pwd  # Should be in project root directory
```

## Directory Structure for Build

### Source Organization
```
nonogram/
├── src/main/java/          # Main source files
├── src/test/java/          # Test source files
├── resources/              # Resource files
├── build/                  # Build output directory
│   ├── classes/main/       # Compiled main classes
│   ├── classes/test/       # Compiled test classes
│   └── resources/          # Copied resource files
└── lib/                    # External libraries (if any)
```

## Compilation Commands

### Step 1: Create Build Directories
```bash
# Create necessary build directories
mkdir -p build/classes/main
mkdir -p build/classes/test
mkdir -p build/resources
```

### Step 2: Compile Data Structures (Phase 1)
```bash
# Compile data structures package
javac -d build/classes/main \
      -sourcepath src/main/java \
      src/main/java/datastructures/*.java
```

### Step 3: Compile Model Layer (Phase 2)
```bash
# Compile model package (depends on data structures)
javac -d build/classes/main \
      -sourcepath src/main/java \
      -classpath build/classes/main \
      src/main/java/model/*.java
```

### Step 4: Compile Algorithms (Phase 3)
```bash
# Compile algorithms package
javac -d build/classes/main \
      -sourcepath src/main/java \
      -classpath build/classes/main \
      src/main/java/algorithms/*.java
```

### Step 5: Compile Controller Layer (Phase 4)
```bash
# Compile controller package
javac -d build/classes/main \
      -sourcepath src/main/java \
      -classpath build/classes/main \
      src/main/java/controller/*.java
```

### Step 6: Compile View Layer (Phase 5)
```bash
# Compile view package
javac -d build/classes/main \
      -sourcepath src/main/java \
      -classpath build/classes/main \
      src/main/java/view/*.java
```

### Step 7: Compile Utilities
```bash
# Compile utils package
javac -d build/classes/main \
      -sourcepath src/main/java \
      -classpath build/classes/main \
      src/main/java/utils/*.java
```

### Complete Compilation (All at Once)
```bash
# Compile all main source files together
javac -d build/classes/main \
      -sourcepath src/main/java \
      -classpath build/classes/main \
      src/main/java/*/*.java
```

## Test Compilation

### Compile Test Classes
```bash
# Compile all test classes
javac -d build/classes/test \
      -sourcepath src/test/java \
      -classpath build/classes/main:build/classes/test \
      src/test/java/*/*.java
```

### Platform-Specific Classpath
```bash
# Windows (use semicolon separator)
javac -d build/classes/test \
      -sourcepath src/test/java \
      -classpath "build/classes/main;build/classes/test" \
      src/test/java/*/*.java

# Linux/Mac (use colon separator)
javac -d build/classes/test \
      -sourcepath src/test/java \
      -classpath "build/classes/main:build/classes/test" \
      src/test/java/*/*.java
```

## Resource Management

### Copy Resources
```bash
# Copy resource files to build directory
cp -r resources/* build/resources/

# Windows equivalent
xcopy resources build\resources /E /I
```

### Resource Integration
```bash
# Include resources in classpath for execution
# Resources will be accessible via classpath
```

## Execution Commands

### Run Main Application
```bash
# Run the main game application
java -classpath build/classes/main:build/resources \
     view.GameWindow

# Windows equivalent
java -classpath "build/classes/main;build/resources" \
     view.GameWindow
```

### Run Individual Tests
```bash
# Run specific test class
java -classpath build/classes/main:build/classes/test:build/resources \
     datastructures.MyArrayListTest

# Run with assertions enabled
java -ea -classpath build/classes/main:build/classes/test:build/resources \
     datastructures.MyArrayListTest
```

### Run All Tests (Manual)
```bash
# Run each test class individually
java -ea -classpath build/classes/main:build/classes/test:build/resources \
     datastructures.MyArrayListTest
java -ea -classpath build/classes/main:build/classes/test:build/resources \
     datastructures.MyStackTest
java -ea -classpath build/classes/main:build/classes/test:build/resources \
     model.CellTest
# ... continue for all test classes
```

## Build Scripts

### Linux/Mac Build Script (build.sh)
```bash
#!/bin/bash
# Create build script file: build.sh

echo "Building Nonogram Game..."

# Create build directories
mkdir -p build/classes/main
mkdir -p build/classes/test
mkdir -p build/resources

# Compile main sources
echo "Compiling main sources..."
javac -d build/classes/main \
      -sourcepath src/main/java \
      src/main/java/*/*.java

if [ $? -eq 0 ]; then
    echo "Main compilation successful"
else
    echo "Main compilation failed"
    exit 1
fi

# Compile test sources
echo "Compiling test sources..."
javac -d build/classes/test \
      -sourcepath src/test/java \
      -classpath build/classes/main \
      src/test/java/*/*.java

if [ $? -eq 0 ]; then
    echo "Test compilation successful"
else
    echo "Test compilation failed"
    exit 1
fi

# Copy resources
echo "Copying resources..."
cp -r resources/* build/resources/ 2>/dev/null || :

echo "Build completed successfully!"
```

### Windows Build Script (build.bat)
```batch
@echo off
REM Create build script file: build.bat

echo Building Nonogram Game...

REM Create build directories
mkdir build\classes\main 2>nul
mkdir build\classes\test 2>nul
mkdir build\resources 2>nul

REM Compile main sources
echo Compiling main sources...
javac -d build/classes/main ^
      -sourcepath src/main/java ^
      src/main/java/*/*.java

if %errorlevel% neq 0 (
    echo Main compilation failed
    exit /b 1
)
echo Main compilation successful

REM Compile test sources
echo Compiling test sources...
javac -d build/classes/test ^
      -sourcepath src/test/java ^
      -classpath build/classes/main ^
      src/test/java/*/*.java

if %errorlevel% neq 0 (
    echo Test compilation failed
    exit /b 1
)
echo Test compilation successful

REM Copy resources
echo Copying resources...
xcopy resources build\resources /E /I /Q >nul 2>&1

echo Build completed successfully!
```

## JAR Creation

### Create Executable JAR
```bash
# Create manifest file
echo "Main-Class: view.GameWindow" > manifest.txt
echo "Class-Path: resources/" >> manifest.txt

# Create JAR file
jar cfm build/nonogram.jar manifest.txt \
    -C build/classes/main . \
    -C build/resources .

# Run JAR file
java -jar build/nonogram.jar
```

### JAR with Resources
```bash
# Include all resources in JAR
jar cfm build/nonogram.jar manifest.txt \
    -C build/classes/main . \
    -C resources .
```

## Troubleshooting

### Common Compilation Errors

#### Class Not Found
**Problem**: `error: cannot find symbol`
**Solution**: 
- Check classpath includes all dependencies
- Verify package declarations match directory structure
- Ensure all required classes are compiled

#### Package Does Not Exist
**Problem**: `error: package does not exist`
**Solution**:
- Verify package directory structure
- Check import statements
- Ensure sourcepath is set correctly

#### Classpath Issues
**Problem**: Classes not found at runtime
**Solution**:
- Include all necessary directories in classpath
- Use correct path separator for your OS
- Verify build output directories exist

### Platform-Specific Issues

#### Windows Path Issues
- Use backslashes in file paths
- Use semicolons in classpath
- Enclose paths with spaces in quotes

#### Linux/Mac Permission Issues
- Make build scripts executable: `chmod +x build.sh`
- Ensure write permissions for build directory
- Check file ownership if needed

### Build Verification

#### Verify Compilation
```bash
# Check compiled classes exist
ls -la build/classes/main/datastructures/
ls -la build/classes/main/model/

# Verify class files are recent
find build/classes/main -name "*.class" -newer src/main/java
```

#### Test Execution
```bash
# Quick test of main classes
java -classpath build/classes/main datastructures.MyArrayList
java -classpath build/classes/main model.Cell
```

## Clean Build

### Clean Build Directories
```bash
# Remove all build artifacts
rm -rf build/

# Windows equivalent
rmdir /s build
```

### Fresh Build Process
```bash
# Complete clean and rebuild
rm -rf build/
mkdir -p build/classes/main build/classes/test build/resources
# Run full compilation commands
```

## Performance Optimization

### Compilation Optimization
- Use `-O` flag for optimized compilation
- Compile only changed files during development
- Use incremental compilation for large projects

### Memory Management
- Use `-Xmx` flag to set maximum heap size if needed
- Monitor memory usage during compilation
- Consider splitting large packages if compilation is slow

---

**Next Step**: Use these build processes while following the [Implementation Order](Implementation_Order.md) to systematically build the project.
