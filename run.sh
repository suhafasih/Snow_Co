#!/bin/bash
echo "Building LibraryCatalogue on Mac/Unix..."

#var
APP_NAME="LibraryCatalogue"
LIB_DIR="UI/lib/*"
SRC_DIR="UI/src"
OUT_DIR="target/classes"

#clean
rm -rf target
mkdir -p "$OUT_DIR"

#compile
javac -d "$OUT_DIR" -cp "$LIB_DIR:$SRC_DIR" UI/src/*.java

#check
if [ $? -eq 0 ]; then
    echo "Compilation Successful!"
    echo "Launching $APP_NAME..."
    java -cp "$OUT_DIR:$LIB_DIR" LoginWindow
else
    echo "Compilation Failed. Check errors above."
    exit 1
fi