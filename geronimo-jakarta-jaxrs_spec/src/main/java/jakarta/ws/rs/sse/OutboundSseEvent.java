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

import java.lang.reflect.Type;

import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;

public interface OutboundSseEvent extends SseEvent {

    interface Builder {

        Builder id(String id);

        public Builder name(String name);

        Builder reconnectDelay(long milliseconds);

        Builder mediaType(final MediaType mediaType);

        Builder comment(String comment);

        Builder data(Class type, Object data);

        Builder data(GenericType type, Object data);

        Builder data(Object data);

        OutboundSseEvent build();
    }

    Class<?> getType();

    Type getGenericType();

    MediaType getMediaType();

    Object getData();
}
