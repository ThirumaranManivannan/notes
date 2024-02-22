package com.teletronics.notes.annotations;

import com.teletronics.notes.annotations.validator.TagsValidatorImpl;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;

@Documented
@Constraint(validatedBy = TagsValidatorImpl.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ValidTags {

Class<? extends Enum<?>> enumClass();
String message() default "must be any of enum {enumClass}";
Class<?>[] groups() default {};
Class<? extends Payload>[] payload() default {};
}