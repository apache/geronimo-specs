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

public class CollapsedStringAdapter extends XmlAdapter<String, String> {

    public String marshal(String v) {
        return v;
    }

    public String unmarshal(String v) {
        if(v == null) {
            return null;
        }
        int len = v.length();
        int s;
        for (s = 0; s < len && !isWhiteSpace(v.charAt(s)); s++);
        if (s == len) {
            return v;
        }
        StringBuffer result = new StringBuffer(len);
        if (s != 0) {
            for(int i = 0; i < s; i++) {
                result.append(v.charAt(i));
            }
            result.append(' ');
        }
        boolean inStripMode = true;
        for (int i = s + 1; i < len; i++) {
            char ch = v.charAt(i);
            boolean b = isWhiteSpace(ch);
            if (inStripMode && b) {
                continue;
            }
            inStripMode = b;
            result.append(inStripMode ? ' ' : ch);
        }
        len = result.length();
        if (len > 0 && result.charAt(len - 1) == ' ') {
            result.setLength(len - 1);
        }
        return result.toString();
    }

    protected static boolean isWhiteSpace(char ch) {
        if (ch > ' ') {
            return false;
        } else {
            return ch == '\t' || ch == '\n' || ch == '\r' || ch == ' ';
        }
    }

}
