package org.tat.gginl.api.controller.simpleLifeController;

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
import org.tat.gginl.api.dto.simpleLifeDTO.SimpleLifeDTO;
import org.tat.gginl.api.dto.simpleLifeDTO.SimpleLifeReponseDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/simpleLife")
@Api(tags = "Simple Life Proposal")
public class SimpleLifeController {

  @Autowired
  private LifeProposalService lifeProposalService;

  @Autowired
  private ModelMapper mapper;

  @PostMapping("/submitproposal")
  @ApiResponses(value = {@ApiResponse(code = 400, message = "Something went wrong"),
      @ApiResponse(code = 403, message = "Access denied"),
      @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
  @ApiOperation(value = "${SimpleLifeController.submitProposal}")
  public ResponseDTO<Object> submitproposal(
      @ApiParam("Submit SampleLife Proposal") @Valid @RequestBody SimpleLifeDTO sampleLifeProposalDTO) {
    List<LifePolicy> policyList = new ArrayList<>();
    SimpleLifeDTO simpleLifeDTO = mapper.map(sampleLifeProposalDTO, SimpleLifeDTO.class);

    // create sample Life proposal
    policyList = lifeProposalService.createSimpleLifePolicy(simpleLifeDTO);

    // create response object
    List<SimpleLifeReponseDTO> responseList = new ArrayList<>();

    policyList.forEach(policy -> {
      SimpleLifeReponseDTO dto = SimpleLifeReponseDTO.builder()
          .bpmsInsuredPersonId(policy.getPolicyInsuredPersonList().get(0).getBpmsInsuredPersonId())
          .proposalNo(policy.getLifeProposal().getProposalNo()).policyNo(policy.getPolicyNo())
          .customerId(policy.getPolicyInsuredPersonList().get(0).isNewCustomer()
              ? policy.getPolicyInsuredPersonList().get(0).getCustomer().getId()
              : null)
          .build();
      responseList.add(dto);
    });

    ResponseDTO<Object> responseDTO =
        ResponseDTO.builder().status("Success!").responseBody(responseList).build();

    return responseDTO;
  }

}


