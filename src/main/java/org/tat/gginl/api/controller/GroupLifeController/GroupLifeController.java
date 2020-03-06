package org.tat.gginl.api.controller.GroupLifeController;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tat.gginl.api.domains.LifePolicy;
import org.tat.gginl.api.domains.services.LifeProposalService;
import org.tat.gginl.api.dto.ResponseDTO;
import org.tat.gginl.api.dto.GroupLifeDTO.GroupLifeProposalDTO;
import org.tat.gginl.api.dto.GroupLifeDTO.GroupLifeResponseDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/grouplife")
@Api(tags = "GroupLife Proposal")
public class GroupLifeController {

	@Autowired
	private LifeProposalService lifeProposalService;

	@Autowired
	private ModelMapper mapper;

	@PostMapping("/submitproposal")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Something went wrong"), @ApiResponse(code = 403, message = "Access denied"),
			@ApiResponse(code = 500, message = "Expired or invalid JWT token") })
	public ResponseDTO<Object> submitproposal(@Valid @RequestBody GroupLifeProposalDTO groupLifeproposalDTO) {
		LifePolicy policy = new LifePolicy();
		GroupLifeProposalDTO a = mapper.map(groupLifeproposalDTO, GroupLifeProposalDTO.class);

		// create short term endowment proposal
		policy = lifeProposalService.createGroupLifeProposalToPolicy(a);

		// create response object
		List<GroupLifeResponseDTO> responseList = new ArrayList<>();

		GroupLifeResponseDTO dto = GroupLifeResponseDTO.builder().bpmsInsuredPersonId(policy.getPolicyInsuredPersonList().get(0).getBpmsInsuredPersonId())
				.proposalNo(policy.getLifeProposal().getProposalNo()).policyNo(policy.getPolicyNo())
				.customerId(policy.getPolicyInsuredPersonList().get(0).isNewCustomer() ? policy.getCustomer().getId() : null).build();
		responseList.add(dto);

		ResponseDTO<Object> responseDTO = ResponseDTO.builder().responseStatus("Success!").responseBody(responseList).build();

		return responseDTO;
	}
}