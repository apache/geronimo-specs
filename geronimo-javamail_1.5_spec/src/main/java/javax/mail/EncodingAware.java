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
package javax.mail;

/**
 * A class that implements EncodingAware may specify the Content-Transfer-Encoding
 * to use for its data.  Valid Content-Transfer-Encoding values specified
 * by RFC 2045 are "7bit", "8bit", "quoted-printable", "base64", and "binary".
 * This is mainly used for {@link javax.activation.DataSource DataSource}s.
 *
 * @since   JavaMail 1.5
 */

public interface EncodingAware {

    /**
     * @return the MIME Content-Transfer-Encoding to use for this data,
     * or null to indicate that an appropriate value should be chosen
     * by the caller.
     */
    public String getEncoding();
}
