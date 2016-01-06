echo off
echo Running Autos
echo
set CLASSPATH=..\lib\mysql-connector-java-5.1.13-bin.jar;.
cd bin
java ca.bob.autos.cli.Console %1 %2 %3 %4 %5 %6
cd ..
