#!/bin/bash
echo "🧹 Cleaning up old builds..."
rm -f *.class *.jar

echo "🔨 Compiling Game.java..."
javac Game.java

if [ $? -eq 0 ]; then
    echo "📦 Packaging into java-snake-game.jar using manifest.mf..."
    jar --create --file=java-snake-game.jar --manifest=manifest.mf *.class
    
    echo "🚀 Launching Snake Game..."
    java -jar java-snake-game.jar
else
    echo "❌ Compilation failed."
fi
