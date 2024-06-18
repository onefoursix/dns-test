#!/usr/bin/env bash

# Set this to point to a JDK8 directory
export JAVA_HOME=/Library/Java/JavaVirtualMachines/zulu-8.jdk/Contents/Home

rm -f dnstest-1.0.0.jar

mvn package -DskipTests
mv target/dnstest-1.0.0.jar .
rm -rf target

