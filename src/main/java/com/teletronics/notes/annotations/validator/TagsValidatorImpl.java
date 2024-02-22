package com.teletronics.notes.annotations.validator;

import com.teletronics.notes.annotations.ValidTags;
import com.teletronics.notes.enums.Tags;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TagsValidatorImpl implements ConstraintValidator<ValidTags, Set<Tags>> {

    Set<String> values;

    @Override
    public void initialize(ValidTags constraintAnnotation) {
        values = Stream.of(constraintAnnotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(Set<Tags> valueSet, ConstraintValidatorContext context) {
        if(valueSet == null || valueSet.isEmpty())
            return true;
        for(Tags tag : valueSet){
            if(!this.values.contains(tag.toString()))
                return false;
        }
        return true;
    }

}