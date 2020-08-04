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

package javax.mail.event;

import javax.mail.Folder;

/**
 * @version $Rev$ $Date$
 */
public class FolderEvent extends MailEvent {
	
	private static final long serialVersionUID = 5278131310563694307L;
	
    public static final int CREATED = 1;
    public static final int DELETED = 2;
    public static final int RENAMED = 3;

    protected transient Folder folder;
    protected transient Folder newFolder;
    protected int type;

    /**
     * Constructor used for RENAMED events.
     *
     * @param source the source of the event
     * @param oldFolder the folder that was renamed
     * @param newFolder the folder with the new name
     * @param type the event type
     */
    public FolderEvent(final Object source, final Folder oldFolder, final Folder newFolder, final int type) {
        super(source);
        folder = oldFolder;
        this.newFolder = newFolder;
        this.type = type;
    }

    /**
     * Constructor other events.
     *
     * @param source the source of the event
     * @param folder the folder affected
     * @param type the event type
     */
    public FolderEvent(final Object source, final Folder folder, final int type) {
        this(source, folder, null, type);
    }

    @Override
    public void dispatch(final Object listener) {
        final FolderListener l = (FolderListener) listener;
        switch (type) {
        case CREATED:
            l.folderCreated(this);
            break;
        case DELETED:
            l.folderDeleted(this);
            break;
        case RENAMED:
            l.folderRenamed(this);
            break;
        default:
            throw new IllegalArgumentException("Invalid type " + type);
        }
    }

    /**
     * Return the affected folder.
     * @return the affected folder
     */
    public Folder getFolder() {
        return folder;
    }

    /**
     * Return the new folder; only applicable to RENAMED events.
     * @return the new folder
     */
    public Folder getNewFolder() {
        return newFolder;
    }

    /**
     * Return the event type.
     * @return the event type
     */
    public int getType() {
        return type;
    }
}
