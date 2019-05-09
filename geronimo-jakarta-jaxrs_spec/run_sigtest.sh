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

# file to run the JAX-RS signature tests


# HOWTO:
#
# Download sigtestdev.jar from https://wiki.openjdk.java.net/display/CodeTools/SigTest
# Copy to a local folder and set SIGTEST_HOME to it.


# not needed, we don't have deps to other specs in JPA
# mvn clean package dependency:copy-dependencies

packages="-Package javax.decorator -Package jakarta.ws.rs"
cp="${JAVA_HOME}/jre/lib/rt.jar"

# generate the SIG for the RI
java -jar ${SIGTEST_HOME}/lib/sigtestdev.jar Setup -classpath ${cp}:jakarta.ws.rs-api-2.1.jar ${packages} -FileName target/jakarta.ws.rs-api.sig -static

# this generates the signature for our own api
java -jar ${SIGTEST_HOME}/lib/sigtestdev.jar Setup -classpath ${cp}:target/geronimo-jaxrs_2.1_spec-1.1-SNAPSHOT.jar ${packages} -FileName target/geronimo-jaxrs_2.1_spec-1.1-SNAPSHOT.sig -static

# then open the 2 generated sig files in a diff browser and the only difference should be some internal variables.

echo "differences: "
diff target/jakarta.ws.rs-api.sig target/geronimo-jaxrs_2.1_spec-1.1-SNAPSHOT.sig | wc -l