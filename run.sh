#!/bin/bash
echo "🧹 Cleaning up old builds..."
rm -f *.class

echo "🔨 Compiling Game.java..."
javac Game.java

if [ $? -eq 0 ]; then
    echo "🚀 Launching Snake Game..."
    java Game
else
    echo "❌ Compilation failed."
fi
