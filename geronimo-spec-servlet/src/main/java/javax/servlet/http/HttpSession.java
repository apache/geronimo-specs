/*
 * Copyright 2006 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * 
 */

//
// This source code implements specifications defined by the Java
// Community Process. In order to remain compliant with the specification
// DO NOT add / change / or delete method signatures!
//
package javax.servlet.http;

import java.util.Enumeration;
import javax.servlet.ServletContext;

/**
 * @version $Revision$ $Date$
 */
public interface HttpSession {
    public Object getAttribute(String name);

    public Enumeration getAttributeNames();

    public long getCreationTime();

    public String getId();

    public long getLastAccessedTime();

    public int getMaxInactiveInterval();

    public ServletContext getServletContext();

    @Deprecated
    public HttpSessionContext getSessionContext();

    @Deprecated
    public Object getValue(String name);

    @Deprecated
    public String[] getValueNames();

    public void invalidate();

    public boolean isNew();

    @Deprecated
    public void putValue(String name, Object value);

    public void removeAttribute(String name);

    @Deprecated
    public void removeValue(String name);

    public void setAttribute(String name, Object value);

    public void setMaxInactiveInterval(int interval);
}
