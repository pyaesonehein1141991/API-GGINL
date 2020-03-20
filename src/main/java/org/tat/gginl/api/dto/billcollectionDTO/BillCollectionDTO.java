package org.tat.gginl.api.dto.billcollectionDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BillCollectionDTO {
	

	@ApiModelProperty(position = 1, example = "S/1909/0000000053", required = true)
	@NotBlank(message = "amount is mandatory")
	private String policyNo;

	@ApiModelProperty(position = 2, example = "1", required = true)
	private int  paymentTimes;
	
	@ApiModelProperty(position = 3, example = "30000", required = true)
	@NotNull(message = "Total SI is mandatory")
	private double amount;
	
	@ApiModelProperty(position = 4, example = "CSH", required = true)
	@NotBlank(message = "payment channel is mandatory")
	private String paymentChannel;
	
	@ApiModelProperty(position = 5, example = "1", required = true)
	@NotBlank(message = "payment channel is mandatory")
	private String salePointId;
	
	@ApiModelProperty(position = 6, example = "ISSYS010000009558003042019")
	private String fromBank;
	
	
	@ApiModelProperty(position = 7, example = "ISSYS010000009558103042019")
	private String toBank;
	
	@ApiModelProperty(position = 8, example = "9558103042019")
	private String chequeNo;
	
            

	
	
	


}
