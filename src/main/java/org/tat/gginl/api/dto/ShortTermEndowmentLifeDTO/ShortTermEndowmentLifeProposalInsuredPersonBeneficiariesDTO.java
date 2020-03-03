package org.tat.gginl.api.dto.ShortTermEndowmentLifeDTO;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.tat.gginl.api.configuration.DateHandler;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ShortTermEndowmentLifeProposalInsuredPersonBeneficiariesDTO {

	@ApiModelProperty(position = 0, example = "U", required = true)
	@NotBlank(message = "InitialId is mandatory")
	private String initialId;

	@ApiModelProperty(position = 2, example = "Kyaw", required = true)
	@NotBlank(message = "FirstName is mandatory")
	private String firstName;

	@ApiModelProperty(position = 3, example = "Thein")
	private String middleName;

	@ApiModelProperty(position = 4, example = "Win")
	private String lastName;

	@ApiModelProperty(position = 9, example = "NRCNO", required = true)
	@NotBlank(message = "idType is mandatory")
	private String idType;

	@ApiModelProperty(position = 10, example = "9/MAMANA(N)346875")
	private String idNo;

	@ApiModelProperty(position = 14, example = "MALE", required = true)
	@NotNull(message = "gender is mandatory")
	private String gender;

	@ApiModelProperty(position = 14, example = "ISSYS012001000000316823022016", required = true)
	@NotBlank(message = "relationshipId is mandatory")
	private String relationshipId;

	@ApiModelProperty(position = 2, example = "5", required = true)
	@NotNull(message = "percentage is mandatory")
	private float percentage;

	@ApiModelProperty(position = 1, example = "2019-12-16", required = true)
	@NotNull(message = "dob is mandatory")
	@JsonDeserialize(using = DateHandler.class)
	private Date dob;

	@ApiModelProperty(position = 6, example = "Yangon", required = true)
	@NotBlank(message = "Address is mandatory")
	private String residentAddress;

	@ApiModelProperty(position = 11, example = "ISSYS004002000000127620082014", required = true)
	@NotBlank(message = "townshipId is mandatory")
	@NotEmpty
	private String townshipId;

}
