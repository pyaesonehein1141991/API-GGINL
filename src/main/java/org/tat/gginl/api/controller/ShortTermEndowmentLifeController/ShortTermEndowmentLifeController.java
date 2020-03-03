package org.tat.gginl.api.controller.ShortTermEndowmentLifeController;

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
import org.tat.gginl.api.dto.ShortTermEndowmentLifeDTO.ShortTermEndowmentLifeProposalDTO;
import org.tat.gginl.api.dto.ShortTermEndowmentLifeDTO.ShortTermEndowmentLifeReponseDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/shorttermendowmentlife")
@Api(tags = "ShortTermEndowmentLife Proposal")
public class ShortTermEndowmentLifeController {

	@Autowired
	private LifeProposalService lifeProposalService;

	@Autowired
	private ModelMapper mapper;

	@PostMapping("/submitproposal")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Something went wrong"), @ApiResponse(code = 403, message = "Access denied"),
			@ApiResponse(code = 500, message = "Expired or invalid JWT token") })
	public ResponseDTO<Object> submitproposal(@Valid @RequestBody ShortTermEndowmentLifeProposalDTO shorttermendowmentLifeProposalDTO) {
		List<LifePolicy> policyList = new ArrayList<>();
		ShortTermEndowmentLifeProposalDTO a = mapper.map(shorttermendowmentLifeProposalDTO, ShortTermEndowmentLifeProposalDTO.class);

		// create short term endowment proposal
		policyList = lifeProposalService.createShortTermEndowmentLifeProposalToPolicy(a);

		// create response object
		List<ShortTermEndowmentLifeReponseDTO> responseList = new ArrayList<>();

		policyList.forEach(policy -> {
			ShortTermEndowmentLifeReponseDTO dto = ShortTermEndowmentLifeReponseDTO.builder()
					.bpmsInsuredPersonId(policy.getPolicyInsuredPersonList().get(0).getBpmsInsuredPersonId()).proposalNo(policy.getLifeProposal().getProposalNo())
					.policyNo(policy.getPolicyNo()).customerId(policy.getPolicyInsuredPersonList().get(0).isNewCustomer() ? policy.getCustomer().getId() : null).build();
			responseList.add(dto);
		});

		ResponseDTO<Object> responseDTO = ResponseDTO.builder().responseStatus("Success!").responseBody(responseList).build();

		return responseDTO;
	}
}
