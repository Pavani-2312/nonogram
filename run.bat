@echo off
REM Nonogram Run Script (Windows)
echo Starting Nonogram Game...

REM Try normal execution first
java -cp bin nonogram.Main >nul 2>&1

REM If that fails, try with display compatibility flags
if %errorlevel% neq 0 (
    echo Retrying with display compatibility flags...
    java -Djava.awt.headless=false -Dsun.java2d.xrender=false -Dsun.java2d.pmoffscreen=false -Dsun.java2d.d3d=false -Dsun.java2d.opengl=false -cp bin nonogram.Main
)

pause
