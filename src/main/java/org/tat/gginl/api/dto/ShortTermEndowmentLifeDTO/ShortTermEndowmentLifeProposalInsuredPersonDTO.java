package org.tat.gginl.api.dto.ShortTermEndowmentLifeDTO;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.tat.gginl.api.configuration.DateHandler;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ShortTermEndowmentLifeProposalInsuredPersonDTO {

	@ApiModelProperty(position = 0, example = "U", required = true)
	@NotBlank(message = "InitialId is mandatory")
	private String initialId;

	@ApiModelProperty(position = 1, example = "36221e42-a0000040-133dc830-59dccb1a", required = true)
	@NotBlank(message = "BPMS InsuredPersonId is mandatory")
	private String bpmsInsuredPersonId;

	@ApiModelProperty(position = 2, example = "Khin", required = true)
	@NotBlank(message = "FirstName is mandatory")
	private String firstName;

	@ApiModelProperty(position = 3, example = "Thein")
	private String middleName;

	@ApiModelProperty(position = 4, example = "Kyu")
	private String lastName;

	@ApiModelProperty(position = 6, example = "U Thein", required = true)
	@NotBlank(message = "FatherName is mandatory")
	private String fatherName;

	@ApiModelProperty(position = 9, example = "NRCNO", required = true)
	@NotBlank(message = "idType is mandatory")
	private String idType;

	@ApiModelProperty(position = 10, example = "9/MAMANA(N)456789")
	private String idNo;

	@ApiModelProperty(position = 5, example = "1998-12-16", required = true)
	@NotNull(message = "DateOfBirth is mandatory")
	@JsonDeserialize(using = DateHandler.class)
	private Date dateOfBirth;

	@ApiModelProperty(position = 17, example = "Yangon", required = true)
	@NotBlank(message = "residentAddress is mandatory")
	private String residentAddress;

	@ApiModelProperty(position = 16, example = "ISSYS004000009724620062019", required = true)
	@NotBlank(message = "residentTownshipId is mandatory")
	private String residentTownshipId;

	@ApiModelProperty(position = 11, example = "ISSYS011000009823001042019", required = true)
	@NotBlank(message = "occupationId is mandatory")
	private String occupationId;

	@ApiModelProperty(position = 6, example = "ISSYS001000018029101042019")
	private String customerID;

	@ApiModelProperty(position = 13, example = "FEMALE", required = true)
	@NotBlank(message = "Gender is mandatory")
	private String gender;

	@ApiModelProperty(position = 2, example = "100000", required = true)
	@NotNull(message = "proposedSumInsured is mandatory")
	private double proposedSumInsured;

	@ApiModelProperty(position = 3, example = "100000", required = true)
	@NotNull(message = "proposedPremium is mandatory")
	private double proposedPremium;

	@ApiModelProperty(position = 4, example = "10000", required = true)
	@NotNull(message = "approvedSumInsured is mandatory")
	private double approvedSumInsured;

	@ApiModelProperty(position = 5, example = "10000", required = true)
	@NotNull(message = "approvedPremium is mandatory")
	private double approvedPremium;

	@ApiModelProperty(position = 6, example = "10000", required = true)
	@NotNull(message = "basicTermPremium is mandatory")
	private double basicTermPremium;

	@ApiModelProperty(position = 10, example = "2019-12-16", required = true)
	@NotNull(message = "startDate is mandatory")
	@JsonDeserialize(using = DateHandler.class)
	private Date startDate;

	@ApiModelProperty(position = 11, example = "2019-12-16", required = true)
	@NotNull(message = "endDate is mandatory")
	@JsonDeserialize(using = DateHandler.class)
	private Date endDate;

	@Valid
	@ApiModelProperty(position = 21)
	@NotNull(message = "insuredPersonBeneficiariesList is mandatory")
	private List<ShortTermEndowmentLifeProposalInsuredPersonBeneficiariesDTO> insuredPersonBeneficiariesList;

}
