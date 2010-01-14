/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
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
// DO NOT add / change / or delete method signatures!
//
package javax.annotation.processing;

import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.util.Set;

/**
 * @version $Revision$ $Date$
 */
public abstract class AbstractProcessor implements javax.annotation.processing.Processor {

    protected ProcessingEnvironment processingEnv;

    protected AbstractProcessor() {
    }

    protected boolean isInitialized() {
        throw new UnsupportedOperationException();
    }

    public abstract boolean process(Set<? extends TypeElement> typeElements, RoundEnvironment roundEnvironment);

    public Iterable<? extends Completion> getCompletions(Element element, AnnotationMirror annotationMirror, ExecutableElement executableElement, java.lang.String s) {
        throw new UnsupportedOperationException();
    }

    public Set<java.lang.String> getSupportedAnnotationTypes() {
        throw new UnsupportedOperationException();
    }

    public Set<java.lang.String> getSupportedOptions() {
        throw new UnsupportedOperationException();
    }

    public SourceVersion getSupportedSourceVersion() {
        throw new UnsupportedOperationException();
    }

    public void init(ProcessingEnvironment processingEnvironment) {
        throw new UnsupportedOperationException();
    }

}
