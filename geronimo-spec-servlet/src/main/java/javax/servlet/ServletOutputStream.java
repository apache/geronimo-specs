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
//
package javax.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.CharBuffer;

/**
 * @version $Revision$ $Date$
 */
public abstract class ServletOutputStream extends OutputStream {
    protected ServletOutputStream() {
    }

    public void print(boolean b) throws IOException {
        if (b) {
            print("true");
        } else {
            print("false");
        }
    }

    public void print(char c) throws IOException {
        print(String.valueOf(c));
    }

    public void print(double d) throws IOException {
        print(String.valueOf(d));
    }

    public void print(float f) throws IOException {
        print(String.valueOf(f));
    }

    public void print(int i) throws IOException {
        print(String.valueOf(i));
    }

    public void print(long l) throws IOException {
        print(String.valueOf(l));
    }

    public void print(String s) throws IOException {
        try {
            if (null == s)
                s = "null";
            CharBuffer buffer = CharBuffer.allocate(s.length());
            buffer.put(s);
            for (int i = 0; i < buffer.length(); i++) {
                write(buffer.get(i));
            }
        } catch (IndexOutOfBoundsException e) {
            IOException ioe = new IOException(
                    "buffer.get passed an out of bounds index");
            ioe.initCause(e);
            throw ioe;
        }
    }

    public void println() throws IOException {
        print("\r\n");
    }

    public void println(boolean b) throws IOException {
        print(b);
        println();
    }

    public void println(char c) throws IOException {
        print(c);
        println();
    }

    public void println(double d) throws IOException {
        print(d);
        println();
    }

    public void println(float f) throws IOException {
        print(f);
        println();
    }

    public void println(int i) throws IOException {
        print(i);
        println();
    }

    public void println(long l) throws IOException {
        print(l);
        println();
    }

    public void println(String s) throws IOException {
        print(s);
        println();
    }
}
