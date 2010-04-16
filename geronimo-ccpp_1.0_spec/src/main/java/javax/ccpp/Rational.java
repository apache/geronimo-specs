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


package javax.ccpp;

import java.io.Serializable;

/**
 * @version $Rev$ $Date$
 */
public class Rational implements Comparable, Serializable {

    private final int numerator;
    private final int denominator;

    public Rational(int numerator, int denominator) throws NumberFormatException {
        if (denominator == 0) {
            throw new NumberFormatException("Rational denominator cannot be 0");
        }
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public Rational(String string) throws NumberFormatException {
        this.numerator = 1;
        this.denominator = 1;
    }

    public int compareTo(Object o) {
        return RationalComparator.getInstance().compare(this, o);
    }

    public double doubleValue() {
        return numerator/denominator;
    }

    public float floatValue() {
        return numerator/denominator;
    }

    public int getNumerator() {
        return numerator;
    }

    public int getDenominator() {
        return denominator;
    }

    public int intValue() {
        return numerator/denominator;
    }
    public long longValue() {
        return numerator/denominator;
    }

    public String toString() {
         return numerator + "/" + denominator;
    }


}
