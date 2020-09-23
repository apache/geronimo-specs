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

package javax.mail.search;

/**
 * Base for comparison terms.
 *
 * @version $Rev$ $Date$
 */
public abstract class ComparisonTerm extends SearchTerm {
	
	private static final long serialVersionUID =  1456646953666474308L;
	
    public static final int LE = 1;
    public static final int LT = 2;
    public static final int EQ = 3;
    public static final int NE = 4;
    public static final int GT = 5;
    public static final int GE = 6;

    protected int comparison;

    public ComparisonTerm() {
    }

    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof ComparisonTerm)) {
            return false; 
        }
        return comparison == ((ComparisonTerm)other).comparison;
    }

    @Override
    public int hashCode() {
        return comparison; 
    }
}
