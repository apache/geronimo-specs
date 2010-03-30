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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.ext.RuntimeDelegate;
import javax.ws.rs.ext.RuntimeDelegate.HeaderDelegate;

public class CacheControl {

    private int                 maxAge          = -1;
    private int                 sMaxAge         = -1;
    private boolean             isPrivate       = false;
    private boolean             noCache         = false;
    private boolean             noStore         = false;
    private boolean             noTransform     = true;
    private boolean             mustRevalidate  = false;
    private boolean             proxyRevalidate = false;
    private Map<String, String> cacheExtensions = null;
    private List<String>        noCacheFields   = null;
    private List<String>        privateFields   = null;

    public CacheControl() {
        /* do nothing */
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        /*
         * TODO: should the check be for instanceof or for getClass()? this
         * class is not final so checking instanceof for now.
         */
        if (!(obj instanceof CacheControl)) {
            return false;
        }

        CacheControl other = (CacheControl)obj;

        if (isPrivate != other.isPrivate()) {
            return false;
        }

        if (noCache != other.isNoCache()) {
            return false;
        }

        if (noStore != other.isNoStore()) {
            return false;
        }

        if (noTransform != other.isNoTransform()) {
            return false;
        }

        if (mustRevalidate != other.isMustRevalidate()) {
            return false;
        }

        if (proxyRevalidate != other.isProxyRevalidate()) {
            return false;
        }

        if (maxAge != other.getMaxAge()) {
            return false;
        }

        if (sMaxAge != other.getSMaxAge()) {
            return false;
        }

        if (!getCacheExtension().equals(other.getCacheExtension())) {
            return false;
        }

        if (!getPrivateFields().equals(other.getPrivateFields())) {
            return false;
        }

        if (!getNoCacheFields().equals(other.getNoCacheFields())) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + maxAge;
        result = 31 * result + sMaxAge;
        result = 31 * result + ((isPrivate) ? 1 : 0);
        result = 31 * result + ((noCache) ? 1 : 0);
        result = 31 * result + ((noStore) ? 1 : 0);
        result = 31 * result + ((noTransform) ? 1 : 0);
        result = 31 * result + ((mustRevalidate) ? 1 : 0);
        result = 31 * result + ((proxyRevalidate) ? 1 : 0);
        result = 31 * result + getCacheExtension().hashCode();
        result = 31 * result + getNoCacheFields().hashCode();
        result = 31 * result + getPrivateFields().hashCode();
        return result;
    }

    public Map<String, String> getCacheExtension() {
        if (cacheExtensions == null) {
            cacheExtensions = new HashMap<String, String>();
        }
        return cacheExtensions;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public List<String> getNoCacheFields() {
        if (noCacheFields == null) {
            noCacheFields = new ArrayList<String>();
        }
        return noCacheFields;
    }

    public List<String> getPrivateFields() {
        if (privateFields == null) {
            privateFields = new ArrayList<String>();
        }
        return privateFields;
    }

    public int getSMaxAge() {
        return sMaxAge;
    }

    public boolean isMustRevalidate() {
        return mustRevalidate;
    }

    public boolean isNoCache() {
        return noCache;
    }

    public boolean isNoStore() {
        return noStore;
    }

    public boolean isNoTransform() {
        return noTransform;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public boolean isProxyRevalidate() {
        return proxyRevalidate;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public void setMustRevalidate(boolean mustRevalidate) {
        this.mustRevalidate = mustRevalidate;
    }

    public void setNoCache(boolean noCache) {
        this.noCache = noCache;
    }

    public void setNoStore(boolean noStore) {
        this.noStore = noStore;
    }

    public void setNoTransform(boolean noTransform) {
        this.noTransform = noTransform;
    }

    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public void setProxyRevalidate(boolean proxyRevalidate) {
        this.proxyRevalidate = proxyRevalidate;
    }

    public void setSMaxAge(int sMaxAge) {
        this.sMaxAge = sMaxAge;
    }

    private final static HeaderDelegate<CacheControl> headerDelegate =
                                                                         RuntimeDelegate
                                                                             .getInstance()
                                                                             .createHeaderDelegate(CacheControl.class);

    @Override
    public String toString() {
        return headerDelegate.toString(this);
    }

    public static CacheControl valueOf(String value) {
        return headerDelegate.fromString(value);
    }
}
