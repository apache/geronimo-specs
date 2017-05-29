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
# Download sigtestdev.jar from https://wiki.openjdk.java.net/display/CodeTools/SigTest
# Copy to a local folder and set SIGTEST_HOME to it.


# not needed, we don't have deps to other specs in JPA
mvn clean package dependency:copy-dependencies

packages="-Package javax.decorator -Package javax.enterprise.context -Package javax.enterprise.util -Package javax.enterprise.inject -Package javax.enterprise.event"
cp="${JAVA_HOME}/jre/lib/rt.jar:target/dependency/geronimo-interceptor_1.1_spec-1.0.jar:target/dependency/geronimo-el_2.2_spec-1.0.jar:target/dependency/geronimo-atinject_1.0_spec-1.0.jar"

# generate the SIG for the RI
java -jar ${SIGTEST_HOME}/lib/sigtestdev.jar Setup -classpath ${cp}:cdi-api-2.0.jar ${packages} -FileName target/cdi-api.sig -static

# this generates the signature for our own api
java -jar ${SIGTEST_HOME}/lib/sigtestdev.jar Setup -classpath ${cp}:target/geronimo-jcdi_2.0_spec-1.0-SNAPSHOT.jar ${packages} -FileName target/geronimo-jcdi-api.sig -static

# then open the 2 generated sig files in a diff browser and the only difference should be some internal variables.

echo "differences: "
diff target/cdi-api.sig target/geronimo-jcdi-api.sig | wc -l