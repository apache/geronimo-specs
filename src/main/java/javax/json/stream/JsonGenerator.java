/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package javax.json.stream;

import javax.json.JsonValue;
import java.io.Closeable;
import java.io.Flushable;
import java.math.BigDecimal;
import java.math.BigInteger;

public interface JsonGenerator extends Flushable, Closeable {
    String PRETTY_PRINTING = "javax.json.stream.JsonGenerator.prettyPrinting"; // TODO: ensure it exists before releasing

    JsonGenerator writeStartObject();

    JsonGenerator writeStartObject(String name);

    JsonGenerator writeStartArray();

    JsonGenerator writeStartArray(String name);

    /**
     * Write the key with a colon;
     *
     * @throws JsonGenerationException if this method is not called within an object context
     * @since 1.1
     */
    JsonGenerator writeKey(String name);

    JsonGenerator write(String name, JsonValue value);

    JsonGenerator write(String name, String value);

    JsonGenerator write(String name, BigInteger value);

    JsonGenerator write(String name, BigDecimal value);

    JsonGenerator write(String name, int value);

    JsonGenerator write(String name, long value);

    JsonGenerator write(String name, double value);

    JsonGenerator write(String name, boolean value);

    JsonGenerator writeNull(String name);

    JsonGenerator writeEnd();

    JsonGenerator write(JsonValue value);

    JsonGenerator write(String value);

    JsonGenerator write(BigDecimal value);

    JsonGenerator write(BigInteger value);

    JsonGenerator write(int value);

    JsonGenerator write(long value);

    JsonGenerator write(double value);

    JsonGenerator write(boolean value);

    JsonGenerator writeNull();

    @Override
    void close();

    @Override
    void flush();

}
