:: for window user system, not for mac
@echo off
echo Building LibraryCatalogue on Windows...


::Instead of hardcoding paths throughout the script, we declare where our source code (src),
::external libraries (lib), and compiled output (target) live
:: variables:
set APP_NAME=LibraryCatalogue
set LIB_DIR=UI\lib\*
set SRC_DIR=UI\src
set OUT_DIR=target\classes

:: clean
:: rmdir /s /q silently deletes the target folder if it exists
rmdir /s /q target 2>nul
mkdir %OUT_DIR%

:: compile

javac -d %OUT_DIR% -cp "%LIB_DIR%;%SRC_DIR%" UI\src\*.java

:: check if compilation worked by looking at the system's exit code.
if %ERRORLEVEL% EQU 0 (
    echo Compilation Successful!
    echo Launching %APP_NAME%...
    java -cp "%OUT_DIR%;%LIB_DIR%" LoginWindow
) else (
    echo Compilation Failed. Check errors above.
    pause
    exit /b 1
)