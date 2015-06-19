#!/bin/sh

#    Licensed to the Apache Software Foundation (ASF) under one
#    or more contributor license agreements.  See the NOTICE file
#    distributed with this work for additional information
#    regarding copyright ownership.  The ASF licenses this file
#    to you under the Apache License, Version 2.0 (the
#    "License"); you may not use this file except in compliance
#    with the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
#    Unless required by applicable law or agreed to in writing,
#    software distributed under the License is distributed on an
#    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
#    KIND, either express or implied.  See the License for the
#    specific language governing permissions and limitations
#    under the License.

# file to run the JPA signature tests


# HOWTO:
#
# Download sigtestdev.jar from http://download.java.net/sigtest/2.2/Rel/
# Copy to a local folder and set SIGTEST_HOME to it.


# not needed, we don't have deps to other specs in JPA
# mvn clean dependency:copy-dependencies

# generate the SIG for the RI
curl  http://repo1.maven.org/maven2/org/eclipse/persistence/javax.persistence/2.1.0/javax.persistence-2.1.0.jar > ./target/javax.persistence-2.1.0.jar
java -jar ${SIGTEST_HOME}/lib/sigtestdev.jar Setup -classpath ${JAVA_HOME}/jre/lib/rt.jar:./target/javax.persistence-2.1.0.jar -Package javax.persistence  -FileName target/javax.persistence-2.1.0.sig -static

# this generates the signature for our own jpa api
java -jar ${SIGTEST_HOME}/lib/sigtestdev.jar Setup -classpath ${JAVA_HOME}/jre/lib/rt.jar:./target/geronimo-jpa_2.1_spec-1.0-SNAPSHOT.jar -Package javax.persistence  -FileName target/geronimo-jpa-api.sig -static

# then open the 2 generated sig files in a diff browser and the only difference should be some internal variables.
