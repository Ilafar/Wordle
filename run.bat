@echo off
setlocal enabledelayedexpansion

cd /d "%~dp0"
if not exist "out" mkdir "out"

type nul > "out\sources.txt"
for /r "src" %%f in (*.java) do (
    set "p=%%f"
    set "p=!p:\=/!"
    echo "!p!">>"out\sources.txt"
)

javac -d "out" @"out\sources.txt"
if errorlevel 1 (
    echo Compilation failed.
    exit /b 1
)

java -cp "out" com.wordgame.Main
