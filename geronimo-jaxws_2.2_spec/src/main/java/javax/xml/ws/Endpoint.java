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

package javax.xml.ws;

import javax.xml.transform.Source;
import javax.xml.ws.spi.Provider;

import org.w3c.dom.Element;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

public abstract class Endpoint {

    public Endpoint() {
    }

    public static Endpoint create(Object implementor) {
        return create(null, implementor);
    }

    public static Endpoint create(String bindingId, Object implementor) {
        return Provider.provider().createEndpoint(bindingId, implementor);
    }
    /**
     * @since 2.2
     */
    public static Endpoint create(Object implementor, WebServiceFeature ... features) {
        return Provider.provider().createEndpoint(null, implementor, features);                
    }
    /**
     * @since 2.2
     */
    public static Endpoint create(String bindingId, Object implementor, WebServiceFeature ... features) {
        return Provider.provider().createEndpoint(bindingId, implementor, features);        
    }
    
    public static Endpoint publish(String address, Object implementor) {
        return Provider.provider().createAndPublishEndpoint(address, implementor);
    }
    /**
     * @since 2.2
     */
    public static Endpoint publish(String address, Object implementor, WebServiceFeature ... features) {
        return Provider.provider().createAndPublishEndpoint(address, implementor, features);        
    }

    public abstract Binding getBinding();

    public abstract Object getImplementor();

    public abstract void publish(String s);


    public abstract void publish(Object obj);

    public abstract void stop();

    public abstract boolean isPublished();

    public abstract List<Source> getMetadata();

    public abstract void setMetadata(List<Source> list);

    public abstract Executor getExecutor();

    public abstract void setExecutor(Executor executor);

    public abstract Map<String, Object> getProperties();

    public abstract void setProperties(Map<String, Object> map);

    public abstract EndpointReference getEndpointReference(Element... referenceParameters);

    public abstract <T extends EndpointReference> T getEndpointReference(Class<T> clazz, Element... referenceParameters);

    
    /**
     * @since 2.2
     */
    public void setEndpointContext(EndpointContext ctx) {
        throw new UnsupportedOperationException("JAX-WS 2.2 implementations must override this method.");
    }
    /**
     * @since 2.2
     */
    public void publish(javax.xml.ws.spi.http.HttpContext context) {
        throw new UnsupportedOperationException("JAX-WS 2.2 implementations must override this method.");        
    }
    
    
    public static final String WSDL_SERVICE = "javax.xml.ws.wsdl.service";
    public static final String WSDL_PORT = "javax.xml.ws.wsdl.port";
}
