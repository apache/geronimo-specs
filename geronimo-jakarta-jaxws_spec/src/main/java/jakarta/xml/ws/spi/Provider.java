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

package jakarta.xml.ws.spi;

import org.w3c.dom.Element;

import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import jakarta.xml.ws.Endpoint;
import jakarta.xml.ws.EndpointReference;
import jakarta.xml.ws.Service;
import jakarta.xml.ws.WebServiceFeature;
import jakarta.xml.ws.wsaddressing.W3CEndpointReference;
import java.net.URL;
import java.util.List;
import java.util.Map;

public abstract class Provider {
    public static final String JAXWSPROVIDER_PROPERTY = "jakarta.xml.ws.spi.Provider";
    private static final String DEFAULT_JAXWSPROVIDER = "org.apache.cxf.jaxws.spi.ProviderImpl";

    protected Provider() {
    }

    public static Provider provider() {
        return (Provider) FactoryFinder.find(JAXWSPROVIDER_PROPERTY, DEFAULT_JAXWSPROVIDER);
    }

    /**
     * Creates a service delegate object for the URL and service Qname
     */
    public abstract ServiceDelegate createServiceDelegate(URL url, QName qname, Class<? extends Service> class1);
    
    /**
     * Creates a service delegate object for the URL, service Qname, and web service features.
     * @since 2.2
     */
    public ServiceDelegate createServiceDelegate(URL url, QName qname, Class<? extends Service> cls,
                                                 WebServiceFeature ... features) {
        throw new UnsupportedOperationException("JAX-WS 2.2 implementations must override this method.");        
    }

    /**
     * Creates an Endpoint object with the provided bindingId and implementation object.
     */
    public abstract Endpoint createEndpoint(String bindingId, Object obj);

    /**
     * Creates an Endpoint object with the provided bindingId, implementation class, invoker, and web service features.
     * @since 2.2
     */
    public Endpoint createEndpoint(String bindingId, Class<?> cls, Invoker invoker, WebServiceFeature ... features) {
        throw new UnsupportedOperationException("JAX-WS 2.2 implementations must override this method.");        
    }
    
    /**
     * Creates an Endpoint object with the provided bindingId, implementation object, and web service features.
     * @since 2.2
     */
    public Endpoint createEndpoint(String bindingId, Object obj, WebServiceFeature ... features) {
        throw new UnsupportedOperationException("JAX-WS 2.2 implementations must override this method.");        
    }

    
    /**
     * Creates and publishes an Endpoint object with the specified address and implementation object.
     */
    public abstract Endpoint createAndPublishEndpoint(String s, Object obj);
    
    /**
     * Creates and publishes an Endpoint object with the specified address, implementation object and web service features.
     * @since 2.2
     */
    public Endpoint createAndPublishEndpoint(String address, Object obj, WebServiceFeature ... features) {
        throw new UnsupportedOperationException("JAX-WS 2.2 implementations must override this method.");
    }
    
    public abstract EndpointReference readEndpointReference(Source eprInfoset);
    
    public abstract <T> T getPort(EndpointReference endpointReference, Class<T> serviceEndpointInterface, 
                                  WebServiceFeature... features);
    
    public abstract W3CEndpointReference createW3CEndpointReference(String address,
            QName serviceName,
            QName portName,
            List<Element> metadata,
            String wsdlDocumentLocation,
            List<Element> referenceParameters);
    

    
    /**
     * @since 2.2
     */
    public W3CEndpointReference createW3CEndpointReference(String address, 
                                                           QName interfaceName, 
                                                           QName serviceName,
                                                           QName portName,
                                                           List<Element> metadata,
                                                           String wsdlLocation,
                                                           List<Element> referenceParameters,
                                                           List<Element> elements,
                                                           Map<QName, String> attributes) {
        throw new UnsupportedOperationException("JAX-WS 2.2 implementations must override this method.");        
    }
    
    
}
