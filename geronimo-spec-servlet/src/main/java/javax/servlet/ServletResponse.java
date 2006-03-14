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
package javax.servlet;

import java.io.IOException;
import java.io.Writer;
import java.util.Locale;

/**
 * @version $Revision$ $Date$
 */
public interface ServletResponse {
    public void flushBuffer() throws IOException;

    public int getBufferSize();

    public String getCharacterEncoding();

    public String getContentType();

    public Locale getLocale();

    public ServletOutputStream getOutputStream() throws IOException;

    public Writer getWriter() throws IOException;

    public boolean isCommitted();

    public void reset();

    public void resetBuffer();

    public void setBufferSize(int size);

    public void setCharacterEncoding(String charset);

    public void setContentLength(int len);

    public void setContentType(String type);

    public void setLocale(Locale locale);
}
