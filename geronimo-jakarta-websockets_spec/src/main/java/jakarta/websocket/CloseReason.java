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

public class CloseReason {

    /**
     * A marker interface for the close codes. This interface may be implemented by enumerations that contain web socket
     * close codes, for example enumerations that contain all the in use close codes as of web socket 1.0, or an
     * enumeration that contains close codes that are currently reserved for special use by the web socket
     * specification.
     */
    public static interface CloseCode {
        /**
         * Returns the code number, for example the integer '1000' for normal closure.
         * 
         * @return the code number
         */
        public int getCode();
    }

    /**
     * An Enumeration of status codes for a web socket close that are defined in the specification.
     */
    public static enum CloseCodes implements CloseCode {
        /**
         * 1000 indicates a normal closure, meaning that the purpose for which the connection was established has been
         * fulfilled.
         */
        NORMAL_CLOSURE (1000),
        /**
         * 1001 indicates that an endpoint is "going away", such as a server going down or a browser having navigated
         * away from a page.
         */
        GOING_AWAY (1001),
        /**
         * 1002 indicates that an endpoint is terminating the connection due to a protocol error.
         */
        PROTOCOL_ERROR (1002),
        /**
         * 1003 indicates that an endpoint is terminating the connection because it has received a type of data it
         * cannot accept (e.g., an endpoint that understands only text data MAY send this if it receives a binary
         * message).
         */
        CANNOT_ACCEPT (1003),
        /**
         * Reserved. The specific meaning might be defined in the future.
         */
        RESERVED (1004),
        /**
         * 1005 is a reserved value and MUST NOT be set as a status code in a Close control frame by an endpoint. It is
         * designated for use in applications expecting a status code to indicate that no status code was actually
         * present.
         */
        NO_STATUS_CODE (1005),
        /**
         * 1006 is a reserved value and MUST NOT be set as a status code in a Close control frame by an endpoint. It is
         * designated for use in applications expecting a status code to indicate that the connection was closed
         * abnormally, e.g., without sending or receiving a Close control frame.
         */
        CLOSED_ABNORMALLY (1006),
        /**
         * 1007 indicates that an endpoint is terminating the connection because it has received data within a message
         * that was not consistent with the type of the message (e.g., non-UTF-8 data within a text message).
         */
        NOT_CONSISTENT (1007),
        /**
         * 1008 indicates that an endpoint is terminating the connection because it has received a message that violates
         * its policy. This is a generic status code that can be returned when there is no other more suitable status
         * code (e.g., 1003 or 1009) or if there is a need to hide specific details about the policy.
         */
        VIOLATED_POLICY (1008),
        /**
         * 1009 indicates that an endpoint is terminating the connection because it has received a message that is too
         * big for it to process.
         */
        TOO_BIG (1009),
        /**
         * 1010 indicates that an endpoint (client) is terminating the connection because it has expected the server to
         * negotiate one or more extension, but the server didn't return them in the response message of the WebSocket
         * handshake. The list of extensions that are needed SHOULD appear in the /reason/ part of the Close frame. Note
         * that this status code is not used by the server, because it can fail the WebSocket handshake instead.
         */
        NO_EXTENSION (1010),
        /**
         * 1011 indicates that a server is terminating the connection because it encountered an unexpected condition
         * that prevented it from fulfilling the request.
         */
        UNEXPECTED_CONDITION (1011),
        /**
         * 1012 indicates that the service will be restarted.
         */
        SERVICE_RESTART (1012),
        /**
         * 1013 indicates that the service is experiencing overload
         */
        TRY_AGAIN_LATER (1013),

        /**
         * 1015 is a reserved value and MUST NOT be set as a status code in a Close control frame by an endpoint. It is
         * designated for use in applications expecting a status code to indicate that the connection was closed due to
         * a failure to perform a TLS handshake (e.g., the server certificate can't be verified).
         */
        TLS_HANDSHAKE_FAILURE (1015);

	private int code;

	CloseCodes (int code) {
		this.code = code;
	}

        /**
         * Return the code number of this status code.
         * 
         * @return the code.
         */
        public int getCode() {
            return this.code;
        }

        /**
         * Creates a CloseCode from the given int code number. This method throws an IllegalArgumentException if the int
         * is not one of the close codes.
         * 
         * @param code
         *            the integer code number
         * @return a new CloseCode with the given code number
         * @throws IllegalArgumentException
         *             - if the code is not a valid close code
         */
        public static CloseCode getCloseCode(int code) {
            switch (code) {
            case 1000:
                return NORMAL_CLOSURE;
            case 1001:
                return GOING_AWAY;
            case 1002:
                return PROTOCOL_ERROR;
            case 1003:
                return CANNOT_ACCEPT;
            case 1005:
                return NO_STATUS_CODE;
            case 1006:
                return CLOSED_ABNORMALLY;
            case 1007:
                return NOT_CONSISTENT;
            case 1008:
                return VIOLATED_POLICY;
            case 1009:
                return TOO_BIG;
            case 1010:
                return NO_EXTENSION;
            case 1011:
                return UNEXPECTED_CONDITION;
            case 1012:
                return SERVICE_RESTART;
            case 1013:
                return TRY_AGAIN_LATER;
            case 1015:
                return TLS_HANDSHAKE_FAILURE;
            default:
                throw new IllegalArgumentException("Unknown Code: " + code);
            }
        }

    }

    private final CloseCode closeCode;
    private final String reasonPhrase;

    /**
     * Creates a reason for closing a web socket connection with the given code and reason phrase.
     * 
     * @param closeCode
     *            the close code, may not be null
     * @param reasonPhrase
     *            the reason phrase, may be null.
     */
    public CloseReason(CloseCode closeCode, String reasonPhrase) {
        if (closeCode == null) {
            throw new NullPointerException();
        }
        this.closeCode = closeCode;
        this.reasonPhrase = reasonPhrase == null ? "" : reasonPhrase;
    }

    /**
     * The Close code associated with this CloseReason.
     * 
     * @return the close code.
     */
    public CloseCode getCloseCode() {
        return closeCode;
    }

    /**
     * The reason phrase associated with this CloseReason.
     * 
     * @return the reason phrase. If there is no reason phrase, this returns the empty string
     */
    public String getReasonPhrase() {
        return reasonPhrase;
    }

    /**
     * Converts the CloseReason to a debug-friendly string. The exact format is not defined by the specification and may
     * change in future releases.
     * 
     * @return A String representation of this CloseReason
     */
    @Override
    public String toString() {
        return "javax.websocket.CloseReason: " + this.closeCode + " " + this.reasonPhrase;
    }
}
