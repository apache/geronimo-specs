package javax.xml.bind.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * Created by IntelliJ IDEA.
 * User: gnodet
 * Date: Apr 17, 2008
 * Time: 11:21:08 AM
 * To change this template use File | Settings | File Templates.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PACKAGE)
public @interface XmlSchema {

    XmlNsForm attributFormDefault() default XmlNsForm.UNSET;

    XmlNsForm elementFormDefault() default XmlNsForm.UNSET;

    String namespace() default "";

    XmlNs[] xmlns() default {};
}
