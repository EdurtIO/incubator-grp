package io.edurt.grp.web.annotation;

import io.edurt.grp.web.type.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping
{
    /**
     * 请求路径
     *
     * @return 请求路径
     */
    String[] value() default {};

    /**
     * 请求方式
     *
     * @return 请求方式
     * @see RequestMethod
     */
    RequestMethod[] method() default {};
}
