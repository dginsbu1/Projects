@echo off
title FinalProject
:: as per outr email exchange, my programs parse out non alphabetic characters as well as numbers
:: use "|" with the quotes when doing the grep and wc

echo Java Object Oriented:
javac  "%CD%\Java\src\OOprogram"\*.java
java -cp "%CD%\Java\src"\ OOprogram.Controller %*
echo.
echo Java Functional:
javac  "%CD%\Java\src\functionalProgram"\*.java
java -cp "%CD%\Java\src"\  functionalProgram.Processor %*
echo.

echo Python Object Oriented:
python "%CD%\Python\venv\ObjectOriented\Controller.py" %*
echo.
echo Python Functional:
python "%CD%\Python\venv\Functional\Processor.py" %*
echo.

echo C Imperative:
gcc "%CD%\C\Imperative\Processor.c" -o "%CD%\C\Imperative\processor"
"%CD%\C\Imperative\processor" %*
echo.

echo JavaScript Objcet Oriented:
node "%CD%\JavaScript\ObjectiveOriented\Controller.js" %*
echo.
echo JavaScript Functional:
node "%CD%\JavaScript\Functional\Processor.js" %*
echo.

