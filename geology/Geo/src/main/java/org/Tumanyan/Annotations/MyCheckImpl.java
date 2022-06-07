package org.Tumanyan.Annotations;

import java.lang.annotation.Annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MyCheckImpl extends NumberFormatException implements ConstraintValidator<CheckInt, Integer> {

    @Override
    public void initialize(CheckInt arg0) {

    }

    @Override
    public boolean isValid(Integer arg0, ConstraintValidatorContext arg1) {
          return arg0 != 0;
    }

}