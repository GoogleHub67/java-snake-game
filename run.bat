@echo off
echo 🧹 Cleaning up old builds...
del /q *.class *.jar 2>nul

echo 🔨 Compiling Game.java...
javac Game.java

if %errorlevel% equ 0 (
    echo 📦 Packaging into java-snake-game.jar using manifest.mf...
    jar --create --file=java-snake-game.jar --manifest=manifest.mf *.class
    
    echo 🚀 Launching Snake Game...
    java -jar java-snake-game.jar
) else (
    echo ❌ Compilation failed.
    pause
)
