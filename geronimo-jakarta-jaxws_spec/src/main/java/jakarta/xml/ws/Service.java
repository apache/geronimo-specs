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

package jakarta.xml.ws;

import jakarta.xml.bind.JAXBContext;
import javax.xml.namespace.QName;
import jakarta.xml.ws.handler.HandlerResolver;
import jakarta.xml.ws.spi.Provider;
import jakarta.xml.ws.spi.ServiceDelegate;
import java.net.URL;
import java.util.Iterator;
import java.util.concurrent.Executor;

public class Service {
    public enum Mode { MESSAGE, PAYLOAD }

    protected Service(URL wsdlDocumentLocation, QName serviceName) {
        delegate = Provider.provider().createServiceDelegate(wsdlDocumentLocation, serviceName, getClass());
    }
    /**
     * @since 2.2
     */
    protected Service(URL wsdlDocumentLocation, QName serviceName, WebServiceFeature... features) {
        delegate = Provider.provider().createServiceDelegate(wsdlDocumentLocation, serviceName, getClass(), features);
    }
    

    public <T> T getPort(QName portName, Class<T> serviceEndpointInterface) {
        return (T) delegate.getPort(portName, serviceEndpointInterface);
    }

    public <T> T getPort(Class<T> serviceEndpointInterface) {
        return (T) delegate.getPort(serviceEndpointInterface);
    }

    public <T> T getPort(QName portName, Class<T> serviceEndpointInterface, WebServiceFeature... features) {
        return (T) delegate.getPort(portName, serviceEndpointInterface, features);
    }
    
    public <T> T getPort(Class<T> serviceEndpointInterface, WebServiceFeature... features) {
        return (T) delegate.getPort(serviceEndpointInterface, features);
    }
    
    public <T> T getPort(EndpointReference endpointReference, Class<T> serviceEndpointInterface, WebServiceFeature... features) {
        return (T) delegate.getPort(endpointReference, serviceEndpointInterface, features);
    }
    
    public void addPort(QName portName, String bindingId, String endpointAddress) {
        delegate.addPort(portName, bindingId, endpointAddress);
    }

    public <T>Dispatch<T> createDispatch(QName portName, Class<T> type, Mode mode) {
        return delegate.createDispatch(portName, type, mode);
    }

    public Dispatch<Object> createDispatch(QName portName, JAXBContext context, Mode mode) {
        return delegate.createDispatch(portName, context, mode);
    }

    public <T> Dispatch<T> createDispatch(QName portName, Class<T> type, Service.Mode mode, WebServiceFeature... features) {
        return delegate.createDispatch(portName, type, mode, features);
    }
    
    public <T> Dispatch<T> createDispatch(EndpointReference endpointReference, Class<T> type, Service.Mode mode, WebServiceFeature... features) {
        return delegate.createDispatch(endpointReference, type, mode, features);
    }
    
    public Dispatch<Object> createDispatch(QName portName, JAXBContext context, Service.Mode mode, WebServiceFeature... features) {
        return delegate.createDispatch(portName, context, mode, features);
    }
    
    public Dispatch<Object> createDispatch(EndpointReference endpointReference, JAXBContext context, Service.Mode mode, WebServiceFeature... features) {
        return delegate.createDispatch(endpointReference, context, mode, features);
    }
    
    public QName getServiceName() {
        return delegate.getServiceName();
    }

    public Iterator<QName> getPorts() {
        return delegate.getPorts();
    }

    public URL getWSDLDocumentLocation() {
        return delegate.getWSDLDocumentLocation();
    }

    public HandlerResolver getHandlerResolver() {
        return delegate.getHandlerResolver();
    }

    public void setHandlerResolver(HandlerResolver handlerResolver) {
        delegate.setHandlerResolver(handlerResolver);
    }

    public Executor getExecutor() {
        return delegate.getExecutor();
    }

    public void setExecutor(Executor executor) {
        delegate.setExecutor(executor);
    }

    public static Service create(URL wsdlDocumentLocation, QName serviceName) {
        return new Service(wsdlDocumentLocation, serviceName);
    }

    public static Service create(QName serviceName) {
        return new Service(null, serviceName);
    }
    /**
     * @since 2.2
     */
    public static Service create(URL url, QName sn, jakarta.xml.ws.WebServiceFeature ... features) {
        return new Service(url, sn,  features);
    }
    /**
     * @since 2.2
     */
    public static Service create(QName sn, WebServiceFeature ... features) {
        return new Service(null, sn,  features);        
    }


    private ServiceDelegate delegate;
}
