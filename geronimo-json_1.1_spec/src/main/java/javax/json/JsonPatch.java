/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package javax.json;

/**
 * <p>
 * JsonPatch is a format for expressing a sequence of operations to apply on
 * a target JSON document.
 * </p>
 * <p>
 * A JsonPatch is an array of 'operations' in the form e.g.
 * <p>
 * <pre>
 * [
 *   { "op": "add", "path": "/foo/-", "value": ["abc", "def"] }
 *   { "path": "/a/b/c", "op": "add", "value": "foo" }
 * ]
 * </pre>
 * </p>
 * <p>
 * The 'operations' are performed in the order they are in the JsonPatch and applied
 * to the 'result' JSON document from the previous operation.
 * </p>
 * <p>
 * Supported operations are
 * <ul>
 * <li>ADD}</li>
 * <li>REMOVE}</li>
 * <li>REPLACE}</li>
 * <li>MOVE}</li>
 * <li>COPY}</li>
 * <li>TEST}</li>
 * </ul>
 * </p>
 * <p>
 * for more infos see <a href="https://tools.ietf.org/html/rfc6902">RFC-6902</a>
 * </p>
 * <p>
 * NOTICE: All JsonValues are immutable and therefore every {@code apply()} method will return
 * new references when the {@link JsonPatch} is applied.
 * </p>
 *
 * @since 1.1
 */
public interface JsonPatch {

    /**
     * @param target - the target to apply the {@link JsonPatch}
     * @return 'patched' {@link JsonStructure}
     * @throws NullPointerException if {@code target} is {@code null}
     */
    <T extends JsonStructure> T apply(T target);
}

