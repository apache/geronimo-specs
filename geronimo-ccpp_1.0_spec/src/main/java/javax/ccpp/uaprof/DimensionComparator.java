/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */


package javax.ccpp.uaprof;

import java.util.Comparator;

/**
 * @version $Rev$ $Date$
 */
public class DimensionComparator implements Comparator {

    private static final DimensionComparator instance = new DimensionComparator();

    public static DimensionComparator getInstance() {
        return instance;
    }

    private DimensionComparator() {
    }

    public int compare(Object o, Object o1) {
        Dimension d1 = (Dimension) o;
        Dimension d2 = (Dimension) o1;
        if (d1.getWidth() == d2.getWidth()) {
            return d1.getHeight() - d2.getHeight();
        }
        return d1.getWidth() - d1.getWidth();
    }
}
