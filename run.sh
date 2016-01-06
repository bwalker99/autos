#!/bin/bash
# run the Auto command line interface
#
# export JAVA_HOME=/usr/local/java/current
echo ----------------------------- 
echo Running Autos:
cd bin  
export CLASSPATH=../lib/mysql-connector-java-5.1.13-bin.jar:. 
java ca/bob/autos/cli/Console $1 $2 $3 $4 $5 $6
cd ..
