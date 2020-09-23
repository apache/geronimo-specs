JavaMail Specification 1.5
==========================

These classes are based on the JavaMailTM API Design Specification Version 1.5 which can be found here:
o https://javamail.java.net/nonav/docs/JavaMail-1.5.pdf
o http://download.oracle.com/otn-pub/jcp/java_mail-1_5-mrel2-spec/javamail-1.5.0-javadocs.zip

The classes represent the JavaMail API and contain implementations of the 
classes found in the javax.mail packages. In order to function correctly,
these classes require:

o The Java Activation Framework (JAF) 1.1 (or newer) in javax.activation unless you are using Java SE 6 or newer
o J2SE 5.0/JDK 1.5 or newer
o Implementations of the JavaMail providers to deal with pop/imap/etc.
  (You may use this with the geronimo-mail implementation or write your own)

This contains no JavaDoc: see 
  https://java.net/projects/javamail/pages/Home
  http://docs.oracle.com/javaee/7/api/

for more information on how to use JavaMail to send messages.

JAF 1.1.1 is currently the newest version.  Note that JAF 1.1
is included in JDK 1.6 and JAF 1.1.1 is included in JDK 1.6.0_10 and
later.

Configuration
-------------

The JavaMail spec defines the following configuration files:

javamail.providers [Defines which classes are used to map to protocols]
javamail.address.map [Defines which message types (rfc822, news) map to protocols]

They need to be in the CLASSPATH (or in a Jar) in a directory /META-INF/
e.g. c:\mymail\META-INF\javamail.providers

Providers
---------
Provides a protocol, along with its implementation and whether it is a store
or a transport (subclass of javax.mail.Store or javax.mail.Transport)

protocol=smtp;type=transport;class=org.me.MySMTPTransport;vendor=Me Inc;version=1.0
protocol=imap;type=store;class=org.me.MyIMAPStore;vendor=Me Inc;version=1.0

Address Map
-----------

Contains entries in 'name=value' format:
rfc822=smtp
news=nntp

Default
=======

To ensure that other files can be extended at a later stage, the JavaMail
spec defines three locations for these files:

$JAVA_HOME\lib\javamail.properties
META-INF\javamail.properties
META-INF\javamail.default.properties

The files are located in that order and overwrite whatever the previous
version contained, so if 'smtp' is defined in javamail.properties and
javamail.default.properties, it will use the one from javamail.default.properties
