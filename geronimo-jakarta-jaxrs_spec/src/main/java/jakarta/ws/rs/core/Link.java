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

package jakarta.ws.rs.core;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jakarta.ws.rs.ext.RuntimeDelegate;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.namespace.QName;

public abstract class Link {

    public static final String TITLE = "title";

    public static final String REL = "rel";

    public static final String TYPE = "type";

    public abstract URI getUri();

    public abstract UriBuilder getUriBuilder();

    public abstract String getRel();

    public abstract List<String> getRels();

    public abstract String getTitle();

    public abstract String getType();

    public abstract Map<String, String> getParams();

    @Override
    public abstract String toString();

    public static Link valueOf(String value) {
        Builder b = RuntimeDelegate.getInstance().createLinkBuilder();
        b.link(value);
        return b.build();
    }

    public static Builder fromUri(URI uri) {
        Builder b = RuntimeDelegate.getInstance().createLinkBuilder();
        b.uri(uri);
        return b;
    }

    public static Builder fromUri(String uri) {
        Builder b = RuntimeDelegate.getInstance().createLinkBuilder();
        b.uri(uri);
        return b;
    }

    public static Builder fromUriBuilder(UriBuilder uriBuilder) {
        Builder b = RuntimeDelegate.getInstance().createLinkBuilder();
        b.uriBuilder(uriBuilder);
        return b;
    }

    public static Builder fromLink(Link link) {
        Builder b = RuntimeDelegate.getInstance().createLinkBuilder();
        b.link(link);
        return b;
    }

    public static Builder fromPath(String path) {
        return fromUriBuilder(UriBuilder.fromPath(path));
    }

    public static Builder fromResource(Class<?> resource) {
        return fromUriBuilder(UriBuilder.fromResource(resource));
    }

    public static Builder fromMethod(Class<?> resource, String method) {
        return fromUriBuilder(UriBuilder.fromMethod(resource, method));
    }

    public interface Builder {

        public Builder link(Link link);

        public Builder link(String link);

        public Builder uri(URI uri);

        public Builder uri(String uri);

        public Builder baseUri(URI uri);

        public Builder baseUri(String uri);

        public Builder uriBuilder(UriBuilder uriBuilder);

        public Builder rel(String rel);

        public Builder title(String title);

        public Builder type(String type);

        public Builder param(String name, String value);

        public Link build(Object... values);

        public Link buildRelativized(URI uri, Object... values);
    }

    public static class JaxbLink {

        private URI uri;

        private Map<QName, Object> params;

        public JaxbLink() {
        }

        public JaxbLink(URI uri) {
            this.uri = uri;
        }

        public JaxbLink(URI uri, Map<QName, Object> params) {
            this.uri = uri;
            this.params = params;
        }

        @XmlAttribute(name = "href")
        public URI getUri() {
            return uri;
        }

        @XmlAnyAttribute
        public Map<QName, Object> getParams() {
            if (params == null) {
                params = new HashMap<QName, Object>();
            }
            return params;
        }

        void setUri(URI uri) {
            this.uri = uri;
        }

        void setParams(Map<QName, Object> params) {
            this.params = params;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (!(o instanceof JaxbLink))
                return false;

            JaxbLink jaxbLink = (JaxbLink) o;

            if (uri != null ? !uri.equals(jaxbLink.uri) : jaxbLink.uri != null) {
                return false;
            }

            if (params == jaxbLink.params) {
                return true;
            }
            if (params == null) {
                // if this.params is 'null', consider other.params equal to empty
                return jaxbLink.params.isEmpty();
            }
            if (jaxbLink.params == null) {
                // if other.params is 'null', consider this.params equal to empty
                return params.isEmpty();
            }

            return params.equals(jaxbLink.params);
        }

        @Override
        public int hashCode() {
            int result = uri != null ? uri.hashCode() : 0;
            result = 31 * result + (params != null && !params.isEmpty() ? params.hashCode() : 0);
            return result;
        }

    }

    public static class JaxbAdapter extends XmlAdapter<JaxbLink, Link> {

        @Override
        public Link unmarshal(JaxbLink v) {
            Link.Builder lb = Link.fromUri(v.getUri());
            for (Entry<QName, Object> e : v.getParams().entrySet()) {
                lb.param(e.getKey().getLocalPart(), e.getValue().toString());
            }
            return lb.build();
        }

        @Override
        public JaxbLink marshal(Link v) {
            JaxbLink jl = new JaxbLink(v.getUri());
            for (Entry<String, String> e : v.getParams().entrySet()) {
                final String name = e.getKey();
                jl.getParams().put(new QName("", name), e.getValue());
            }
            return jl;
        }
    }
}
