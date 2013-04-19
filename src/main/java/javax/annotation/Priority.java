package javax.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to declare a priority for a class.
 * This is used by other specs, e.g. CDI where it is used
 * to define the priority of Interceptors and Decorators.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Priority {

    /**
     * The priority value.
     * The higher the value, the more important the annotated class is.
     */
    int value();
}
