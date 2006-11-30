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

//
// This source code implements specifications defined by the Java
// Community Process. In order to remain compliant with the specification
// DO NOT add / change / or delete method signatures!
//

package javax.security.jacc;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.regex.Pattern;


/**
 * @version $Rev$ $Date$
 */
final class HTTPMethodSpec {

    private final static String[] HTTP_METHODS = {"GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS", "TRACE"};
    private final static int[] HTTP_MASKS = {0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40};

    final static int NA = 0x00;
    final static int INTEGRAL = 0x01;
    final static int CONFIDENTIAL = 0x02;
    final static int NONE = INTEGRAL | CONFIDENTIAL;

    private final int mask;
    private final String[] extensionMethods;
    private final boolean isExcluded;
    private final int transport;
    private String actions;
    private static final String[] NO_METHODS = new String[0];
    private static final Pattern TOKEN_PATTERN = Pattern.compile("[!-~&&[^\\(\\)\\<\\>@,;:\\\\\"/\\[\\]\\?=\\{\\}]]*");

    public HTTPMethodSpec(String[] HTTPMethods) {
        this(HTTPMethods, null);
    }

    public HTTPMethodSpec(String name, boolean parseTransportType) {
        if (parseTransportType) {
            if (name == null || name.length() == 0) {
                this.transport = NONE;
            } else {
                String[] tokens = name.split(":", 2);

                if (tokens.length == 2) {
                    if (tokens[1].equals("NONE")) {
                        this.transport = NONE;
                    } else if (tokens[1].equals("INTEGRAL")) {
                        this.transport = INTEGRAL;
                    } else if (tokens[1].equals("CONFIDENTIAL")) {
                        this.transport = CONFIDENTIAL;
                    } else {
                        throw new IllegalArgumentException("Invalid transportType: " + tokens[1]);
                    }
                } else {
                    this.transport = NONE;
                }
                name = tokens[0];
            }
        } else {
            this.transport = NA;
        }

        if (name == null || name.length() == 0) {
            this.mask = 0x00;
            this.extensionMethods = NO_METHODS;
            this.isExcluded = true;
        } else {
            ArrayList<String> extensions = null;
            if (isExcluded = name.charAt(0) == '!') {
                name = name.substring(1);
            }
            String[] methods = name.split(",", -1);
            int tmpMask = 0;

            for (int i = 0; i < methods.length; i++) {
                boolean found = false;

                for (int j = 0; j < HTTP_METHODS.length; j++) {
                    if (methods[i].equals(HTTP_METHODS[j])) {
                        tmpMask |= HTTP_MASKS[j];
                        found = true;

                        break;
                    }
                }
                if (!found) {
                    checkToken(methods[i]);
                    if (extensions == null) {
                        extensions = new ArrayList<String>(methods.length);
                    }
                    add(extensions, methods[i]);
                }
            }
            this.mask = tmpMask;
            if (extensions == null) {
                extensionMethods = NO_METHODS;
            } else {
                extensionMethods = extensions.toArray(new String[extensions.size()]);
            }
        }
    }

    public HTTPMethodSpec(String[] HTTPMethods, String transport) {
        boolean parseTransportType = transport != null;

        if (HTTPMethods == null || HTTPMethods.length == 0) {
            this.mask = 0x00;
            this.extensionMethods = NO_METHODS;
            this.isExcluded = true;
        } else {
            int tmpMask = 0;
            this.isExcluded = false;
            ArrayList<String> extensions = null;

            for (int i = 0; i < HTTPMethods.length; i++) {
                boolean found = false;

                for (int j = 0; j < HTTP_METHODS.length; j++) {
                    if (HTTPMethods[i].equals(HTTP_METHODS[j])) {
                        tmpMask |= HTTP_MASKS[j];
                        found = true;

                        break;
                    }
                }
                if (!found) {
                    checkToken(HTTPMethods[i]);
                    if (extensions == null) {
                        extensions = new ArrayList<String>(HTTPMethods.length);
                    }
                    add(extensions, HTTPMethods[i]);
                }
            }
            this.mask = tmpMask;
            if (extensions == null) {
                extensionMethods = NO_METHODS;
            } else {
                extensionMethods = extensions.toArray(new String[extensions.size()]);
            }
        }

        if (parseTransportType) {
            if (transport.length() == 0 || transport.equals("NONE")) {
                this.transport = NONE;
            } else if (transport.equals("INTEGRAL")) {
                this.transport = INTEGRAL;
            } else if (transport.equals("CONFIDENTIAL")) {
                this.transport = CONFIDENTIAL;
            } else {
                throw new IllegalArgumentException("Invalid transport");
            }
        } else {
            this.transport = NA;
        }
    }

    public HTTPMethodSpec(String singleMethod, int transport) {
        int tmpMask = 0;

        for (int j = 0; j < HTTP_METHODS.length; j++) {
            if (HTTP_METHODS[j].equals(singleMethod)) {
                tmpMask = HTTP_MASKS[j];

                break;
            }
        }
        if (tmpMask == 0) {
            checkToken(singleMethod);
            this.extensionMethods = new String[]{singleMethod};
        } else {
            this.extensionMethods = NO_METHODS;
        }

        this.mask = tmpMask;
        this.isExcluded = false;
        this.transport = transport;
    }


    private void checkToken(String method) {
        if (!TOKEN_PATTERN.matcher(method).matches()) {
            throw new IllegalArgumentException("Invalid HTTPMethodSpec");
        }
    }

    private void add(ArrayList<String> extensions, String httpMethod) {
        for (int i = 0; i < extensions.size(); i++) {
            String existingMethod = extensions.get(i);
            int compare = existingMethod.compareTo(httpMethod);
            if (compare == 0) {
                return;
            }
            if (compare > 0) {
                extensions.add(i, httpMethod);
                return;
            }
        }
        extensions.add(httpMethod);
    }


    public boolean equals(HTTPMethodSpec o) {
        return mask == o.mask && transport == o.transport;
    }

    public String getActions() {
        if (actions == null && !isAll()) {
            boolean first = true;
            StringBuffer buffer = new StringBuffer();
            if (isExcluded) {
                buffer.append("!");
            }

            for (int i = 0; i < HTTP_MASKS.length; i++) {
                if ((mask & HTTP_MASKS[i]) > 0) {
                    if (first) {
                        first = false;
                    } else {
                        buffer.append(",");
                    }
                    buffer.append(HTTP_METHODS[i]);
                }
            }
            for (int i = 0; i < extensionMethods.length; i++) {
                String method = extensionMethods[i];
                if (first) {
                    first = false;
                } else {
                    buffer.append(",");
                }
                buffer.append(method);
            }

            if (transport == INTEGRAL) {
                buffer.append(":INTEGRAL");
            } else if (transport == CONFIDENTIAL) {
                buffer.append(":CONFIDENTIAL");
            }

            actions = buffer.toString();
        }
        return actions;
    }

    private boolean isAll() {
        return isExcluded && mask == 0x00;
    }

    public int hashCode() {
        return mask ^ transport;
    }

    public boolean implies(HTTPMethodSpec p) {
        if ((transport & p.transport) != p.transport) {
            return false;
        }
        if (isExcluded) {
            if (p.isExcluded) {
                return ((mask & p.mask) == mask) && contains(p.extensionMethods, extensionMethods);
            } else {
                return ((mask & p.mask) == 0x00) && isDisjoint(extensionMethods, p.extensionMethods);
            }
        } else {
            if (p.isExcluded) {
                return false;
            } else {
                return ((mask & p.mask) == p.mask) && contains(extensionMethods, p.extensionMethods);
            }
        }
    }

    private boolean isDisjoint(String[] a, String[] b) {
        int start = 0;
        for (int i = 0; i < a.length; i++) {
            String s = a[i];
            for (int j = start; j < b.length; j++) {
                String s1 = b[j];
                int compare = s.compareTo(s1);
                if (compare == 0) {
                    return false;
                }
                if (compare < 0) {
                    start = j;
                    break;
                }
            }
        }
        return true;
    }

    private boolean contains(String[] set, String[] subset) {
        int start = 0;
        for (int i = 0; i < subset.length; i++) {
            boolean found = false;
            String s = subset[i];
            for (int j = start; j < set.length; j++) {
                String s1 = set[j];
                int compare = s.compareTo(s1);
                if (compare == 0) {
                    found = true;
                    start = j + 1;
                    break;
                }
                if (compare < 0) {
                    return false;
                }
            }
            if (!found) {
                return false;
            }
        }
        return true;
    }
}
