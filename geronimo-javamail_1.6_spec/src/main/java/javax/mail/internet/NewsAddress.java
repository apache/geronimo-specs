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

package javax.mail.internet;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.mail.Address;

/**
 * A representation of an RFC1036 Internet newsgroup address.
 *
 * @version $Rev$ $Date$
 */
public class NewsAddress extends Address {
	
	private static final long serialVersionUID = -4203797299824684143L;
	
    /**
     * The host for this newsgroup
     */
    protected String host;

    /**
     * The name of this newsgroup
     */
    protected String newsgroup;

    public NewsAddress() {
    }

    public NewsAddress(final String newsgroup) {
        this.newsgroup = newsgroup;
    }

    public NewsAddress(final String newsgroup, final String host) {
        this.newsgroup = newsgroup;
        this.host = host;
    }

    /**
     * The type of this address; always "news".
     * @return "news"
     */
    @Override
    public String getType() {
        return "news";
    }

    public void setNewsgroup(final String newsgroup) {
        this.newsgroup = newsgroup;
    }

    public String getNewsgroup() {
        return newsgroup;
    }

    public void setHost(final String host) {
        this.host = host;
    }

    public String getHost() {
        return host;
    }

    @Override
    public String toString() {
        // Sun impl only appears to return the newsgroup name, no host.
        return newsgroup;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
			return true;
		}
        if (!(o instanceof NewsAddress)) {
			return false;
		}

        final NewsAddress newsAddress = (NewsAddress) o;

        if (host != null ? !host.equals(newsAddress.host) : newsAddress.host != null) {
			return false;
		}
        if (newsgroup != null ? !newsgroup.equals(newsAddress.newsgroup) : newsAddress.newsgroup != null) {
			return false;
		}

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        result = (host != null ? host.toLowerCase().hashCode() : 0);
        result = 29 * result + (newsgroup != null ? newsgroup.hashCode() : 0);
        return result;
    }

    /**
     * Parse a comma-spearated list of addresses.
     *
     * @param addresses the list to parse
     * @return the array of extracted addresses
     * @throws AddressException if one of the addresses is invalid
     */
    public static NewsAddress[] parse(final String addresses) throws AddressException {
        final List result = new ArrayList();
        final StringTokenizer tokenizer = new StringTokenizer(addresses, ",");
        while (tokenizer.hasMoreTokens()) {
            final String address = tokenizer.nextToken().trim();
            final int index = address.indexOf('@');
            if (index == -1) {
                result.add(new NewsAddress(address));
            } else {
                final String newsgroup = address.substring(0, index).trim();
                final String host = address.substring(index+1).trim();
                result.add(new NewsAddress(newsgroup, host));
            }
        }
        return (NewsAddress[]) result.toArray(new NewsAddress[result.size()]);
    }

    /**
     * Convert the supplied addresses to a comma-separated String.
     * If addresses is null, returns null; if empty, returns an empty string.
     *
     * @param addresses the addresses to convert
     * @return a comma-separated list of addresses
     */
    public static String toString(final Address[] addresses) {
        if (addresses == null) {
            return null;
        }
        if (addresses.length == 0) {
            return "";
        }

        final StringBuffer result = new StringBuffer(addresses.length * 32);
        result.append(addresses[0]);
        for (int i = 1; i < addresses.length; i++) {
            result.append(',').append(addresses[i].toString());
        }
        return result.toString();
    }
}
