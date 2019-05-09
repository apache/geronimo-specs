/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package javax.xml.bind.annotation.adapters;

public final class NormalizedStringAdapter extends XmlAdapter<String, String> {

    public String marshal(String v) {
        return v;
    }

    public String unmarshal(String v) {
        if (v == null) {
            return null;
        }
        int i;
        for (i = v.length() - 1; i >= 0 && !isWhiteSpaceExceptSpace(v.charAt(i)); i--);
        if (i < 0) {
            return v;
        }
        char buf[] = v.toCharArray();
        buf[i--] = ' ';
        for(; i >= 0; i--) {
            if(isWhiteSpaceExceptSpace(buf[i])) {
                buf[i] = ' ';
            }
        }
        return new String(buf);
    }

    protected static boolean isWhiteSpaceExceptSpace(char ch) {
        if (ch >= ' ') {
            return false;
        } else {
            return ch == '\t' || ch == '\n' || ch == '\r';
        }
    }

}
