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
package jakarta.ws.rs.sse;

import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import jakarta.ws.rs.client.WebTarget;

public interface SseEventSource extends AutoCloseable {

    abstract class Builder {

        public static final String JAXRS_DEFAULT_SSE_BUILDER_PROPERTY = "org.apache.cxf.jaxrs.sse.client.SseEventSourceBuilderImpl";

        protected Builder() {
        }

        static Builder newBuilder() {
            try {
                Object delegate = SseFinder.find(JAXRS_DEFAULT_SSE_BUILDER_PROPERTY);
                if (!(delegate instanceof Builder)) {
                    Class pClass = Builder.class;
                    String classnameAsResource = pClass.getName().replace('.', '/') + ".class";
                    ClassLoader loader = pClass.getClassLoader();
                    if (loader == null) {
                        loader = ClassLoader.getSystemClassLoader();
                    }
                    URL targetTypeURL = loader.getResource(classnameAsResource);
                    throw new LinkageError("ClassCastException: attempting to cast"
                            + delegate.getClass().getClassLoader().getResource(classnameAsResource) + " to " + targetTypeURL);
                }
                return (Builder) delegate;
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        protected abstract Builder target(WebTarget endpoint);

        public abstract Builder reconnectingEvery(long delay, TimeUnit unit);

        public abstract SseEventSource build();
    }

    void register(Consumer<InboundSseEvent> onEvent);

    void register(Consumer<InboundSseEvent> onEvent, Consumer<Throwable> onError);

    void register(Consumer<InboundSseEvent> onEvent, Consumer<Throwable> onError, Runnable onComplete);

    static Builder target(WebTarget endpoint) {
        return Builder.newBuilder().target(endpoint);
    }

    void open();

    boolean isOpen();

    @Override
    default void close() {
        close(5, TimeUnit.SECONDS);
    }

    boolean close(final long timeout, final TimeUnit unit);
}
