/*
 **
 ** Licensed to the Apache Software Foundation (ASF) under one
 ** or more contributor license agreements.  See the NOTICE file
 ** distributed with this work for additional information
 ** regarding copyright ownership.  The ASF licenses this file
 ** to you under the Apache License, Version 2.0 (the
 ** "License"); you may not use this file except in compliance
 ** with the License.  You may obtain a copy of the License at
 **
 **  http://www.apache.org/licenses/LICENSE-2.0
 **
 ** Unless required by applicable law or agreed to in writing,
 ** software distributed under the License is distributed on an
 ** "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 ** KIND, either express or implied.  See the License for the
 ** specific language governing permissions and limitations
 ** under the License.
 */
package javax.xml.bind;

public class PropertyException extends JAXBException {

    public PropertyException(String message) {
        super(message);
    }

    public PropertyException(String message, String errorCode) {
        super(message, errorCode);
    }

    public PropertyException(Throwable cause) {
        super(cause);
    }

    public PropertyException(String message, Throwable cause) {
        super(message, cause);
    }

    public PropertyException(String message, String errorCode, Throwable cause) {
        super(message, errorCode, cause);
    }

    public PropertyException(String name, Object value) {
        super("name: " + name + ", value: " + value);
    }

}
