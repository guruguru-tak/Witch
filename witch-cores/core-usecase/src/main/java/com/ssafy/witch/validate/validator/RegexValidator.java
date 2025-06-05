package com.ssafy.witch.validate.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;
import java.util.Objects;
import java.util.regex.Pattern;

public abstract class RegexValidator<A extends Annotation, T> implements ConstraintValidator<A, T> {

  protected Pattern pattern;

  @Override
  public void initialize(A constraintAnnotation) {
    pattern = Pattern.compile(getRegex());
  }

  @Override
  public boolean isValid(T value, ConstraintValidatorContext context) {

    if (Objects.isNull(value) || !pattern.matcher(convertToString(value)).matches()) {
      addConstraintViolation(context, getErrorMessage());
      return false;
    }

    return true;
  }

  // 필수 구현 메소드
  protected abstract String getRegex();

  protected abstract String getErrorMessage();

  // 기본 구현을 제공하지만 필요시 오버라이드 가능한 메소드
  protected String convertToString(T value) {
    return value.toString();
  }


  protected void addConstraintViolation(ConstraintValidatorContext context, String message) {
    context.disableDefaultConstraintViolation();
    context.buildConstraintViolationWithTemplate(message)
        .addConstraintViolation();
  }
}
