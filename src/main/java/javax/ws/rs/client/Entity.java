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

package javax.ws.rs.client;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Locale;

import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Variant;

public final class Entity<T> {

    private static final Annotation[] EMPTY_ANNOTATIONS = new Annotation[0];

    private final T entity;

    private final Variant variant;

    private final Annotation[] annotations;

    public static <T> Entity<T> entity(final T entity, final MediaType mediaType) {
        return new Entity<T>(entity, mediaType);
    }

    public static <T> Entity<T> entity(final T entity, final MediaType mediaType, Annotation[] annotations) {
        return new Entity<T>(entity, mediaType, annotations);
    }

    public static <T> Entity<T> entity(final T entity, final String mediaType) {
        return new Entity<T>(entity, MediaType.valueOf(mediaType));
    }

    public static <T> Entity<T> entity(final T entity, final Variant variant) {
        return new Entity<T>(entity, variant);
    }

    public static <T> Entity<T> entity(final T entity, final Variant variant, Annotation[] annotations) {
        return new Entity<T>(entity, variant, annotations);
    }

    public static <T> Entity<T> text(final T entity) {
        return new Entity<T>(entity, MediaType.TEXT_PLAIN_TYPE);
    }

    public static <T> Entity<T> xml(final T entity) {
        return new Entity<T>(entity, MediaType.APPLICATION_XML_TYPE);
    }

    public static <T> Entity<T> json(final T entity) {
        return new Entity<T>(entity, MediaType.APPLICATION_JSON_TYPE);
    }

    public static <T> Entity<T> html(final T entity) {
        return new Entity<T>(entity, MediaType.TEXT_HTML_TYPE);
    }

    public static <T> Entity<T> xhtml(final T entity) {
        return new Entity<T>(entity, MediaType.APPLICATION_XHTML_XML_TYPE);
    }

    public static Entity<Form> form(final Form form) {
        return new Entity<Form>(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE);
    }

    public static Entity<Form> form(final MultivaluedMap<String, String> formData) {
        return new Entity<Form>(new Form(formData), MediaType.APPLICATION_FORM_URLENCODED_TYPE);
    }

    private Entity(final T entity, final MediaType mediaType) {
        this(entity, new Variant(mediaType, (Locale) null, null), null);
    }

    private Entity(final T entity, final Variant variant) {
        this(entity, variant, null);
    }

    private Entity(final T entity, final MediaType mediaType, Annotation[] annotations) {
        this(entity, new Variant(mediaType, (Locale) null, null), annotations);
    }

    private Entity(final T entity, final Variant variant, Annotation[] annotations) {
        this.entity = entity;
        this.variant = variant;

        this.annotations = (annotations == null) ? EMPTY_ANNOTATIONS : annotations;
    }

    public Variant getVariant() {
        return variant;
    }

    public MediaType getMediaType() {
        return variant.getMediaType();
    }

    public String getEncoding() {
        return variant.getEncoding();
    }

    public Locale getLanguage() {
        return variant.getLanguage();
    }

    public T getEntity() {
        return entity;
    }

    public Annotation[] getAnnotations() {
        return annotations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Entity))
            return false;

        Entity entity1 = (Entity) o;

        if (!Arrays.equals(annotations, entity1.annotations))
            return false;
        if (entity != null ? !entity.equals(entity1.entity) : entity1.entity != null)
            return false;
        if (variant != null ? !variant.equals(entity1.variant) : entity1.variant != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = entity != null ? entity.hashCode() : 0;
        result = 31 * result + (variant != null ? variant.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(annotations);
        return result;
    }

    @Override
    public String toString() {
        return "Entity{" + "entity=" + entity + ", variant=" + variant + ", annotations=" + Arrays.toString(annotations) + '}';
    }
}
