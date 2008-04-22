package javax.xml.bind.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * Created by IntelliJ IDEA.
 * User: gnodet
 * Date: Apr 17, 2008
 * Time: 11:24:30 AM
 * To change this template use File | Settings | File Templates.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface XmlType {

    class DEFAULT {
    }

    Class factoryClass() default DEFAULT.class;

    String factoryMethod() default "";

    String name() default "##default";

    String namespace() default "##default";

    String[] propOrder() default "";
}
