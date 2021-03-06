/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package javax.websocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.ByteBuffer;

/**
 * The Decoder interface holds member interfaces that define how a developer can provide the web socket container a way
 * web socket messages into developer defined custom objects. The websocket implementation creates a new instance of the
 * decoder per endpoint instance per connection. The lifecycle of the Decoder instance is governed by the container
 * calls to the init(javax.websocket.EndpointConfig) and destroy() methods.
 * 
 */
public interface Decoder {

    /**
     * This interface defines how a custom object (of type T) is decoded from a web socket message in the form of a byte
     * buffer.
     */
    public static interface Binary<T> extends Decoder {
        /**
         * Decode the given bytes into an object of type T.
         * 
         * @param bytes
         *            the bytes to be decoded.
         * @return the decoded object.
         * @throws DecodeException
         */
        T decode(ByteBuffer bytes) throws DecodeException;

        /**
         * Answer whether the given bytes can be decoded into an object of type T.
         * 
         * @param bytes
         *            the bytes to be decoded.
         * @return whether or not the bytes can be decoded by this decoder.
         */
        boolean willDecode(ByteBuffer bytes);
    }

    /**
     * This interface defines how a custom object is decoded from a web socket message in the form of a binary stream.
     */
    public static interface BinaryStream<T> extends Decoder {
        /**
         * Decode the given bytes read from the input stream into an object of type T.
         * 
         * @param is
         *            the input stream carrying the bytes.
         * @return the decoded object.
         * @throws DecodeException
         * @throws IOException
         */
        T decode(InputStream is) throws DecodeException, IOException;
    }

    /**
     * This interface defines how a custom object is decoded from a web socket message in the form of a string.
     */
    public static interface Text<T> extends Decoder {
        /**
         * Decode the given String into an object of type T.
         * 
         * @param s
         *            string to be decoded.
         * @return the decoded message as an object of type T
         * @throws DecodeException
         */
        T decode(String s) throws DecodeException;

        /**
         * Answer whether the given String can be decoded into an object of type T.
         * 
         * @param s
         *            the string being tested for decodability.
         * @return whether this decoder can decoded the supplied string.
         */
        boolean willDecode(String s);
    }

    /**
     * This interface defines how a custom object of type T is decoded from a web socket message in the form of a
     * character stream.
     */
    public static interface TextStream<T> extends Decoder {
        /**
         * Reads the websocket message from the implementation provided Reader and decodes it into an instance of the
         * supplied object type.
         * 
         * @param reader
         *            the reader from which to read the web socket message.
         * @return the instance of the object that is the decoded web socket message.
         * @throws DecodeException
         * @throws IOException
         */
        T decode(Reader reader) throws DecodeException, IOException;
    }

    /**
     * This method is called with the endpoint configuration object of the endpoint this decoder is intended for when it
     * is about to be brought into service.
     * 
     * @param config
     *            the endpoint configuration object when being brought into service
     */
    void init(EndpointConfig config);

    /**
     * This method is called when the decoder is about to be removed from service in order that any resources the
     * encoder used may be closed gracefully.
     */
    void destroy();
}
