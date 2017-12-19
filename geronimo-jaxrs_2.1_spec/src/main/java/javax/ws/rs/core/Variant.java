/*
 * #%L
 * Apache Geronimo JAX-RS Spec 2.0
 * %%
 * Copyright (C) 2003 - 2014 The Apache Software Foundation
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

package javax.ws.rs.core;

import java.io.StringWriter;
import java.util.List;
import java.util.Locale;

import javax.ws.rs.ext.RuntimeDelegate;

public class Variant {

    private Locale language;

    private MediaType mediaType;

    private String encoding;

    public Variant(MediaType mediaType, String language, String encoding) {
        if (mediaType == null && language == null && encoding == null) {
            throw new IllegalArgumentException("mediaType, language, encoding all null");
        }
        this.encoding = encoding;
        this.language = (language == null) ? null : new Locale(language);
        this.mediaType = mediaType;
    }

    public Variant(MediaType mediaType, String language, String country, String encoding) {
        if (mediaType == null && language == null && encoding == null) {
            throw new IllegalArgumentException("mediaType, language, encoding all null");
        }
        this.encoding = encoding;
        this.language = (language == null) ? null : new Locale(language, country);
        this.mediaType = mediaType;
    }

    public Variant(MediaType mediaType, String language, String country, String languageVariant, String encoding) {
        if (mediaType == null && language == null && encoding == null) {
            throw new IllegalArgumentException("mediaType, language, encoding all null");
        }
        this.encoding = encoding;
        this.language = (language == null) ? null : new Locale(language, country, languageVariant);
        this.mediaType = mediaType;
    }

    public Variant(MediaType mediaType, Locale language, String encoding) {
        if (mediaType == null && language == null && encoding == null) {
            throw new IllegalArgumentException("mediaType, language, encoding all null");
        }
        this.encoding = encoding;
        this.language = language;
        this.mediaType = mediaType;
    }

    public Locale getLanguage() {
        return language;
    }

    public String getLanguageString() {
        return (language == null) ? null : language.toString();
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public String getEncoding() {
        return encoding;
    }

    public static VariantListBuilder mediaTypes(MediaType... mediaTypes) {
        VariantListBuilder b = VariantListBuilder.newInstance();
        b.mediaTypes(mediaTypes);
        return b;
    }

    public static VariantListBuilder languages(Locale... languages) {
        VariantListBuilder b = VariantListBuilder.newInstance();
        b.languages(languages);
        return b;
    }

    public static VariantListBuilder encodings(String... encodings) {
        VariantListBuilder b = VariantListBuilder.newInstance();
        b.encodings(encodings);
        return b;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.language != null ? this.language.hashCode() : 0);
        hash = 29 * hash + (this.mediaType != null ? this.mediaType.hashCode() : 0);
        hash = 29 * hash + (this.encoding != null ? this.encoding.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Variant other = (Variant) obj;
        if (this.language != other.language && (this.language == null || !this.language.equals(other.language))) {
            return false;
        }
        if (this.mediaType != other.mediaType && (this.mediaType == null || !this.mediaType.equals(other.mediaType))) {
            return false;
        }
        // noinspection StringEquality
        return this.encoding == other.encoding || (this.encoding != null && this.encoding.equals(other.encoding));
    }

    @Override
    public String toString() {
        StringWriter w = new StringWriter();
        w.append("Variant[mediaType=");
        w.append(mediaType == null ? "null" : mediaType.toString());
        w.append(", language=");
        w.append(language == null ? "null" : language.toString());
        w.append(", encoding=");
        w.append(encoding == null ? "null" : encoding);
        w.append("]");
        return w.toString();
    }

    public static abstract class VariantListBuilder {

        protected VariantListBuilder() {
        }

        public static VariantListBuilder newInstance() {
            return RuntimeDelegate.getInstance().createVariantListBuilder();
        }

        public abstract List<Variant> build();

        public abstract VariantListBuilder add();

        public abstract VariantListBuilder languages(Locale... languages);

        public abstract VariantListBuilder encodings(String... encodings);

        public abstract VariantListBuilder mediaTypes(MediaType... mediaTypes);
    }
}
