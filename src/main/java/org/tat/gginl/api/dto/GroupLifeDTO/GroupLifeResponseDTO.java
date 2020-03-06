package org.tat.gginl.api.dto.GroupLifeDTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GroupLifeResponseDTO {

	private String bpmsInsuredPersonId;
	private String policyNo;
	private String proposalNo;
	private String customerId;
}
