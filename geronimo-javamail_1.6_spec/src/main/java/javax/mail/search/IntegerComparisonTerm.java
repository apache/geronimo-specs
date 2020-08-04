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
 * A Term that provides comparisons for integers.
 *
 * @version $Rev$ $Date$
 */
public abstract class IntegerComparisonTerm extends ComparisonTerm {
	
	private static final long serialVersionUID = -6963571240154302484L;
	
    protected int number;

    protected IntegerComparisonTerm(final int comparison, final int number) {
        super();
        this.comparison = comparison;
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public int getComparison() {
        return comparison;
    }

    protected boolean match(final int match) {
        switch (comparison) {
        case EQ:
            return match == number;
        case NE:
            return match != number;
        case GT:
            return match > number;
        case GE:
            return match >= number;
        case LT:
            return match < number;
        case LE:
            return match <= number;
        default:
            return false;
        }
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
			return true;
		}
        if (other instanceof IntegerComparisonTerm == false) {
			return false;
		}
        final IntegerComparisonTerm term = (IntegerComparisonTerm) other;
        return this.comparison == term.comparison && this.number == term.number;
    }

    @Override
    public int hashCode() {
        return number + super.hashCode();
    }
}
