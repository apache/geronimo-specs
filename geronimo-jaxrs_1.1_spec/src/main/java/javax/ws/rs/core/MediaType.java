/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.    
 */

package javax.ws.rs.core;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import javax.ws.rs.ext.RuntimeDelegate;
import javax.ws.rs.ext.RuntimeDelegate.HeaderDelegate;

public class MediaType {
    public static final String                     APPLICATION_ATOM_XML             =
                                                                                        "application/atom+xml";

    public static final MediaType                  APPLICATION_ATOM_XML_TYPE        =
                                                                                        new MediaType(
                                                                                                      "application",
                                                                                                      "atom+xml");
    public static final String                     APPLICATION_FORM_URLENCODED      =
                                                                                        "application/x-www-form-urlencoded";
    public static final MediaType                  APPLICATION_FORM_URLENCODED_TYPE =
                                                                                        new MediaType(
                                                                                                      "application",
                                                                                                      "x-www-form-urlencoded");
    public static final String                     APPLICATION_JSON                 =
                                                                                        "application/json";
    public static final MediaType                  APPLICATION_JSON_TYPE            =
                                                                                        new MediaType(
                                                                                                      "application",
                                                                                                      "json");
    public static final String                     APPLICATION_OCTET_STREAM         =
                                                                                        "application/octet-stream";
    public static final MediaType                  APPLICATION_OCTET_STREAM_TYPE    =
                                                                                        new MediaType(
                                                                                                      "application",
                                                                                                      "octet-stream");
    public static final String                     APPLICATION_SVG_XML              =
                                                                                        "application/svg+xml";
    public static final MediaType                  APPLICATION_SVG_XML_TYPE         =
                                                                                        new MediaType(
                                                                                                      "application",
                                                                                                      "svg+xml");
    public static final String                     APPLICATION_XHTML_XML            =
                                                                                        "application/xhtml+xml";
    public static final MediaType                  APPLICATION_XHTML_XML_TYPE       =
                                                                                        new MediaType(
                                                                                                      "application",
                                                                                                      "xhtml+xml");
    public static final String                     APPLICATION_XML                  =
                                                                                        "application/xml";
    public static final MediaType                  APPLICATION_XML_TYPE             =
                                                                                        new MediaType(
                                                                                                      "application",
                                                                                                      "xml");
    public static final String                     MEDIA_TYPE_WILDCARD              = "*";
    public static final String                     MULTIPART_FORM_DATA              =
                                                                                        "multipart/form-data";
    public static final MediaType                  MULTIPART_FORM_DATA_TYPE         =
                                                                                        new MediaType(
                                                                                                      "multipart",
                                                                                                      "form-data");
    public static final String                     TEXT_HTML                        = "text/html";
    public static final MediaType                  TEXT_HTML_TYPE                   =
                                                                                        new MediaType(
                                                                                                      "text",
                                                                                                      "html");
    public static final String                     TEXT_PLAIN                       = "text/plain";
    public static final MediaType                  TEXT_PLAIN_TYPE                  =
                                                                                        new MediaType(
                                                                                                      "text",
                                                                                                      "plain");
    public static final String                     TEXT_XML                         = "text/xml";
    public static final MediaType                  TEXT_XML_TYPE                    =
                                                                                        new MediaType(
                                                                                                      "text",
                                                                                                      "xml");
    public static final String                     WILDCARD                         = "*/*";
    public static final MediaType                  WILDCARD_TYPE                    =
                                                                                        new MediaType(
                                                                                                      "*",
                                                                                                      "*");

    private final String                           type;
    private final String                           subtype;
    private final Map<String, String>              params;

    private static final HeaderDelegate<MediaType> delegate                         =
                                                                                        RuntimeDelegate
                                                                                            .getInstance()
                                                                                            .createHeaderDelegate(MediaType.class);

    public MediaType(String type, String subtype, Map<String, String> parameters) {
        if (type == null) {
            this.type = MEDIA_TYPE_WILDCARD;
        } else {
            this.type = type;
        }

        if (subtype == null) {
            this.subtype = MEDIA_TYPE_WILDCARD;
        } else {
            this.subtype = subtype;
        }

        if (parameters == null) {
            this.params = Collections.emptyMap();
        } else {
            // need to use a temporary map here since for some reason the ordering is important.
            Map<String, String> temp = new TreeMap<String, String>(new Comparator<String>() {
                public int compare(String o1, String o2) {
                    return o1.compareToIgnoreCase(o2);
                }
            });
            // need to put in as all lower case keys for comparisons, hashcode, and output
            for (String key : parameters.keySet()) {
                temp.put(key.toLowerCase(), parameters.get(key));
            }
            this.params = Collections.unmodifiableMap(temp);
        }
    }

    public MediaType(String type, String subtype) {
        this(type, subtype, null);
    }

    public MediaType() {
        this(MEDIA_TYPE_WILDCARD, MEDIA_TYPE_WILDCARD);
    }

    public static MediaType valueOf(String type) throws java.lang.IllegalArgumentException {
        return delegate.fromString(type);
    }

    public String getType() {
        return type;
    }

    public boolean isWildcardType() {
        return MEDIA_TYPE_WILDCARD.equals(getType());
    }

    public String getSubtype() {
        return subtype;
    }

    public boolean isWildcardSubtype() {
        return MEDIA_TYPE_WILDCARD.equals(getSubtype());
    }

    public Map<String, String> getParameters() {
        return params;
    }

    public boolean isCompatible(MediaType other) {
        if (other == null) {
            return false;
        }
        if (isWildcardType() || other.isWildcardType()) {
            return true;
        }
        if (isWildcardSubtype() || other.isWildcardSubtype()) {
            return getType().equalsIgnoreCase(other.getType());
        }
        return getType().equalsIgnoreCase(other.getType()) && getSubtype().equalsIgnoreCase(other
            .getSubtype());
    }

    @Override
    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof MediaType)) {
            return false;
        }

        MediaType other = (MediaType)obj;

        return getType().equalsIgnoreCase(other.getType()) && getSubtype().equalsIgnoreCase(other
            .getSubtype())
            && getParameters().equals(other.getParameters());
    }

    @Override
    public int hashCode() {
        int result = 17;
        // be sure to lowercase for comparisions

        // be careful about hash code. since not guaranteed that this is a final
        // class, need to use methods. methods do not guarantee lowercase
        result = 31 * result + getType().toLowerCase().hashCode();
        result = 31 * result + getSubtype().toLowerCase().hashCode();
        result = 31 * result + getParameters().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return delegate.toString(this);
    }
}
