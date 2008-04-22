package javax.xml.bind.annotation;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * Created by IntelliJ IDEA.
 * User: gnodet
 * Date: Apr 17, 2008
 * Time: 11:02:25 AM
 * To change this template use File | Settings | File Templates.
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.PACKAGE, ElementType.TYPE})
public @interface XmlAccessorOrder {

    XmlAccessOrder value() default XmlAccessOrder.UNDEFINED;
}
