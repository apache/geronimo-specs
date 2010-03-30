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

import java.util.Locale;

import javax.ws.rs.ext.RuntimeDelegate;

public class Variant {
    public abstract static class VariantListBuilder {
        protected VariantListBuilder() {
            /* do nothing */
        }

        public abstract Variant.VariantListBuilder add();

        public abstract java.util.List<Variant> build();

        public abstract Variant.VariantListBuilder encodings(String... values);

        public abstract Variant.VariantListBuilder languages(Locale... values);

        public abstract Variant.VariantListBuilder mediaTypes(MediaType... values);

        private final static RuntimeDelegate delegate = RuntimeDelegate.getInstance();

        public static Variant.VariantListBuilder newInstance() {
            return delegate.createVariantListBuilder();
        }
    }

    public static Variant.VariantListBuilder encodings(String... values) {
        return VariantListBuilder.newInstance().encodings(values);
    }

    public static Variant.VariantListBuilder languages(java.util.Locale... values) {
        return VariantListBuilder.newInstance().languages(values);
    }

    public static Variant.VariantListBuilder mediaTypes(MediaType... values) {
        return VariantListBuilder.newInstance().mediaTypes(values);
    }

    private final MediaType mediaType;
    private final Locale    language;
    private final String    encoding;

    public Variant(MediaType mediaType, Locale language, String encoding) {
        if (mediaType == null && language == null && encoding == null) {
            throw new IllegalArgumentException();
        }

        this.mediaType = mediaType;
        this.language = language;
        this.encoding = encoding;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Variant)) {
            return false;
        }

        Variant other = (Variant)obj;

        String encoding = getEncoding();
        if (encoding == null) {
            if (other.getEncoding() != null) {
                return false;
            }
        } else {
            if (!encoding.equals(other.getEncoding())) {
                return false;
            }
        }

        Locale locale = getLanguage();
        if (locale == null) {
            if (other.getLanguage() != null) {
                return false;
            }
        } else {
            if (!locale.equals(other.getLanguage())) {
                return false;
            }
        }

        MediaType mt = getMediaType();
        if (mt == null) {
            if (other.getMediaType() != null) {
                return false;
            }
        } else {
            return mt.equals(other.getMediaType());
        }

        return true;
    }

    public String getEncoding() {
        return encoding;
    }

    public java.util.Locale getLanguage() {
        return language;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + ((language == null) ? 0 : language.hashCode());
        result = 31 * result + ((encoding == null) ? 0 : encoding.hashCode());
        result = 31 * result + ((mediaType == null) ? 0 : mediaType.hashCode());
        return result;
    }

}
