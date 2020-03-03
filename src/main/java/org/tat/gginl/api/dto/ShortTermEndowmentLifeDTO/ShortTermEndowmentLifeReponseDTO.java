package org.tat.gginl.api.dto.ShortTermEndowmentLifeDTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShortTermEndowmentLifeReponseDTO {

	private String bpmsInsuredPersonId;
	private String policyNo;
	private String proposalNo;
	private String customerId;
}
