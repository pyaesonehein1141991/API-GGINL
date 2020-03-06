package org.tat.gginl.api.validator;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.tat.gginl.api.dto.GroupLifeDTO.GroupLifeProposalInsuredPersonDTO;

public class MaxSizeConstraintValidator implements ConstraintValidator<MaxSizeConstraint, List<GroupLifeProposalInsuredPersonDTO>> {

	@Override
	public boolean isValid(List<GroupLifeProposalInsuredPersonDTO> value, ConstraintValidatorContext context) {

		return value.size() >= 5;
	}

}
