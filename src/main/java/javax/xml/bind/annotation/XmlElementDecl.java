package javax.xml.bind.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;

/**
 * Created by IntelliJ IDEA.
 * User: gnodet
 * Date: Apr 17, 2008
 * Time: 11:08:28 AM
 * To change this template use File | Settings | File Templates.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface XmlElementDecl {

    class GLOBAL {
    }

    String name();

    Class scope() default GLOBAL.class;

    String defautValue() default "##default";

    String namespace() default "##default";

    String substitutionHeadNamespace() default "##default";

    String substitutionHeadName() default "";

    String defaultValue() default "\u0000";
}
