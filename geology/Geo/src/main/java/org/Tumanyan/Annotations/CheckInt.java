package org.Tumanyan.Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target( {ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MyCheckImpl.class)
public @interface CheckInt {

    String message() default "неизвестная ошибка";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}