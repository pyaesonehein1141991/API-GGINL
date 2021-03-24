package org.tat.gginl.api.dto.customerDTO;

import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.tat.gginl.api.common.NameDto;
import org.tat.gginl.api.common.emumdata.Gender;
import org.tat.gginl.api.common.emumdata.IdType;
import org.tat.gginl.api.common.emumdata.MaritalStatus;
import org.tat.gginl.api.configuration.DateHandler;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GuardianDto {
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(position = 0, required = false)
	private String initialId;

	@ApiModelProperty(position = 1, required = false)
	private String fatherName;

	@ApiModelProperty(position = 2, example="009354", required = false)
	private String idNo;

	@ApiModelProperty(position = 3, example = "2020-12-16", required = true)
	@NotNull(message = "DateOfBirth is mandatory")
	@JsonDeserialize(using = DateHandler.class)
	private Date dateOfBirth;

	@ApiModelProperty(position = 5,example = "MALE", required = true)
	@NotNull(message = "Gender is mandatory")
	private Gender gender;

	@ApiModelProperty(position = 6,example = "NRCNO", required = true)
	@NotNull(message = "Id Type is mandatory")
	private IdType idType;

	@ApiModelProperty(position = 7, example = "SINGLE", required = true)
	@NotNull(message = "Marital status is mandatory")
	private MaritalStatus maritalStatus;

	@Valid
	@ApiModelProperty(position = 9, required = false)
	private PermanentAddressDto permanentAddress;

	@Valid
	@ApiModelProperty(position = 10, required = true)
	@NotNull(message = "ResidentAddress is mandatory")
	private ResidentAddressDto residentAddress;

	@Valid
	@ApiModelProperty(position = 12, required = true)
	@NotNull(message = "Name is mandatory")
	private NameDto name;

}
