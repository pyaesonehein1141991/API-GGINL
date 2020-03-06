package org.tat.gginl.api.validator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = MaxSizeConstraintValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface MaxSizeConstraint {

	String message() default "At least 5 insured person must have.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
