@echo off
echo 🧹 Cleaning up old builds...
del /q *.class 2>nul

echo 🔨 Compiling Game.java...
javac Game.java

if %errorlevel% equ 0 (
    echo 🚀 Launching Snake Game...
    java Game
) else (
    echo ❌ Compilation failed.
    pause
)
