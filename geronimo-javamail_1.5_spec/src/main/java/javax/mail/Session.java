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

package javax.mail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.WeakHashMap;

import org.apache.geronimo.mail.MailProviderRegistry;
import org.apache.geronimo.osgi.locator.ProviderLocator;

/**
 * OK, so we have a final class in the API with a heck of a lot of implementation required...
 * let's try and figure out what it is meant to do.
 * <p/>
 * It is supposed to collect together properties and defaults so that they can be
 * shared by multiple applications on a desktop; with process isolation and no
 * real concept of shared memory, this seems challenging. These properties and
 * defaults rely on system properties, making management in a app server harder,
 * and on resources loaded from "mail.jar" which may lead to skew between
 * differnet independent implementations of this API.
 *
 * @version $Rev$ $Date$
 */
public final class Session {
    private static final Class[] PARAM_TYPES = {Session.class, URLName.class};
    private static final WeakHashMap addressMapsByClassLoader = new WeakHashMap();
    private static Session DEFAULT_SESSION;

    private final Map passwordAuthentications = new HashMap();

    private final Properties properties;
    private final Authenticator authenticator;
    private boolean debug;
    private PrintStream debugOut = System.out;

    private static final WeakHashMap providersByClassLoader = new WeakHashMap();

    /**
     * No public constrcutor allowed.
     */
    private Session(final Properties properties, final Authenticator authenticator) {
        this.properties = properties;
        this.authenticator = authenticator;
        debug = Boolean.valueOf(properties.getProperty("mail.debug")).booleanValue();
    }

    /**
     * Create a new session initialized with the supplied properties which uses the supplied authenticator.
     * Clients should ensure the properties listed in Appendix A of the JavaMail specification are
     * set as the defaults are unlikey to work in most scenarios; particular attention should be given
     * to:
     * <ul>
     * <li>mail.store.protocol</li>
     * <li>mail.transport.protocol</li>
     * <li>mail.host</li>
     * <li>mail.user</li>
     * <li>mail.from</li>
     * </ul>
     *
     * @param properties    the session properties
     * @param authenticator an authenticator for callbacks to the user
     * @return a new session
     */
    public static Session getInstance(Properties properties, final Authenticator authenticator) {
        // if we have a properties bundle, we need a copy of the provided one
        if (properties != null) {
            properties = (Properties)properties.clone();
        }

        return new Session(properties, authenticator);
    }

    /**
     * Create a new session initialized with the supplied properties with no authenticator.
     *
     * @param properties the session properties
     * @return a new session
     * @see #getInstance(java.util.Properties, Authenticator)
     */
    public static Session getInstance(final Properties properties) {
        return getInstance(properties, null);
    }

    /**
     * Get the "default" instance assuming no authenticator is required.
     *
     * @param properties the session properties
     * @return if "default" session
     * @throws SecurityException if the does not have permission to access the default session
     */
    public synchronized static Session getDefaultInstance(final Properties properties) {
        return getDefaultInstance(properties, null);
    }

    /**
     * Get the "default" session.
     * If there is not current "default", a new Session is created and installed as the default.
     *
     * @param properties
     * @param authenticator
     * @return if "default" session
     * @throws SecurityException if the does not have permission to access the default session
     */
    public synchronized static Session getDefaultInstance(final Properties properties, final Authenticator authenticator) {
        if (DEFAULT_SESSION == null) {
            DEFAULT_SESSION = getInstance(properties, authenticator);
        } else {
            if (authenticator != DEFAULT_SESSION.authenticator) {
                if (authenticator == null || DEFAULT_SESSION.authenticator == null || authenticator.getClass().getClassLoader() != DEFAULT_SESSION.authenticator.getClass().getClassLoader()) {
                    throw new SecurityException();
                }
            }
            // todo we should check with the SecurityManager here as well
        }
        return DEFAULT_SESSION;
    }

    /**
     * Enable debugging for this session.
     * Debugging can also be enabled by setting the "mail.debug" property to true when
     * the session is being created.
     *
     * @param debug the debug setting
     */
    public void setDebug(final boolean debug) {
        this.debug = debug;
    }

    /**
     * Get the debug setting for this session.
     *
     * @return the debug setting
     */
    public boolean getDebug() {
        return debug;
    }

    /**
     * Set the output stream where debug information should be sent.
     * If set to null, System.out will be used.
     *
     * @param out the stream to write debug information to
     */
    public void setDebugOut(final PrintStream out) {
        debugOut = out == null ? System.out : out;
    }

    /**
     * Return the debug output stream.
     *
     * @return the debug output stream
     */
    public PrintStream getDebugOut() {
        return debugOut;
    }

    /**
     * Return the list of providers available to this application.
     * This method searches for providers that are defined in the javamail.providers
     * and javamail.default.providers resources available through the current context
     * classloader, or if that is not available, the classloader that loaded this class.
     * <p/>
     * As searching for providers is potentially expensive, this implementation maintains
     * a WeakHashMap of providers indexed by ClassLoader.
     *
     * @return an array of providers
     */
    public Provider[] getProviders() {
        final ProviderInfo info = getProviderInfo();
        return (Provider[]) info.all.toArray(new Provider[info.all.size()]);
    }

    /**
     * Return the provider for a specific protocol.
     * This implementation initially looks in the Session properties for an property with the name
     * "mail.<protocol>.class"; if found it attempts to create an instance of the class named in that
     * property throwing a NoSuchProviderException if the class cannot be loaded.
     * If this property is not found, it searches the providers returned by {@link #getProviders()}
     * for a entry for the specified protocol.
     *
     * @param protocol the protocol to get a provider for
     * @return a provider for that protocol
     * @throws NoSuchProviderException
     */
    public Provider getProvider(final String protocol) throws NoSuchProviderException {
        final ProviderInfo info = getProviderInfo();
        Provider provider = null;
        final String providerName = properties.getProperty("mail." + protocol + ".class");
        if (providerName != null) {
            provider = (Provider) info.byClassName.get(providerName);
            if (debug) {
                writeDebug("DEBUG: new provider loaded: " + provider.toString());
            }
        }

        // if not able to locate this by class name, just grab a registered protocol.
        if (provider == null) {
            provider = (Provider) info.byProtocol.get(protocol);
        }

        if (provider == null) {
            throw new NoSuchProviderException("Unable to locate provider for protocol: " + protocol);
        }
        if (debug) {
            writeDebug("DEBUG: getProvider() returning provider " + provider.toString());
        }
        return provider;
    }

    /**
     * Make the supplied Provider the default for its protocol.
     *
     * @param provider the new default Provider
     * @throws NoSuchProviderException
     */
    public void setProvider(final Provider provider) throws NoSuchProviderException {
        final ProviderInfo info = getProviderInfo();
        info.byProtocol.put(provider.getProtocol(), provider);
    }

    /**
     * Return a Store for the default protocol defined by the mail.store.protocol property.
     *
     * @return the store for the default protocol
     * @throws NoSuchProviderException
     */
    public Store getStore() throws NoSuchProviderException {
        final String protocol = properties.getProperty("mail.store.protocol");
        if (protocol == null) {
            throw new NoSuchProviderException("mail.store.protocol property is not set");
        }
        return getStore(protocol);
    }

    /**
     * Return a Store for the specified protocol.
     *
     * @param protocol the protocol to get a Store for
     * @return a Store
     * @throws NoSuchProviderException if no provider is defined for the specified protocol
     */
    public Store getStore(final String protocol) throws NoSuchProviderException {
        final Provider provider = getProvider(protocol);
        return getStore(provider);
    }

    /**
     * Return a Store for the protocol specified in the given URL
     *
     * @param url the URL of the Store
     * @return a Store
     * @throws NoSuchProviderException if no provider is defined for the specified protocol
     */
    public Store getStore(final URLName url) throws NoSuchProviderException {
        return (Store) getService(getProvider(url.getProtocol()), url);
    }

    /**
     * Return the Store specified by the given provider.
     *
     * @param provider the provider to create from
     * @return a Store
     * @throws NoSuchProviderException if there was a problem creating the Store
     */
    public Store getStore(final Provider provider) throws NoSuchProviderException {
        if (Provider.Type.STORE != provider.getType()) {
            throw new NoSuchProviderException("Not a Store Provider: " + provider);
        }
        return (Store) getService(provider, null);
    }

    /**
     * Return a closed folder for the supplied URLName, or null if it cannot be obtained.
     * <p/>
     * The scheme portion of the URL is used to locate the Provider and create the Store;
     * the returned Store is then used to obtain the folder.
     *
     * @param name the location of the folder
     * @return the requested folder, or null if it is unavailable
     * @throws NoSuchProviderException if there is no provider
     * @throws MessagingException      if there was a problem accessing the Store
     */
    public Folder getFolder(final URLName name) throws MessagingException {
        final Store store = getStore(name);
        return store.getFolder(name);
    }

    /**
     * Return a Transport for the default protocol specified by the
     * <code>mail.transport.protocol</code> property.
     *
     * @return a Transport
     * @throws NoSuchProviderException
     */
    public Transport getTransport() throws NoSuchProviderException {
        final String protocol = properties.getProperty("mail.transport.protocol");
        if (protocol == null) {
            throw new NoSuchProviderException("mail.transport.protocol property is not set");
        }
        return getTransport(protocol);
    }

    /**
     * Return a Transport for the specified protocol.
     *
     * @param protocol the protocol to use
     * @return a Transport
     * @throws NoSuchProviderException
     */
    public Transport getTransport(final String protocol) throws NoSuchProviderException {
        final Provider provider = getProvider(protocol);
        return getTransport(provider);
    }

    /**
     * Return a transport for the protocol specified in the URL.
     *
     * @param name the URL whose scheme specifies the protocol
     * @return a Transport
     * @throws NoSuchProviderException
     */
    public Transport getTransport(final URLName name) throws NoSuchProviderException {
        return (Transport) getService(getProvider(name.getProtocol()), name);
    }

    /**
     * Return a transport for the protocol associated with the type of this address.
     *
     * @param address the address we are trying to deliver to
     * @return a Transport
     * @throws NoSuchProviderException
     */
    public Transport getTransport(final Address address) throws NoSuchProviderException {
        final String type = address.getType();
        // load the address map from the resource files.
        final Map addressMap = getAddressMap();
        final String protocolName = (String)addressMap.get(type);
        if (protocolName == null) {
            throw new NoSuchProviderException("No provider for address type " + type);
        }
        return getTransport(protocolName);
    }

    /**
     * Return the Transport specified by a Provider
     *
     * @param provider the defining Provider
     * @return a Transport
     * @throws NoSuchProviderException
     */
    public Transport getTransport(final Provider provider) throws NoSuchProviderException {
        return (Transport) getService(provider, null);
    }

    /**
     * Set the password authentication associated with a URL.
     *
     * @param name          the url
     * @param authenticator the authenticator
     */
    public void setPasswordAuthentication(final URLName name, final PasswordAuthentication authenticator) {
        if (authenticator == null) {
            passwordAuthentications.remove(name);
        } else {
            passwordAuthentications.put(name, authenticator);
        }
    }

    /**
     * Get the password authentication associated with a URL
     *
     * @param name the URL
     * @return any authenticator for that url, or null if none
     */
    public PasswordAuthentication getPasswordAuthentication(final URLName name) {
        return (PasswordAuthentication) passwordAuthentications.get(name);
    }

    /**
     * Call back to the application supplied authenticator to get the needed username add password.
     *
     * @param host            the host we are trying to connect to, may be null
     * @param port            the port on that host
     * @param protocol        the protocol trying to be used
     * @param prompt          a String to show as part of the prompt, may be null
     * @param defaultUserName the default username, may be null
     * @return the authentication information collected by the authenticator; may be null
     */
    public PasswordAuthentication requestPasswordAuthentication(final InetAddress host, final int port, final String protocol, final String prompt, final String defaultUserName) {
        if (authenticator == null) {
            return null;
        }
        return authenticator.authenticate(host, port, protocol, prompt, defaultUserName);
    }

    /**
     * Return the properties object for this Session; this is a live collection.
     *
     * @return the properties for the Session
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     * Return the specified property.
     *
     * @param property the property to get
     * @return its value, or null if not present
     */
    public String getProperty(final String property) {
        return getProperties().getProperty(property);
    }


    /**
     * Add a provider to the Session managed provider list.
     *
     * @param provider The new provider to add.
     */
    public synchronized void addProvider(final Provider provider) {
        final ProviderInfo info = getProviderInfo();
        info.addProvider(provider);
    }



    /**
     * Add a mapping between an address type and a protocol used
     * to process that address type.
     *
     * @param addressType
     *                 The address type identifier.
     * @param protocol The protocol name mapping.
     */
    public void setProtocolForAddress(final String addressType, final String protocol) {
        final Map addressMap = getAddressMap();

        // no protocol specified is a removal
        if (protocol == null) {
            addressMap.remove(addressType);
        }
        else {
            addressMap.put(addressType, protocol);
        }
    }


    private Service getService(final Provider provider, URLName name) throws NoSuchProviderException {
        try {
            if (name == null) {
                name = new URLName(provider.getProtocol(), null, -1, null, null, null);
            }
            final ClassLoader cl = getClassLoader();
            Class clazz = null;
            try {
                clazz = ProviderLocator.loadClass(provider.getClassName(), this.getClass(), cl);
            } catch (final ClassNotFoundException e) {
                throw (NoSuchProviderException) new NoSuchProviderException("Unable to load class for provider: " + provider).initCause(e);
            }
            final Constructor ctr = clazz.getConstructor(PARAM_TYPES);
            return(Service) ctr.newInstance(new Object[]{this, name});
        } catch (final NoSuchMethodException e) {
            throw (NoSuchProviderException) new NoSuchProviderException("Provider class does not have a constructor(Session, URLName): " + provider).initCause(e);
        } catch (final InstantiationException e) {
            throw (NoSuchProviderException) new NoSuchProviderException("Unable to instantiate provider class: " + provider).initCause(e);
        } catch (final IllegalAccessException e) {
            throw (NoSuchProviderException) new NoSuchProviderException("Unable to instantiate provider class: " + provider).initCause(e);
        } catch (final InvocationTargetException e) {
            throw (NoSuchProviderException) new NoSuchProviderException("Exception from constructor of provider class: " + provider).initCause(e.getCause());
        }
    }

    private ProviderInfo getProviderInfo() {
        final ClassLoader cl = getClassLoader();
        synchronized (providersByClassLoader) {
            ProviderInfo info = (ProviderInfo) providersByClassLoader.get(cl);
            if (info == null) {
                info = loadProviders(cl);
            }
            return info;
        }
    }

    private Map getAddressMap() {
        final ClassLoader cl = getClassLoader();
        Map addressMap = (Map)addressMapsByClassLoader.get(cl);
        if (addressMap == null) {
            addressMap = loadAddressMap(cl);
        }
        return addressMap;
    }


    /**
     * Resolve a class loader used to resolve context resources.  The
     * class loader used is either a current thread context class
     * loader (if set), the class loader used to load an authenticator
     * we've been initialized with, or the class loader used to load
     * this class instance (which may be a subclass of Session).
     *
     * @return The class loader used to load resources.
     */
    private ClassLoader getClassLoader() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            if (authenticator != null) {
                cl = authenticator.getClass().getClassLoader();
            }
            else {
                cl = this.getClass().getClassLoader();
            }
        }
        return cl;
    }

    private ProviderInfo loadProviders(final ClassLoader cl) {
        // we create a merged map from reading all of the potential address map entries.  The locations
        // searched are:
        //   1.   java.home/lib/javamail.address.map
        //   2. META-INF/javamail.address.map
        //   3. META-INF/javamail.default.address.map
        //
        final ProviderInfo info = new ProviderInfo();

        // NOTE:  Unlike the addressMap, we process these in the defined order.  The loading routine
        // will not overwrite entries if they already exist in the map.

        try {
            final File file = new File(System.getProperty("java.home"), "lib/javamail.providers");
            final InputStream is = new FileInputStream(file);
            try {
                loadProviders(info, is);
                if (debug) {
                    writeDebug("Loaded lib/javamail.providers from " + file.toString());
                }
            } finally{
                is.close();
            }
        } catch (final SecurityException e) {
            // ignore
        } catch (final IOException e) {
            // ignore
        }

        try {
            final Enumeration e = cl.getResources("META-INF/javamail.providers");
            while (e.hasMoreElements()) {
                final URL url = (URL) e.nextElement();
                if (debug) {
                    writeDebug("Loading META-INF/javamail.providers from " + url.toString());
                }
                final InputStream is = url.openStream();
                try {
                    loadProviders(info, is);
                } finally{
                    is.close();
                }
            }
        } catch (final SecurityException e) {
            // ignore
        } catch (final IOException e) {
            // ignore
        }

        // we could be running in an OSGi environment, so there might be some globally defined
        // providers
        try {
            final Collection<URL> l = MailProviderRegistry.getProviders();
            for (final URL url : l) {
                if (debug) {
                    writeDebug("Loading META-INF/javamail.providers from " + url.toString());
                }
                final InputStream is = url.openStream();
                try {
                    loadProviders(info, is);
                } finally{
                    is.close();
                }
            }
        } catch (final SecurityException e) {
            // ignore
        } catch (final IOException e) {
            // ignore
        }

        try {
            final Enumeration e = cl.getResources("META-INF/javamail.default.providers");
            while (e.hasMoreElements()) {
                final URL url = (URL) e.nextElement();
                if (debug) {
                    writeDebug("Loading javamail.default.providers from " + url.toString());
                }

                final InputStream is = url.openStream();
                try {
                    loadProviders(info, is);
                } finally{
                    is.close();
                }
            }
        } catch (final SecurityException e) {
            // ignore
        } catch (final IOException e) {
            // ignore
        }

        // we could be running in an OSGi environment, so there might be some globally defined
        // providers
        try {
            final Collection<URL> l = MailProviderRegistry.getDefaultProviders();
            for (final URL url : l) {
                if (debug) {
                    writeDebug("Loading META-INF/javamail.providers from " + url.toString());
                }
                final InputStream is = url.openStream();
                try {
                    loadProviders(info, is);
                } finally{
                    is.close();
                }
            }
        } catch (final SecurityException e) {
            // ignore
        } catch (final IOException e) {
            // ignore
        }

        // make sure this is added to the global map.
        providersByClassLoader.put(cl, info);

        return info;
    }

    private void loadProviders(final ProviderInfo info, final InputStream is) throws IOException {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = reader.readLine()) != null) {
            // Lines beginning with "#" are just comments.
            if (line.startsWith("#")) {
                continue;
            }

            final StringTokenizer tok = new StringTokenizer(line, ";");
            String protocol = null;
            Provider.Type type = null;
            String className = null;
            String vendor = null;
            String version = null;
            while (tok.hasMoreTokens()) {
                final String property = tok.nextToken();
                final int index = property.indexOf('=');
                if (index == -1) {
                    continue;
                }
                final String key = property.substring(0, index).trim().toLowerCase();
                final String value = property.substring(index+1).trim();
                if (protocol == null && "protocol".equals(key)) {
                    protocol = value;
                } else if (type == null && "type".equals(key)) {
                    if ("store".equals(value)) {
                        type = Provider.Type.STORE;
                    } else if ("transport".equals(value)) {
                        type = Provider.Type.TRANSPORT;
                    }
                } else if (className == null && "class".equals(key)) {
                    className = value;
                } else if ("vendor".equals(key)) {
                    vendor = value;
                } else if ("version".equals(key)) {
                    version = value;
                }
            }
            if (protocol == null || type == null || className == null) {
                //todo should we log a warning?
                continue;
            }

            if (debug) {
                writeDebug("DEBUG: loading new provider protocol=" + protocol + ", className=" + className + ", vendor=" + vendor + ", version=" + version);
            }
            final Provider provider = new Provider(type, protocol, className, vendor, version);
            // add to the info list.
            info.addProvider(provider);
        }
    }

    /**
     * Load up an address map associated with a using class loader
     * instance.
     *
     * @param cl     The class loader used to resolve the address map.
     *
     * @return A map containing the entries associated with this classloader
     *         instance.
     */
    private static Map loadAddressMap(final ClassLoader cl) {
        // we create a merged map from reading all of the potential address map entries.  The locations
        // searched are:
        //   1.   java.home/lib/javamail.address.map
        //   2. META-INF/javamail.address.map
        //   3. META-INF/javamail.default.address.map
        //
        // if all of the above searches fail, we just set up some "default" defaults.

        // the format of the address.map file is defined as a property file.  We can cheat and
        // just use Properties.load() to read in the files.
        final Properties addressMap = new Properties();

        // add this to the tracking map.
        addressMapsByClassLoader.put(cl, addressMap);

        // NOTE:  We are reading these resources in reverse order of what's cited above.  This allows
        // user defined entries to overwrite default entries if there are similarly named items.

        try {
            final Enumeration e = cl.getResources("META-INF/javamail.default.address.map");
            while (e.hasMoreElements()) {
                final URL url = (URL) e.nextElement();
                final InputStream is = url.openStream();
                try {
                    // load as a property file
                    addressMap.load(is);
                } finally{
                    is.close();
                }
            }
        } catch (final SecurityException e) {
            // ignore
        } catch (final IOException e) {
            // ignore
        }


        try {
            final Enumeration e = cl.getResources("META-INF/javamail.address.map");
            while (e.hasMoreElements()) {
                final URL url = (URL) e.nextElement();
                final InputStream is = url.openStream();
                try {
                    // load as a property file
                    addressMap.load(is);
                } finally{
                    is.close();
                }
            }
        } catch (final SecurityException e) {
            // ignore
        } catch (final IOException e) {
            // ignore
        }


        try {
            final File file = new File(System.getProperty("java.home"), "lib/javamail.address.map");
            final InputStream is = new FileInputStream(file);
            try {
                // load as a property file
                addressMap.load(is);
            } finally{
                is.close();
            }
        } catch (final SecurityException e) {
            // ignore
        } catch (final IOException e) {
            // ignore
        }

        try {
            final Enumeration e = cl.getResources("META-INF/javamail.address.map");
            while (e.hasMoreElements()) {
                final URL url = (URL) e.nextElement();
                final InputStream is = url.openStream();
                try {
                    // load as a property file
                    addressMap.load(is);
                } finally{
                    is.close();
                }
            }
        } catch (final SecurityException e) {
            // ignore
        } catch (final IOException e) {
            // ignore
        }


        // if unable to load anything, at least create the MimeMessage-smtp protocol mapping.
        if (addressMap.isEmpty()) {
            addressMap.put("rfc822", "smtp");
        }

        return addressMap;
    }

    /**
     * Private convenience routine for debug output.
     *
     * @param msg    The message to write out to the debug stream.
     */
    private void writeDebug(final String msg) {
        debugOut.println(msg);
    }


    private static class ProviderInfo {
        private final Map byClassName = new HashMap();
        private final Map byProtocol = new HashMap();
        private final List all = new ArrayList();

        public void addProvider(final Provider provider) {
            final String className = provider.getClassName();

            if (!byClassName.containsKey(className)) {
                byClassName.put(className, provider);
            }

            final String protocol = provider.getProtocol();
            if (!byProtocol.containsKey(protocol)) {
                byProtocol.put(protocol, provider);
            }
            all.add(provider);
        }
    }
}
