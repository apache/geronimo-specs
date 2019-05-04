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

import java.nio.ByteBuffer;

/**
 * A general exception that occurs when trying to decode a custom object from a text or binary message.
 * 
 */
public class DecodeException extends Exception {
    private static final long serialVersionUID = -446026355870506692L;

    private final ByteBuffer bytes;
    private final String text;

    /**
     * Constructs a DecodedException with the given ByteBuffer that cannot be decoded, and reason why. The buffer may
     * represent the whole message, or the part of the message most relevant to the decoding error, depending whether
     * the application is using one of the streaming methods or not.
     * 
     * @param bytes
     *            the byte buffer containing the (part of) the message that could not be decoded.
     * @param message
     *            the reason for the failure.
     */
    public DecodeException(ByteBuffer bytes, String message) {
        super(message);
        this.bytes = bytes;
        this.text = null;
    }

    /**
     * Constructor with the binary data that could not be decoded, and the reason why it failed to be, and the cause.
     * The buffer may represent the whole message, or the part of the message most relevant to the decoding error,
     * depending whether the application is using one of the streaming methods or not.
     * 
     * @param bytes
     *            the byte buffer containing the (part of) the message that could not be decoded.
     * @param message
     *            the reason for the failure.
     * @param cause
     *            the cause of the error.
     */
    public DecodeException(ByteBuffer bytes, String message, Throwable cause) {
        super(message, cause);
        this.bytes = bytes;
        this.text = null;
    }

    /**
     * Constructs a DecodedException with the given encoded string that cannot be decoded, and reason why. The encoded
     * string may represent the whole message, or the part of the message most relevant to the decoding error, depending
     * whether the application is using one of the streaming methods or not.
     * 
     * @param encodedString
     *            the string representing the (part of) the message that could not be decoded.
     * @param message
     *            the reason for the failure.
     */
    public DecodeException(String encodedString, String message) {
        super(message);
        this.text = encodedString;
        this.bytes = null;
    }

    /**
     * Constructor with the text data that could not be decoded, and the reason why it failed to be, and the cause. The
     * encoded string may represent the whole message, or the part of the message most relevant to the decoding error,
     * depending whether the application is using one of the streaming methods or not.
     * 
     * @param encodedString
     *            the string representing the (part of) the message that could not be decoded.
     * @param message
     *            the reason for the failure.
     * @param cause
     *            the cause of the error.
     */
    public DecodeException(String encodedString, String message, Throwable cause) {
        super(message, cause);
        this.text = encodedString;
        this.bytes = null;
    }

    /**
     * Return the ByteBuffer containing either the whole message, or the partial message, that could not be decoded, or
     * null if this exception arose from a failure to decode a text message.
     * 
     * @return the binary data not decoded or null for text message failures.
     */
    public ByteBuffer getBytes() {
        return bytes;
    }

    /**
     * Return the encoded string that is either the whole message, or the partial message that could not be decoded, or
     * null if this exception arose from a failure to decode a binary message..
     * 
     * @return the text not decoded or null for binary message failures.
     */
    public String getText() {
        return text;
    }
}
