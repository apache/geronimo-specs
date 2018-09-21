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

# file to run the beanvalidation signature tests


# HOWTO:
#
# Download sigtest 3.1
# Copy to a local folder and set SIGTEST_HOME to it.


# not needed, we don't have deps to other specs in JPA
# mvn clean dependency:copy-dependencies

# generate the SIG for the RI
REF_API_JAR=$(realpath ./target/javax.validation-api-2.0.1.Final.jar)
GERONIMO_API_JAR=$(realpath ./target/geronimo-*.jar)
PACKAGE=javax.validation

echo "refapi:" ${REF_API_JAR}
echo "geronimoapi:" ${GERONIMO_API_JAR}

rm target/*.sig

if [ ! -f ${REF_API_JAR} ]; then
    echo "downlooooooading"
    curl  http://repo1.maven.org/maven2/javax/validation/validation-api/2.0.1.Final/validation-api-2.0.1.Final.jar > ${REF_API_JAR}
fi

java -jar ${SIGTEST_HOME}/lib/sigtestdev.jar Setup -classpath ${JAVA_HOME}/jre/lib/rt.jar:${REF_API_JAR} -Package ${PACKAGE}  -FileName ${REF_API_JAR}.sig -static

# this generates the signature for our own api
java -jar ${SIGTEST_HOME}/lib/sigtestdev.jar Setup -classpath ${JAVA_HOME}/jre/lib/rt.jar:${GERONIMO_API_JAR} -Package ${PACKAGE}  -FileName ${GERONIMO_API_JAR}.sig -static

# then open the 2 generated sig files in a diff browser and the only difference should be some internal variables.

diff ${REF_API_JAR}.sig ${GERONIMO_API_JAR}.sig