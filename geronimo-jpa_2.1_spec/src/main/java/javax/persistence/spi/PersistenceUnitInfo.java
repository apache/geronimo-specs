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

//
// This source code implements specifications defined by the Java
// Community Process. In order to remain compliant with the specification
// DO NOT add / change / or delete method signatures!
//

package javax.persistence.spi;

import javax.sql.DataSource;
import java.util.List;
import java.util.Properties;
import java.net.URL;
import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;


public interface PersistenceUnitInfo {
    public String getPersistenceUnitName();

    public String getPersistenceProviderClassName();

    public PersistenceUnitTransactionType getTransactionType();

    public DataSource getJtaDataSource();

    public DataSource getNonJtaDataSource();

    public List<String> getMappingFileNames();

    public List<URL> getJarFileUrls();

    public URL getPersistenceUnitRootUrl();

    public List<String> getManagedClassNames();

    public boolean excludeUnlistedClasses();

    public SharedCacheMode getSharedCacheMode();

    public ValidationMode getValidationMode();

    public Properties getProperties();

    public String getPersistenceXMLSchemaVersion();

    public ClassLoader getClassLoader();

    public void addTransformer(ClassTransformer transformer);

    public ClassLoader getNewTempClassLoader();
}
