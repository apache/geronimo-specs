package javax.xml.bind.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * Created by IntelliJ IDEA.
 * User: gnodet
 * Date: Apr 17, 2008
 * Time: 11:05:30 AM
 * To change this template use File | Settings | File Templates.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value  = {ElementType.FIELD, ElementType.METHOD })
public @interface XmlAnyElement {

    boolean lax() default false;

    Class<? extends DomHandler> value() default W3CDomHandler.class;
}
