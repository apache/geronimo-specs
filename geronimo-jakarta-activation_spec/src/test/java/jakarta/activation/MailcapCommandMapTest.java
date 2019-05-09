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

//
// This source code implements specifications defined by the Java
// Community Process. In order to remain compliant with the specification
// DO NOT add / change / or delete method signatures!
//
package jakarta.activation;

import junit.framework.TestCase;

/**
 * @version $Rev$ $Date$
 */
public class MailcapCommandMapTest extends TestCase {
    private MailcapCommandMap map;

    public void testAdd() {
        map.addMailcap("foo/bar ;; x-java-view=Foo; x-java-edit=Bar");
        CommandInfo info = map.getCommand("foo/bar", "view");
        assertEquals("view", info.getCommandName());
        assertEquals("Foo", info.getCommandClass());
        info = map.getCommand("foo/bar", "edit");
        assertEquals("edit", info.getCommandName());
        assertEquals("Bar", info.getCommandClass());
    }

    public void testExplicitWildcard() {
        map.addMailcap("foo/bar ;; x-java-view=Bar");
        map.addMailcap("foo/* ;; x-java-view=Star");
        CommandInfo info = map.getCommand("foo/bar", "view");
        assertEquals("view", info.getCommandName());
        assertEquals("Bar", info.getCommandClass());
        info = map.getCommand("foo/foo", "view");
        assertEquals("view", info.getCommandName());
        assertEquals("Star", info.getCommandClass());
        info = map.getCommand("foo/*", "view");
        assertEquals("view", info.getCommandName());
        assertEquals("Star", info.getCommandClass());
// The reference implementation does not appear to handle this the same way.
//        info = map.getCommand("foo", "view");
//        assertEquals("view", info.getCommandName());
//        assertEquals("Star", info.getCommandClass());
    }

    public void testImplicitWildcard() {
        map.addMailcap("foo/bar ;; x-java-view=Bar");
        map.addMailcap("foo ;; x-java-view=Star");
        CommandInfo info = map.getCommand("foo/bar", "view");
        assertEquals("view", info.getCommandName());
        assertEquals("Bar", info.getCommandClass());
        info = map.getCommand("foo/foo", "view");
        assertEquals("view", info.getCommandName());
        assertEquals("Star", info.getCommandClass());
// The RI is not finding this one either
//        info = map.getCommand("foo/*", "view");
//        assertEquals("view", info.getCommandName());
//        assertEquals("Star", info.getCommandClass());
    }

    public void testParameterizedMimeType() {
// TODO:  the RI is not getting a hit on this one.
//        map.addMailcap("foo/bar ;; x-java-view=Bar");
//        CommandInfo info = map.getCommand("foo/bar ; type=\"text/plain\"", "view");
//        assertEquals(
//        assertEquals("view", info.getCommandName());
//        assertEquals("Bar", info.getCommandClass());
    }

    public void testGetNativeCommands() {
        MailcapCommandMap tmap = new MailcapCommandMap();
        // a few filler entries just to increase the noise level.
        tmap.addMailcap("image/gif;;x-java-view=com.sun.activation.viewers.ImageViewer");
        tmap.addMailcap("image/jpeg;;x-java-view=com.sun.activation.viewers.ImageViewer");
        tmap.addMailcap("text/*;yada yada;x-java-view=com.sun.activation.viewers.TextViewer");
        // neither of these is a match
        tmap.addMailcap("text/*;;x-java-edit=com.sun.activation.viewers.TextEditor");
        tmap.addMailcap("text/*; ;x-java-edit=com.sun.activation.viewers.TextEditor");

        // need one with multiple entries
        tmap.addMailcap("text/plain;yada yada;x-java-view=com.sun.activation.viewers.TextViewer");
        tmap.addMailcap("text/plain;bad a bing;x-java-view=com.sun.activation.viewers.TextViewer");

        String[] commands = tmap.getNativeCommands("text/*");

        assertEquals(1, commands.length);
        assertEquals("text/*;yada yada;x-java-view=com.sun.activation.viewers.TextViewer", commands[0]);

        commands = tmap.getNativeCommands("text/plain");
        assertEquals(2, commands.length);
        assertTrue("text/plain;yada yada;x-java-view=com.sun.activation.viewers.TextViewer".equals(commands[0]) ||
            "text/plain;bad a bing;x-java-view=com.sun.activation.viewers.TextViewer".equals(commands[0]));
        assertTrue("text/plain;yada yada;x-java-view=com.sun.activation.viewers.TextViewer".equals(commands[1]) ||
            "text/plain;bad a bing;x-java-view=com.sun.activation.viewers.TextViewer".equals(commands[1]));

        commands = tmap.getNativeCommands("image/gif");
        assertEquals(0, commands.length);

        commands = tmap.getNativeCommands("text/html");
        assertEquals(0, commands.length);
    }

    protected void setUp() throws Exception {
        super.setUp();
        map = new MailcapCommandMap();
    }
}
