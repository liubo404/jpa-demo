package xxx.com.tutorial.db;

/**
 * Created by Thinkpad on 2017/4/5.
 */

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Target({TYPE, METHOD})
@Retention(RUNTIME)
public @interface DataSource {
    String value();
}
