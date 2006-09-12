/**
 *
 * Copyright 2006 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

//
// This source code implements specifications defined by the Java
// Community Process. In order to remain compliant with the specification
// DO NOT add/change/or delete method signatures!
//

package javax.persistence.spi;

import java.security.ProtectionDomain;
import java.lang.instrument.IllegalClassFormatException;

/**
 * @version $Revision$ $Date$
 */
public interface ClassTransformer {
    /**
     * Invoked when a class is being loaded or redefined. The implementation of this
     * method may transform the supplied class file and return a new replacement class
     * file.
     *
     * @param loader              The defining loader of the class to be transformed, may be null if
     *                            the bootstrap loader
     * @param className           The name of the class in the internal form of fully qualified
     *                            class and interface names
     * @param classBeingRedefined If this is a redefine, the class being redefined,
     *                            otherwise null
     * @param protectionDomain    The protection domain of the class being defined or
     *                            redefined
     * @param classfileBuffer     The input byte buffer in class file format - must not be
     *                            modified
     * @return A well-formed class file buffer (the result of the transform), or null if
     *         no transform is performed
     * @throws java.lang.instrument.IllegalClassFormatException
     *          If the input does not represent a well-formed
     *          class file
     */
    byte[] transform(
            ClassLoader loader,
            String className,
            Class<?> classBeingRedefined,
            ProtectionDomain protectionDomain,
            byte[] classfileBuffer) throws IllegalClassFormatException;

}
