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
package javax.json.spi;

import org.junit.Test;

import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParserFactory;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

public class JsonProviderTest {
    @Test
    public void cache() {
        final JsonProvider provider = JsonProvider.provider();
        for (int i = 0; i < 10; i++) {
            final JsonProvider reload = JsonProvider.provider();
            assertSame(provider, reload);
        }
    }

    @Test
    public void cacheByClassLoader() {
        final JsonProvider provider = JsonProvider.provider();

        final Thread thread = Thread.currentThread();
        final URLClassLoader loader = new URLClassLoader(new URL[0], thread.getContextClassLoader());
        thread.setContextClassLoader(loader);
        try {
            final JsonProvider subLoaderProvider = JsonProvider.provider();
            for (int i = 0; i < 10; i++) {
                final JsonProvider reload = JsonProvider.provider();
                assertNotSame(provider, reload);
                assertSame(subLoaderProvider, reload);
            }
        } finally {
            thread.setContextClassLoader(loader.getParent());
        }
    }

    public static class AProvider extends JsonProvider {
        @Override
        public JsonParser createParser(final Reader reader) {
            return null;
        }

        @Override
        public JsonParser createParser(final InputStream in) {
            return null;
        }

        @Override
        public JsonParserFactory createParserFactory(final Map<String, ?> config) {
            return null;
        }

        @Override
        public JsonGenerator createGenerator(final Writer writer) {
            return null;
        }

        @Override
        public JsonGenerator createGenerator(final OutputStream out) {
            return null;
        }

        @Override
        public JsonGeneratorFactory createGeneratorFactory(final Map<String, ?> config) {
            return null;
        }

        @Override
        public JsonReader createReader(final Reader reader) {
            return null;
        }

        @Override
        public JsonReader createReader(final InputStream in) {
            return null;
        }

        @Override
        public JsonWriter createWriter(final Writer writer) {
            return null;
        }

        @Override
        public JsonWriter createWriter(final OutputStream out) {
            return null;
        }

        @Override
        public JsonWriterFactory createWriterFactory(final Map<String, ?> config) {
            return null;
        }

        @Override
        public JsonReaderFactory createReaderFactory(final Map<String, ?> config) {
            return null;
        }

        @Override
        public JsonObjectBuilder createObjectBuilder() {
            return null;
        }

        @Override
        public JsonArrayBuilder createArrayBuilder() {
            return null;
        }

        @Override
        public JsonBuilderFactory createBuilderFactory(final Map<String, ?> config) {
            return null;
        }
    }
}
