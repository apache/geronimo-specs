/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package jakarta.xml.ws.handler;

import java.util.Map;

public interface MessageContext extends Map<String, Object> {

    public enum Scope {
        APPLICATION,  
        HANDLER
    }

    public abstract void setScope(String s, Scope scope);

    public abstract Scope getScope(String s);

    public static final String MESSAGE_OUTBOUND_PROPERTY = "jakarta.xml.ws.handler.message.outbound";
    public static final String WSDL_DESCRIPTION = "jakarta.xml.ws.wsdl.description";
    public static final String WSDL_SERVICE = "jakarta.xml.ws.wsdl.service";
    public static final String WSDL_PORT = "jakarta.xml.ws.wsdl.port";
    public static final String WSDL_INTERFACE = "jakarta.xml.ws.wsdl.interface";
    public static final String WSDL_OPERATION = "jakarta.xml.ws.wsdl.operation";
    public static final String HTTP_RESPONSE_CODE = "jakarta.xml.ws.http.response.code";
    public static final String REFERENCE_PARAMETERS = "jakarta.xml.ws.reference.parameters";
    public static final String HTTP_REQUEST_HEADERS = "jakarta.xml.ws.http.request.headers";
    public static final String HTTP_RESPONSE_HEADERS = "jakarta.xml.ws.http.response.headers";
    public static final String HTTP_REQUEST_METHOD = "jakarta.xml.ws.http.request.method";
    public static final String SERVLET_REQUEST = "jakarta.xml.ws.servlet.request";
    public static final String SERVLET_RESPONSE = "jakarta.xml.ws.servlet.response";
    public static final String SERVLET_CONTEXT = "jakarta.xml.ws.servlet.context";
    public static final String INBOUND_MESSAGE_ATTACHMENTS = "jakarta.xml.ws.binding.attachments.inbound";
    public static final String OUTBOUND_MESSAGE_ATTACHMENTS = "jakarta.xml.ws.binding.attachments.outbound";
    public static final String QUERY_STRING = "jakarta.xml.ws.http.request.querystring";
    public static final String PATH_INFO = "jakarta.xml.ws.http.request.pathinfo";
}
