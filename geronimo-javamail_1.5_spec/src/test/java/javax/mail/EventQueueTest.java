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

import java.util.Vector;

import javax.mail.event.FolderEvent;
import javax.mail.event.FolderListener;

import junit.framework.TestCase;

/**
 * @version $Rev$ $Date$
 */
public class EventQueueTest extends TestCase {
    protected EventQueue queue; 
    
    @Override
    public void setUp() throws Exception {
        queue = new EventQueue();
    }
    
    @Override
    public void tearDown() throws Exception {
        queue.stop(); 
    }
    
    public void testEvent() {
        doEventTests(FolderEvent.CREATED);
        doEventTests(FolderEvent.RENAMED);
        doEventTests(FolderEvent.DELETED);
    }
    
    private void doEventTests(final int type) {
        
        // These tests are essentially the same as the 
        // folder event tests, but done using the asynchronous 
        // event queue.  
        final FolderEvent event = new FolderEvent(this, null, type);
        assertEquals(this, event.getSource());
        assertEquals(type, event.getType());
        final FolderListenerTest listener = new FolderListenerTest();
        final Vector listeners = new Vector(); 
        listeners.add(listener); 
        queue.queueEvent(event, listeners);
        // we need to make sure the queue thread has a chance to dispatch 
        // this before we check. 
        try {
            Thread.currentThread();
            Thread.sleep(1000); 
        } catch (final InterruptedException e ) {
        }
        assertEquals("Unexpcted method dispatched", type, listener.getState());
    }
    
    public static class FolderListenerTest implements FolderListener {
        private int state = 0;
        public void folderCreated(final FolderEvent event) {
            if (state != 0) {
                fail("Recycled Listener");
            }
            state = FolderEvent.CREATED;
        }
        public void folderDeleted(final FolderEvent event) {
            if (state != 0) {
                fail("Recycled Listener");
            }
            state = FolderEvent.DELETED;
        }
        public void folderRenamed(final FolderEvent event) {
            if (state != 0) {
                fail("Recycled Listener");
            }
            state = FolderEvent.RENAMED;
        }
        public int getState() {
            return state;
        }
    }
}

