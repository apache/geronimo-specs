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

package javax.servlet;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.Map;
import java.util.Set;

import javax.servlet.descriptor.JspConfigDescriptor;


/**
 * Defines a set of methods that a servlet uses to communicate with its
 * servlet container, for example, to get the MIME type of a file, dispatch
 * requests, or write to a log file.
 * <p/>
 * <p>There is one context per "web application" per Java Virtual Machine.  (A
 * "web application" is a collection of servlets and content installed under a
 * specific subset of the server's URL namespace such as <code>/catalog</code>
 * and possibly installed via a <code>.war</code> file.)
 * <p/>
 * <p>In the case of a web
 * application marked "distributed" in its deployment descriptor, there will
 * be one context instance for each virtual machine.  In this situation, the
 * context cannot be used as a location to share global information (because
 * the information won't be truly global).  Use an external resource like
 * a database instead.
 * <p/>
 * <p>The <code>ServletContext</code> object is contained within
 * the {@link ServletConfig} object, which the Web server provides the
 * servlet when the servlet is initialized.
 *
 * @version $Rev$ $Date$
 * @see Servlet#getServletConfig
 * @see ServletConfig#getServletContext
 */

public interface ServletContext {

    /**
     * @since Servlet 3.0
     */
    String ORDERED_LIBS = "javax.servlet.context.orderedLibs";
    /**
     * @since Servlet 3.0
     */
    String TEMPDIR = "javax.servlet.context.tempdir";

    /**
     * @return context path for this web app or "" for the / context
     * @since Servlet 2.5
     */
    String getContextPath();

    /**
     * Returns a <code>ServletContext</code> object that
     * corresponds to a specified URL on the server.
     * <p/>
     * <p>This method allows servlets to gain
     * access to the context for various parts of the server, and as
     * needed obtain {@link RequestDispatcher} objects from the context.
     * The given path must be begin with "/", is interpreted relative
     * to the server's document root and is matched against the context roots of
     * other web applications hosted on this container.
     * <p/>
     * <p>In a security conscious environment, the servlet container may
     * return <code>null</code> for a given URL.
     *
     * @param uripath a <code>String</code> specifying the context path of
     *                another web application in the container.
     * @return the <code>ServletContext</code> object that
     *         corresponds to the named URL, or null if either
     *         none exists or the container wishes to restrict
     *         this access.
     * @see RequestDispatcher
     */
    ServletContext getContext(String uripath);

    /**
     * Returns the major version of the Java Servlet API that this
     * servlet container supports. All implementations that comply
     * with Version 3.0 must have this method
     * return the integer 3.
     *
     * @return 3
     */
    int getMajorVersion();

    /**
     * Returns the minor version of the Servlet API that this
     * servlet container supports. All implementations that comply
     * with Version 3.0 must have this method
     * return the integer 0.
     *
     * @return 0
     */
    int getMinorVersion();

    /**
     * Returns the MIME type of the specified file, or <code>null</code> if
     * the MIME type is not known. The MIME type is determined
     * by the configuration of the servlet container, and may be specified
     * in a web application deployment descriptor. Common MIME
     * types are <code>"text/html"</code> and <code>"image/gif"</code>.
     *
     * @param file a <code>String</code> specifying the name
     *             of a file
     * @return a <code>String</code> specifying the file's MIME type
     */
    String getMimeType(String file);

    /**
     * Returns a directory-like listing of all the paths to resources within the web application whose longest sub-path
     * matches the supplied path argument. Paths indicating subdirectory paths end with a '/'. The returned paths are all
     * relative to the root of the web application and have a leading '/'. For example, for a web application
     * containing<br><br>
     * <p/>
     * /welcome.html<br>
     * /catalog/index.html<br>
     * /catalog/products.html<br>
     * /catalog/offers/books.html<br>
     * /catalog/offers/music.html<br>
     * /customer/login.jsp<br>
     * /WEB-INF/web.xml<br>
     * /WEB-INF/classes/com.acme.OrderServlet.class,<br><br>
     * <p/>
     * getResourcePaths("/") returns {"/welcome.html", "/catalog/", "/customer/", "/WEB-INF/"}<br>
     * getResourcePaths("/catalog/") returns {"/catalog/index.html", "/catalog/products.html", "/catalog/offers/"}.<br>
     *
     * @param path the partial path used to match the resources,
     *             which must start with a /
     * @return a Set containing the directory listing, or null if there are no resources in the web application whose path
     *         begins with the supplied path.
     * @since Servlet 2.3
     */
    Set<String> getResourcePaths(String path);

    /**
     * Returns a URL to the resource that is mapped to a specified
     * path. The path must begin with a "/" and is interpreted
     * as relative to the current context root.
     * <p/>
     * <p>This method allows the servlet container to make a resource
     * available to servlets from any source. Resources
     * can be located on a local or remote
     * file system, in a database, or in a <code>.war</code> file.
     * <p/>
     * <p>The servlet container must implement the URL handlers
     * and <code>URLConnection</code> objects that are necessary
     * to access the resource.
     * <p/>
     * <p>This method returns <code>null</code>
     * if no resource is mapped to the pathname.
     * <p/>
     * <p>Some containers may allow writing to the URL returned by
     * this method using the methods of the URL class.
     * <p/>
     * <p>The resource content is returned directly, so be aware that
     * requesting a <code>.jsp</code> page returns the JSP source code.
     * Use a <code>RequestDispatcher</code> instead to include results of
     * an execution.
     * <p/>
     * <p>This method has a different purpose than
     * <code>java.lang.Class.getResource</code>,
     * which looks up resources based on a class loader. This
     * method does not use class loaders.
     *
     * @param path a <code>String</code> specifying
     *             the path to the resource
     * @return the resource located at the named path,
     *         or <code>null</code> if there is no resource
     *         at that path
     * @throws MalformedURLException if the pathname is not given in
     *                               the correct form
     */
    URL getResource(String path) throws MalformedURLException;

    /**
     * Returns the resource located at the named path as
     * an <code>InputStream</code> object.
     * <p/>
     * <p>The data in the <code>InputStream</code> can be
     * of any type or length. The path must be specified according
     * to the rules given in <code>getResource</code>.
     * This method returns <code>null</code> if no resource exists at
     * the specified path.
     * <p/>
     * <p>Meta-information such as content length and content type
     * that is available via <code>getResource</code>
     * method is lost when using this method.
     * <p/>
     * <p>The servlet container must implement the URL handlers
     * and <code>URLConnection</code> objects necessary to access
     * the resource.
     * <p/>
     * <p>This method is different from
     * <code>java.lang.Class.getResourceAsStream</code>,
     * which uses a class loader. This method allows servlet containers
     * to make a resource available
     * to a servlet from any location, without using a class loader.
     *
     * @param path a <code>String</code> specifying the path
     *             to the resource
     * @return the <code>InputStream</code> returned to the
     *         servlet, or <code>null</code> if no resource
     *         exists at the specified path
     */
    InputStream getResourceAsStream(String path);

    /**
     * Returns a {@link RequestDispatcher} object that acts
     * as a wrapper for the resource located at the given path.
     * A <code>RequestDispatcher</code> object can be used to forward
     * a request to the resource or to include the resource in a response.
     * The resource can be dynamic or static.
     * <p/>
     * <p>The pathname must begin with a "/" and is interpreted as relative
     * to the current context root.  Use <code>getContext</code> to obtain
     * a <code>RequestDispatcher</code> for resources in foreign contexts.
     * This method returns <code>null</code> if the <code>ServletContext</code>
     * cannot return a <code>RequestDispatcher</code>.
     *
     * @param path a <code>String</code> specifying the pathname
     *             to the resource
     * @return a <code>RequestDispatcher</code> object
     *         that acts as a wrapper for the resource
     *         at the specified path, or <code>null</code> if
     *         the <code>ServletContext</code> cannot return
     *         a <code>RequestDispatcher</code>
     * @see RequestDispatcher
     * @see ServletContext#getContext
     */
    RequestDispatcher getRequestDispatcher(String path);

    /**
     * Returns a {@link RequestDispatcher} object that acts
     * as a wrapper for the named servlet.
     * <p/>
     * <p>Servlets (and JSP pages also) may be given names via server
     * administration or via a web application deployment descriptor.
     * A servlet instance can determine its name using
     * {@link ServletConfig#getServletName}.
     * <p/>
     * <p>This method returns <code>null</code> if the
     * <code>ServletContext</code>
     * cannot return a <code>RequestDispatcher</code> for any reason.
     *
     * @param name a <code>String</code> specifying the name
     *             of a servlet to wrap
     * @return a <code>RequestDispatcher</code> object
     *         that acts as a wrapper for the named servlet,
     *         or <code>null</code> if the <code>ServletContext</code>
     *         cannot return a <code>RequestDispatcher</code>
     * @see RequestDispatcher
     * @see ServletContext#getContext
     * @see ServletConfig#getServletName
     */
    RequestDispatcher getNamedDispatcher(String name);

    /**
     * @deprecated As of Java Servlet API 2.1, with no direct replacement.
     *             <p/>
     *             <p>This method was originally defined to retrieve a servlet
     *             from a <code>ServletContext</code>. In this version, this method
     *             always returns <code>null</code> and remains only to preserve
     *             binary compatibility. This method will be permanently removed
     *             in a future version of the Java Servlet API.
     *             <p/>
     *             <p>In lieu of this method, servlets can share information using the
     *             <code>ServletContext</code> class and can perform shared business logic
     *             by invoking methods on common non-servlet classes.
     */
    Servlet getServlet(String name) throws ServletException;

    /**
     * @deprecated As of Java Servlet API 2.0, with no replacement.
     *             <p/>
     *             <p>This method was originally defined to return an <code>Enumeration</code>
     *             of all the servlets known to this servlet context. In this
     *             version, this method always returns an empty enumeration and
     *             remains only to preserve binary compatibility. This method
     *             will be permanently removed in a future version of the Java
     *             Servlet API.
     */
    Enumeration<Servlet> getServlets();

    /**
     * @deprecated As of Java Servlet API 2.1, with no replacement.
     *             <p/>
     *             <p>This method was originally defined to return an
     *             <code>Enumeration</code>
     *             of all the servlet names known to this context. In this version,
     *             this method always returns an empty <code>Enumeration</code> and
     *             remains only to preserve binary compatibility. This method will
     *             be permanently removed in a future version of the Java Servlet API.
     */
    Enumeration<String> getServletNames();

    /**
     * Writes the specified message to a servlet log file, usually
     * an event log. The name and type of the servlet log file is
     * specific to the servlet container.
     *
     * @param msg a <code>String</code> specifying the
     *            message to be written to the log file
     */
    void log(String msg);

    /**
     * @deprecated As of Java Servlet API 2.1, use
     *             {@link #log(String message, Throwable throwable)}
     *             instead.
     *             <p/>
     *             <p>This method was originally defined to write an
     *             exception's stack trace and an explanatory error message
     *             to the servlet log file.
     */
    void log(Exception exception, String msg);

    /**
     * Writes an explanatory message and a stack trace
     * for a given <code>Throwable</code> exception
     * to the servlet log file. The name and type of the servlet log
     * file is specific to the servlet container, usually an event log.
     *
     * @param message   a <code>String</code> that
     *                  describes the error or exception
     * @param throwable the <code>Throwable</code> error
     *                  or exception
     */
    void log(String message, Throwable throwable);

    /**
     * Returns a <code>String</code> containing the real path
     * for a given virtual path. For example, the path "/index.html"
     * returns the absolute file path on the server's filesystem would be
     * served by a request for "http://host/contextPath/index.html",
     * where contextPath is the context path of this ServletContext..
     * <p/>
     * <p>The real path returned will be in a form
     * appropriate to the computer and operating system on
     * which the servlet container is running, including the
     * proper path separators. This method returns <code>null</code>
     * if the servlet container cannot translate the virtual path
     * to a real path for any reason (such as when the content is
     * being made available from a <code>.war</code> archive).
     *
     * @param path a <code>String</code> specifying a virtual path
     * @return a <code>String</code> specifying the real path,
     *         or null if the translation cannot be performed
     */
    String getRealPath(String path);

    /**
     * Returns the name and version of the servlet container on which
     * the servlet is running.
     * <p/>
     * <p>The form of the returned string is
     * <i>servername</i>/<i>versionnumber</i>.
     * For example, the JavaServer Web Development Kit may return the string
     * <code>JavaServer Web Dev Kit/1.0</code>.
     * <p/>
     * <p>The servlet container may return other optional information
     * after the primary string in parentheses, for example,
     * <code>JavaServer Web Dev Kit/1.0 (JDK 1.1.6; Windows NT 4.0 x86)</code>.
     *
     * @return a <code>String</code> containing at least the
     *         servlet container name and version number
     */
    String getServerInfo();

    /**
     * Returns a <code>String</code> containing the value of the named
     * context-wide initialization parameter, or <code>null</code> if the
     * parameter does not exist.
     * <p/>
     * <p>This method can make available configuration information useful
     * to an entire "web application".  For example, it can provide a
     * webmaster's email address or the name of a system that holds
     * critical data.
     *
     * @param name a <code>String</code> containing the name of the
     *             parameter whose value is requested
     * @return a <code>String</code> containing at least the
     *         servlet container name and version number
     * @see ServletConfig#getInitParameter
     */
    String getInitParameter(String name);

    /**
     * Returns the names of the context's initialization parameters as an
     * <code>Enumeration</code> of <code>String</code> objects, or an
     * empty <code>Enumeration</code> if the context has no initialization
     * parameters.
     *
     * @return an <code>Enumeration</code> of <code>String</code>
     *         objects containing the names of the context's
     *         initialization parameters
     * @see ServletConfig#getInitParameter
     */
    Enumeration<String> getInitParameterNames();

    /**
     * Set the init parameter if it is not already set.
     *
     * @param name  of the init parameter to set
     * @param value new value
     * @return whether it was set
     * @since 3.0
     */
    boolean setInitParameter(String name, String value);

    /**
     * Returns the servlet container attribute with the given name,
     * or <code>null</code> if there is no attribute by that name.
     * An attribute allows a servlet container to give the
     * servlet additional information not
     * already provided by this interface. See your
     * server documentation for information about its attributes.
     * A list of supported attributes can be retrieved using
     * <code>getAttributeNames</code>.
     * <p/>
     * <p>The attribute is returned as a <code>java.lang.Object</code>
     * or some subclass.
     * Attribute names should follow the same convention as package
     * names. The Java Servlet API specification reserves names
     * matching <code>java.*</code>, <code>javax.*</code>,
     * and <code>sun.*</code>.
     *
     * @param name a <code>String</code> specifying the name
     *             of the attribute
     * @return an <code>Object</code> containing the value
     *         of the attribute, or <code>null</code>
     *         if no attribute exists matching the given
     *         name
     * @see ServletContext#getAttributeNames
     */
    Object getAttribute(String name);

    /**
     * Returns an <code>Enumeration</code> containing the
     * attribute names available
     * within this servlet context. Use the
     * {@link #getAttribute} method with an attribute name
     * to get the value of an attribute.
     *
     * @return an <code>Enumeration</code> of attribute
     *         names
     * @see #getAttribute
     */
    Enumeration<String> getAttributeNames();

    /**
     * Binds an object to a given attribute name in this servlet context. If
     * the name specified is already used for an attribute, this
     * method will replace the attribute with the new to the new attribute.
     * <p>If listeners are configured on the <code>ServletContext</code> the
     * container notifies them accordingly.
     * <p/>
     * If a null value is passed, the effect is the same as calling
     * <code>removeAttribute()</code>.
     * <p/>
     * <p>Attribute names should follow the same convention as package
     * names. The Java Servlet API specification reserves names
     * matching <code>java.*</code>, <code>javax.*</code>, and
     * <code>sun.*</code>.
     *
     * @param name   a <code>String</code> specifying the name
     *               of the attribute
     * @param object an <code>Object</code> representing the
     *               attribute to be bound
     */
    void setAttribute(String name, Object object);

    /**
     * Removes the attribute with the given name from
     * the servlet context. After removal, subsequent calls to
     * {@link #getAttribute} to retrieve the attribute's value
     * will return <code>null</code>.
     * <p/>
     * <p>If listeners are configured on the <code>ServletContext</code> the
     * container notifies them accordingly.
     *
     * @param name a <code>String</code> specifying the name
     *             of the attribute to be removed
     */
    void removeAttribute(String name);

    /**
     * Returns the name of this web application corresponding to this ServletContext as specified in the deployment
     * descriptor for this web application by the display-name element.
     *
     * @return The name of the web application or null if no name has been declared in the deployment descriptor.
     * @since Servlet 2.3
     */
    String getServletContextName();

    /**
     * Add the specified servlet to the context
     *
     * @param servletName servlet's name
     * @param className   class name of servlet
     * @return ServletRegistration.Dynamic allowing configuration of the servlet
     * @throws IllegalArgumentException duplicate servletName
     * @throws IllegalStateException    this method called after #initialize
     * @since 3.0
     */
    ServletRegistration.Dynamic addServlet(String servletName, String className) throws IllegalArgumentException, IllegalStateException;

    /**
     * Add the specified servlet to the context
     *
     * @param servletName servlet's name
     * @param servlet     servlet instance, perhaps created with createServlet
     * @return ServletRegistration.Dynamic allowing configuration of the servlet
     * @throws IllegalArgumentException duplicate servletName
     * @throws IllegalStateException    this method called after #initialize
     * @since 3.0
     */
    ServletRegistration.Dynamic addServlet(String servletName, Servlet servlet) throws IllegalArgumentException, IllegalStateException;

    /**
     * Add the specified servlet to the context
     *
     * @param servletName servlet's name
     * @param clazz       class of the servlet
     * @return ServletRegistration.Dynamic allowing configuration of the servlet
     * @throws IllegalArgumentException duplicate servletName
     * @throws IllegalStateException    this method called after #initialize
     * @since 3.0
     */
    ServletRegistration.Dynamic addServlet(String servletName, Class<? extends Servlet> clazz) throws IllegalArgumentException, IllegalStateException;

    /**
     * Create a servlet with dependencies injected.
     *
     * @param clazz servlet class
     * @return instantiated and injected servlet
     * @throws ServletException if something goes wrong
     * @since Servlet 3.0
     */
    <T extends Servlet> T createServlet(Class<T> clazz) throws ServletException;

    /**
     * Fish out the servlet registration for a named servlet
     *
     * @param servletName name of the servlet you want to configure
     * @return ServletRegistration for servlet you want
     * @since 3.0
     */
    ServletRegistration getServletRegistration(String servletName);

    /**
     * Fish out the servlet registration for a named servlet
     *
     * @return Map of name to ServletRegistration for all registered servlets
     * @since 3.0
     */
    Map<String, ? extends ServletRegistration> getServletRegistrations();

    /**
     * Add a filter to this context
     *
     * @param filterName name of filter
     * @param className  class name of filter
     * @return FilterRegistration.Dynamic allowing configuration of filter
     * @throws IllegalArgumentException duplicate filter name
     * @throws IllegalStateException    if called after #initialise
     * @since 3.0
     */
    FilterRegistration.Dynamic addFilter(String filterName, String className) throws IllegalArgumentException, IllegalStateException;

    /**
     * Add a filter to this context
     *
     * @param filterName name of filter
     * @param filter     filter instance perhaps from createFilter method
     * @return FilterRegistration.Dynamic allowing configuration of filter
     * @throws IllegalArgumentException duplicate filter name
     * @throws IllegalStateException    if called after #initialise
     * @since 3.0
     */
    FilterRegistration.Dynamic addFilter(String filterName, Filter filter) throws IllegalArgumentException, IllegalStateException;

    /**
     * Add a filter to this context
     *
     * @param filterName  name of filter
     * @param filterClass filter class
     * @return FilterRegistration.Dynamic allowing configuration of filter
     * @throws IllegalArgumentException duplicate filter name
     * @throws IllegalStateException    if called after #initialise
     * @since 3.0
     */
    FilterRegistration.Dynamic addFilter(String filterName, Class<? extends Filter> filterClass) throws IllegalArgumentException, IllegalStateException;

    /**
     * Create a filter with dependencies injected.
     *
     * @param clazz filter class
     * @return instantiated and injected filter
     * @throws ServletException if something goes wrong
     * @since Servlet 3.0
     */
    <T extends Filter> T createFilter(Class<T> clazz) throws ServletException;

    /**
     * Return the FilterRegistration corresponding to the named filter
     * @param filterName Name of filter you want to configure
     * @return FilterRegistration allowing configuration of filter
     * @since 3.0
     */
    FilterRegistration getFilterRegistration(String filterName);

    /**
     * Return a possibly empty immutable map of registrations for all filters.
     * @return FilterRegistration allowing configuration of filter
     * @since 3.0
     */
    Map<String, ? extends FilterRegistration> getFilterRegistrations();

    /**
     * Add a listener created from the specified class
     * @param listenerClass class of listener to add
     * @since Servlet 3.0
     */
    void addListener(Class<? extends EventListener> listenerClass);

    /**
     * Add a listener created from the specified class name
     * @param className name of class of listener to add
     * @since Servlet 3.0
     */
    void addListener(String className);

    /**
     * add the listener instance
     * @param t listener instance
     * @param <T> type of listener
     * @since Servlet 3.0
     */
    <T extends EventListener> void addListener(T t);

    /**
     * create a fully initialized listener
     * @param clazz listener class
     * @param <T> type of listener class
     * @return fully initialized listener object
     * @since Servlet 3.0
     */
    <T extends EventListener> T createListener(Class<T> clazz) throws ServletException;

    /**
     * declare the roles used in the web app as argumnents to isUserInRole
     * @param roleNames the role names used in the web app
     * @since Servlet 3.0
     */
    void declareRoles(String... roleNames);

    /**
     * @return configuration of session cookie
     * @since 3.0
     */
    SessionCookieConfig getSessionCookieConfig();

    /**
     * @param sessionTrackingModes enumset of SessionTrackingModes for this web app
     * @since 3.0
     */
    void setSessionTrackingModes(Set<SessionTrackingMode> sessionTrackingModes);

    /**
     * @return the default session tracking modes
     * @since 3.0
     */
    Set<SessionTrackingMode> getDefaultSessionTrackingModes();

    /**
     *
     * @return the major version of the servlet spec the web app thinks it is using.
     * @throws UnsupportedOperationException if this ServletContext was constructed in a way where the spec version cannot be determined.
     * @since 3.0
     */
    int getEffectiveMajorVersion() throws UnsupportedOperationException;

    /**
     *
     * @return the minor version of the servlet spec the web app thinks it is using.
     * @throws UnsupportedOperationException if this ServletContext was constructed in a way where the spec version cannot be determined.
     * @since 3.0
     */
    int getEffectiveMinorVersion() throws UnsupportedOperationException;

    /**
     * @return the actual session tracking modes.  These will be the default ones unless they've been explicitly set.
     * @since 3.0
     */
    Set<SessionTrackingMode> getEffectiveSessionTrackingModes();

    /**
     *
     * @return the classloader for this web app
     * @since 3.0
     */
    ClassLoader getClassLoader();

    /**
     * 
     * @return jsp config aggregated from xml bits or null
     * @since 3.0
     */
    JspConfigDescriptor getJspConfigDescriptor ();

    /**
     * Returns the configuration name of the logical host on which the ServletContext is deployed. 
     * Servlet containers may support multiple logical hosts. This method must return the same name
     * for all the servlet contexts deployed on a logical host, and the name returned by this method 
     * must be distinct, stable per logical host, and suitable for use in associating server 
     * configuration information with the logical host. The returned value is NOT expected or 
     * required to be equivalent to a network address or hostname of the logical host.
     * 
     * @return a String containing the configuration name of the logical host on which the servlet
     *         context is deployed.
     * @throws UnsupportedOperationException - if this ServletContext was passed to the 
     *         ServletContextListener.contextInitialized(javax.servlet.ServletContextEvent)  method
     *         of a ServletContextListener that was neither declared in web.xml or web-fragment.xml, 
     *         nor annotated with WebListener
     * @since Servlet 3.1
     */
    String getVirtualServerName();
}
