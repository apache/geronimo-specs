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

package jakarta.websocket;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The ClientEndpointConfig is a special kind of endpoint configuration object that contains web socket configuration
 * information specific only to client endpoints. Developers deploying programmatic client endpoints can create
 * instances of this configuration by using a ClientEndpointConfig.Builder. Developers can override some of the
 * configuration operations by providing an implementation of ClientEndpointConfig.Configurator.
 * 
 */
public interface ClientEndpointConfig extends EndpointConfig {

    /**
     * The ClientEndpointConfig.Builder is a class used for creating ClientEndpointConfig objects for the purposes of
     * deploying a client endpoint. Here are some examples: Building a plain configuration with no encoders, decoders,
     * subprotocols or extensions. ClientEndpointConfig cec = ClientEndpointConfig.Builder.create().build(); Building a
     * configuration with no subprotocols and a custom configurator.
     * <p/>
     * <code>
     *  ClientEndpointConfig customCec = ClientEndpointConfig.Builder.create()
     *    .preferredSubprotocols(mySubprotocols)
     *    .configurator(new MyClientConfigurator())
     *    .build();
     * </code>
     * 
     */
    public static final class Builder {

        private Configurator configurator;
        private List<String> preferredSubprotocols;
        private List<Extension> extensions;
        private List<Class<? extends Encoder>> encoders;
        private List<Class<? extends Decoder>> decoders;
        private Map<String, Object> userProperties = new HashMap<String, Object>();

        /**
         * Creates a new builder object with no subprotocols, extensions, encoders, decoders and a null configurator.
         * 
         * @return a new builder object
         */
        public static Builder create() {
            return new Builder();
        }

        /**
         * Builds a configuration object using the attributes set on this builder.
         * 
         * @return a new configuration object
         */
        public ClientEndpointConfig build() {
            if (decoders == null)
                decoders = Collections.emptyList();
            if (encoders == null)
                encoders = Collections.emptyList();
            if (extensions == null)
                extensions = Collections.emptyList();
            if (preferredSubprotocols == null)
                preferredSubprotocols = Collections.emptyList();
            
            return new ClientEndpointConfig() {

                public List<Class<? extends Encoder>> getEncoders() {
                    return encoders;
                }

                public List<Class<? extends Decoder>> getDecoders() {
                    return decoders;
                }

                public Map<String, Object> getUserProperties() {
                    return userProperties;
                }

                public List<String> getPreferredSubprotocols() {
                    return preferredSubprotocols;
                }

                public List<Extension> getExtensions() {
                    return extensions;
                }

                public Configurator getConfigurator() {
                    return configurator;
                }

            };
        }

        /**
         * Sets the configurator object for the configuration this builder will build.
         * 
         * @param clientEndpointConfigurator
         *            the configurator
         * @return the builder instance
         */
        public Builder configurator(Configurator clientEndpointConfigurator) {
            this.configurator = clientEndpointConfigurator;
            return this;
        }

        /**
         * Set the preferred sub protocols for the configuration this builder will build. The list is treated in order
         * of preference, favorite first, that this client would like to use for its sessions.
         * 
         * @param preferredSubprotocols
         *            the preferred subprotocol names.
         * @return the builder instance
         */
        public Builder preferredSubprotocols(List<String> preferredSubprotocols) {
            this.preferredSubprotocols = preferredSubprotocols;
            return this;
        }

        /**
         * Set the extensions for the configuration this builder will build. The list is treated in order of preference,
         * favorite first, that the client would like to use for its sessions.
         * 
         * @param extensions
         *            the extensions
         * @return the builder instance
         */
        public ClientEndpointConfig.Builder extensions(List<Extension> extensions) {
            this.extensions = extensions;
            return this;
        }

        /**
         * Assign the list of encoder implementation classes the client will use.
         * 
         * @param encoders
         *            the encoder implementation classes
         * @return the builder instance
         */
        public Builder encoders(List<Class<? extends Encoder>> encoders) {
            this.encoders = encoders;
            return this;
        }

        /**
         * Assign the list of decoder implementation classes the client will use.
         * 
         * @param decoders
         *            the decoder implementation classes
         * @return the builder instance
         */
        public Builder decoders(List<Class<? extends Decoder>> decoders) {
            this.decoders = decoders;
            return this;
        }
    }

    /**
     * The Configurator class may be extended by developers who want to provide custom configuration algorithms, such as
     * intercepting the opening handshake, or providing arbitrary methods and algorithms that can be accessed from each
     * endpoint instance configured with this configurator.
     * 
     */
    public static class Configurator {

        public Configurator() {
            // no-op
        }

        /**
         * This method is called by the implementation after it has formulated the handshake request that will be used
         * to initiate the connection to the server, but before it has sent any part of the request. This allows the
         * developer to inspect and modify the handshake request headers prior to the start of the handshake
         * interaction.
         * 
         * @param headers
         *            the mutable map of handshake request headers the implementation is about to send to start the
         *            handshake interaction.
         */
        public void beforeRequest(Map<String, List<String>> headers) {
            // no-op
        }

        /**
         * This method is called by the implementation after it has received a handshake response from the server as a
         * result of a handshake interaction it initiated. The developer may implement this method in order to inspect
         * the returning handshake response.
         * 
         * @param hr
         *            the handshake response sent by the server.
         */
        public void afterResponse(HandshakeResponse hr) {
            // no-op
        }
    }

    /**
     * Return the ordered list of sub protocols a client endpoint would like to use, in order of preference, favorite
     * first that this client would like to use for its sessions. This list is used to generate the
     * Sec-WebSocket-Protocol header in the opening handshake for clients using this configuration. The first protocol
     * name is the most preferred. See <a href="http://tools.ietf.org/html/rfc6455#section-4.1">Client Opening
     * Handshake</a>.
     * 
     * @return the list of the preferred subprotocols, the empty list if there are none
     */
    List<String> getPreferredSubprotocols();

    /**
     * Return the extensions, in order of preference, favorite first, that this client would like to use for its
     * sessions. These are the extensions that will be used to populate the Sec-WebSocket-Extensions header in the
     * opening handshake for clients using this configuration. The first extension in the list is the most preferred
     * extension. See <a href="http://tools.ietf.org/html/rfc6455#section-9.1">Negotiating Extensions</a>.
     * 
     * @return the list of extensions, the empty list if there are none.
     */
    List<Extension> getExtensions();

    /**
     * Return the custom configurator for this configuration. If the developer did not provide one, the platform default
     * configurator is returned.
     * 
     * @return the configurator in use with this configuration.
     */
    ClientEndpointConfig.Configurator getConfigurator();
}
