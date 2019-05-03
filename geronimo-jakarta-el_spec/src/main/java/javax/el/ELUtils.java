/**
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package javax.el;

class ELUtils {

    private static volatile ExpressionFactory cachedExpressionFactory;

    private static boolean cachedExpressionFactoryEnabled = Boolean.valueOf(System.getProperty("org.apache.geronimo.spec.el.useCachedExpressionFactory", "true"));

    public static ExpressionFactory getCachedExpressionFactory() {
        return cachedExpressionFactory;
    }

    public static boolean isCachedExpressionFactoryEnabled() {
        return cachedExpressionFactoryEnabled;
    }

    public static void setCachedExpressionFactory(ExpressionFactory expressionFactory) {
        cachedExpressionFactory = expressionFactory;
    }

    public static String coerceToString(Object obj) {
        if (obj == null) {
            return "";
        } else if (obj instanceof String) {
            return (String) obj;
        } else if (obj instanceof Enum<?>) {
            return ((Enum<?>) obj).name();
        } else {
            return obj.toString();
        }
    }
}
