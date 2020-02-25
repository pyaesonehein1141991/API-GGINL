package org.tat.gginl.api.domains.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tat.gginl.api.common.COACode;
import org.tat.gginl.api.common.CommonCreateAndUpateMarks;
import org.tat.gginl.api.common.DateUtils;
import org.tat.gginl.api.common.Name;
import org.tat.gginl.api.common.PolicyInsuredPerson;
import org.tat.gginl.api.common.PolicyInsuredPersonBeneficiaries;
import org.tat.gginl.api.common.ResidentAddress;
import org.tat.gginl.api.common.TLFBuilder;
import org.tat.gginl.api.common.TranCode;
import org.tat.gginl.api.common.Utils;
import org.tat.gginl.api.common.emumdata.AgentCommissionEntryType;
import org.tat.gginl.api.common.emumdata.ClassificationOfHealth;
import org.tat.gginl.api.common.emumdata.DoubleEntry;
import org.tat.gginl.api.common.emumdata.Gender;
import org.tat.gginl.api.common.emumdata.IdConditionType;
import org.tat.gginl.api.common.emumdata.IdType;
import org.tat.gginl.api.common.emumdata.MaritalStatus;
import org.tat.gginl.api.common.emumdata.PaymentChannel;
import org.tat.gginl.api.common.emumdata.PolicyReferenceType;
import org.tat.gginl.api.common.emumdata.PolicyStatus;
import org.tat.gginl.api.common.emumdata.ProposalType;
import org.tat.gginl.api.common.emumdata.Status;
import org.tat.gginl.api.domains.Agent;
import org.tat.gginl.api.domains.AgentCommission;
import org.tat.gginl.api.domains.Bank;
import org.tat.gginl.api.domains.Branch;
import org.tat.gginl.api.domains.Country;
import org.tat.gginl.api.domains.Customer;
import org.tat.gginl.api.domains.GradeInfo;
import org.tat.gginl.api.domains.GroupFarmerProposal;
import org.tat.gginl.api.domains.InsuredPersonBeneficiaries;
import org.tat.gginl.api.domains.LifePolicy;
import org.tat.gginl.api.domains.LifeProposal;
import org.tat.gginl.api.domains.Occupation;
import org.tat.gginl.api.domains.Organization;
import org.tat.gginl.api.domains.Payment;
import org.tat.gginl.api.domains.PaymentType;
import org.tat.gginl.api.domains.Product;
import org.tat.gginl.api.domains.ProposalInsuredPerson;
import org.tat.gginl.api.domains.RelationShip;
import org.tat.gginl.api.domains.SaleMan;
import org.tat.gginl.api.domains.SalePoint;
import org.tat.gginl.api.domains.School;
import org.tat.gginl.api.domains.StateCode;
import org.tat.gginl.api.domains.TLF;
import org.tat.gginl.api.domains.Township;
import org.tat.gginl.api.domains.TownshipCode;
import org.tat.gginl.api.domains.repository.AgentCommissionRepository;
import org.tat.gginl.api.domains.repository.CustomerRepository;
import org.tat.gginl.api.domains.repository.GroupFarmerRepository;
import org.tat.gginl.api.domains.repository.LifePolicyRepository;
import org.tat.gginl.api.domains.repository.LifeProposalRepository;
import org.tat.gginl.api.domains.repository.PaymentRepository;
import org.tat.gginl.api.domains.repository.TLFRepository;
import org.tat.gginl.api.dto.groupFarmerDTO.FarmerProposalDTO;
import org.tat.gginl.api.dto.groupFarmerDTO.GroupFarmerProposalInsuredPersonBeneficiariesDTO;
import org.tat.gginl.api.dto.groupFarmerDTO.GroupFarmerProposalInsuredPersonDTO;
import org.tat.gginl.api.dto.studentLifeDTO.StudentLifeProposalDTO;
import org.tat.gginl.api.dto.studentLifeDTO.StudentLifeProposalInsuredPersonDTO;
import org.tat.gginl.api.exception.DAOException;
import org.tat.gginl.api.exception.SystemException;

@Service
public class LifeProposalService {

  Logger logger = LoggerFactory.getLogger(this.getClass());
  @Autowired
  private LifeProposalRepository lifeProposalRepo;

  @Autowired
  private LifePolicyRepository lifePolicyRepo;


  @Autowired
  private BranchService   branchService ;

  @Autowired
  private CustomerRepository customerRepo;
  
  @Autowired
  private CustomerService customerService;
  
  @Autowired
  private OrganizationService organizationService;

  @Autowired
  private PaymentTypeService  paymentTypeService;
  

  @Autowired
  private AgentService agentService;

  @Autowired
  private SaleManService saleManService;

  @Autowired
  private SalePointService salePointService;

  @Autowired
  private ProductService productService;

  @Autowired
  private TownShipService townShipService;

  @Autowired
  private OccupationService occupationService;

  @Autowired
  private RelationshipService  relationshipService;

  @Autowired
  private ICustomIdGenerator customIdRepo;

  @Autowired
  private PaymentRepository paymentRepository;

  @Autowired
  private TLFRepository tlfRepository;

  @Autowired
  private AgentCommissionRepository agentCommissionRepo;

  @Autowired
  private BankService bankService;

  @Autowired
  private GroupFarmerRepository groupFarmerRepository;
  
  @Autowired
  private GradeInfoService gradeInfoService;
  
  @Autowired
  private SchoolService schoolService;
  
  @Autowired
  private StateCodeService stateCodeService ;

  
  @Autowired
  private TownShipCodeService townShipCodeService;

  @Autowired
  private CountryService  countryService;


  @Value("${farmerProductId}")
  private String productId;
  
  @Value("${studentLifeProductId}")
  private String studentLifeProductId;

  @Transactional(propagation = Propagation.REQUIRED)
  public List<LifePolicy> createGroupFarmerProposalToPolicy(
      FarmerProposalDTO groupFarmerProposalDTO) {
    try {
      GroupFarmerProposal groupFarmerProposal = createGroupFarmerProposal(groupFarmerProposalDTO);
      groupFarmerProposal = groupFarmerRepository.save(groupFarmerProposal);

      List<LifeProposal> farmerProposalList =
          convertGroupFarmerProposalDTOToProposal(groupFarmerProposalDTO);
      for (LifeProposal proposal : farmerProposalList) {
        proposal.setGroupFarmerProposal(groupFarmerProposal);
      }

      // convert lifeproposal to lifepolicy
      List<LifePolicy> policyList = convertGroupFarmerProposalToPolicy(farmerProposalList);

      // create lifepolicy and return policynoList
      policyList = lifePolicyRepo.saveAll(policyList);

      List<Payment> paymentList = convertGroupFarmerToPayment(groupFarmerProposal, groupFarmerProposalDTO);
      paymentRepository.saveAll(paymentList);

      if (null != groupFarmerProposalDTO.getAgentID()) {
        List<AgentCommission> agentcommissionList =
            convertGroupFarmerToAgentCommission(groupFarmerProposal);
        CommonCreateAndUpateMarks recorder = new CommonCreateAndUpateMarks();
        recorder.setCreatedDate(new Date());
        agentcommissionList.forEach(agent -> {
          agent.setRecorder(recorder);
        });
        agentCommissionRepo.saveAll(agentcommissionList);
      }

      List<TLF> tlfList = convertGroupFarmerToTLF(groupFarmerProposal);
      tlfRepository.saveAll(tlfList);
      return policyList;

    } catch (Exception e) {
      logger.error("JOEERROR:" + e.getMessage(), e);
      throw e;
    }

  }

  private GroupFarmerProposal createGroupFarmerProposal(FarmerProposalDTO groupFarmerProposalDTO) {
      Optional<Branch> branchOptional = branchService.findById(groupFarmerProposalDTO.getBranchId());
      Optional<PaymentType> paymentTypeOptional = paymentTypeService.findById(groupFarmerProposalDTO.getPaymentTypeId());
      Optional<Agent> agentOptional = agentService.findById(groupFarmerProposalDTO.getAgentID());
      Optional<SaleMan> saleManOptional = saleManService.findById(groupFarmerProposalDTO.getSaleManId());
      Optional<Customer> referralOptional =customerService.findById(groupFarmerProposalDTO.getReferralID());
      Optional<Organization> organizationOptional =organizationService.findById(groupFarmerProposalDTO.getOrganizationID());
      Optional<SalePoint> salePointOptional = salePointService.findById(groupFarmerProposalDTO.getSalePointId());
      GroupFarmerProposal groupFarmerProposal = new GroupFarmerProposal();
     try {
      groupFarmerProposal.setSubmittedDate(groupFarmerProposalDTO.getSubmittedDate());
      groupFarmerProposal.setProposalType(ProposalType.UNDERWRITING);
      groupFarmerProposal.setEndDate(groupFarmerProposalDTO.getEndDate());
      groupFarmerProposal.setNoOfInsuredPerson(groupFarmerProposalDTO.getNoOfInsuredPerson());
      groupFarmerProposal.setTotalSI(groupFarmerProposalDTO.getTotalSI());
      groupFarmerProposal.setPaymentComplete(true);
      groupFarmerProposal.setProcessComplete(true);
      double totalPremium = (groupFarmerProposal.getTotalSI() / 100) * 1;
      groupFarmerProposal.setPremium(totalPremium);
      if (branchOptional.isPresent()) {
        groupFarmerProposal.setBranch(branchOptional.get());
      }

      if (agentOptional.isPresent()) {
        groupFarmerProposal.setAgent(agentOptional.get());
      }
      groupFarmerProposal.setPaymentType(paymentTypeOptional.get());

      if (saleManOptional.isPresent()) {
        groupFarmerProposal.setSaleMan(saleManOptional.get());
      }
      if (referralOptional.isPresent()) {
        groupFarmerProposal.setReferral(referralOptional.get());
      }
      if (organizationOptional.isPresent()) {
        groupFarmerProposal.setOrganization(organizationOptional.get());
      }
      if (salePointOptional.isPresent()) {
        groupFarmerProposal.setSalePoint(salePointOptional.get());
      }

      CommonCreateAndUpateMarks recorder = new CommonCreateAndUpateMarks();
      recorder.setCreatedDate(new Date());
      groupFarmerProposal.setCommonCreateAndUpateMarks(recorder);
      String proposalNo = customIdRepo.getNextId("GROUPFARMER_LIFE_PROPOSAL_NO", null);
      groupFarmerProposal.setProposalNo(proposalNo);
    
    }catch(DAOException e) {
    	throw new SystemException(e.getErrorCode(), e.getMessage());
    } catch(Exception e) {
         logger.error("JOEERROR:" + e.getMessage());
     }
     return groupFarmerProposal;
  }

  private List<LifeProposal> convertGroupFarmerProposalDTOToProposal(FarmerProposalDTO groupFarmerProposalDTO) {

    Optional<Branch> branchOptional = branchService.findById(groupFarmerProposalDTO.getBranchId());
    Optional<Customer> referralOptional = customerService.findById(groupFarmerProposalDTO.getReferralID());
    Optional<Organization> organizationOptional =organizationService.findById(groupFarmerProposalDTO.getOrganizationID());
    Optional<PaymentType> paymentTypeOptional =paymentTypeService.findById(groupFarmerProposalDTO.getPaymentTypeId());
    Optional<Agent> agentOptional = agentService.findById(groupFarmerProposalDTO.getAgentID());
    Optional<SaleMan> saleManOptional = saleManService.findById(groupFarmerProposalDTO.getSaleManId());
    Optional<SalePoint> salePointOptional =salePointService.findById(groupFarmerProposalDTO.getSalePointId());
    List<LifeProposal> lifeProposalList = new ArrayList<>();
    try {
      groupFarmerProposalDTO.getProposalInsuredPersonList().forEach(insuredPerson -> {
        LifeProposal lifeProposal = new LifeProposal();
        if (groupFarmerProposalDTO.getPaymentChannel().equalsIgnoreCase("TRF")) {
          lifeProposal.setPaymentChannel(PaymentChannel.TRANSFER);
          lifeProposal.setToBank(groupFarmerProposalDTO.getToBank());
          lifeProposal.setFromBank(groupFarmerProposalDTO.getFromBank());
        } else if (groupFarmerProposalDTO.getPaymentChannel().equalsIgnoreCase("CSH")) {
          lifeProposal.setPaymentChannel(PaymentChannel.CASHED);
        } else if (groupFarmerProposalDTO.getPaymentChannel().equalsIgnoreCase("CHQ")) {
          lifeProposal.setPaymentChannel(PaymentChannel.CHEQUE);
          lifeProposal.setChequeNo(groupFarmerProposalDTO.getChequeNo());
          lifeProposal.setToBank(groupFarmerProposalDTO.getToBank());
          lifeProposal.setFromBank(groupFarmerProposalDTO.getFromBank());
        } else if (groupFarmerProposalDTO.getPaymentChannel().equalsIgnoreCase("RCV")) {
          lifeProposal.setPaymentChannel(PaymentChannel.SUNDRY);
          lifeProposal.setToBank(groupFarmerProposalDTO.getToBank());
          lifeProposal.setFromBank(groupFarmerProposalDTO.getFromBank());
        }
        lifeProposal.setComplete(true);

        CommonCreateAndUpateMarks recorder = new CommonCreateAndUpateMarks();
        recorder.setCreatedDate(new Date());
        lifeProposal.setRecorder(recorder);

        lifeProposal.setProposalType(ProposalType.UNDERWRITING);
        lifeProposal.setSubmittedDate(groupFarmerProposalDTO.getSubmittedDate());
        lifeProposal.setBranch(branchOptional.get());
        if (referralOptional.isPresent()) {
          lifeProposal.setReferral(referralOptional.get());
        }
        if (organizationOptional.isPresent()) {
          lifeProposal.setOrganization(organizationOptional.get());
        }
        if (paymentTypeOptional.isPresent()) {
          lifeProposal.setPaymentType(paymentTypeOptional.get());
        }

        if (agentOptional.isPresent()) {

          lifeProposal.setAgent(agentOptional.get());
        }

        if (saleManOptional.isPresent()) {
          lifeProposal.setSaleMan(saleManOptional.get());
        }
        lifeProposal.setSalePoint(salePointOptional.get());
        lifeProposal.getProposalInsuredPersonList().add(createInsuredPerson(insuredPerson));
        String proposalNo = customIdRepo.getNextId("FARMER_LIFE_PROPOSAL_NO", null);
        lifeProposal.setProposalNo(proposalNo);
        lifeProposal.setPrefix("ISLIF001");
        lifeProposalList.add(lifeProposal);

      });

    } catch (DAOException e) {
    	 throw new SystemException(e.getErrorCode(), e.getMessage());
    }


    return lifeProposalList;
  }

  private ProposalInsuredPerson createInsuredPerson(GroupFarmerProposalInsuredPersonDTO dto) {
  try {
    Optional<Product> productOptional = productService.findById(productId);
    Optional<Township> townshipOptional = townShipService.findById(dto.getTownshipId());
    Optional<Occupation> occupationOptional = occupationService.findById(dto.getOccupationID());
    Optional<Customer> customerOptional = customerService.findById(dto.getCustomerID());

    ResidentAddress residentAddress = new ResidentAddress();
    residentAddress.setResidentAddress(dto.getResidentAddress());
    residentAddress.setResidentTownship(townshipOptional.get());

    Name name = new Name();
    name.setFirstName(dto.getFirstName());
    name.setMiddleName(dto.getMiddleName());
    name.setLastName(dto.getLastName());

    ProposalInsuredPerson insuredPerson = new ProposalInsuredPerson();

    insuredPerson.setProduct(productOptional.get());
    insuredPerson.setInitialId(dto.getInitialId());
    insuredPerson.setBpmsInsuredPersonId(dto.getBpmsInsuredPersonId());
    insuredPerson.setProposedSumInsured(dto.getProposedSumInsured());
    insuredPerson.setProposedPremium(dto.getProposedPremium());
    insuredPerson.setApprovedSumInsured(dto.getApprovedSumInsured());
    insuredPerson.setApprovedPremium(dto.getApprovedPremium());
    insuredPerson.setBasicTermPremium(dto.getBasicTermPremium());
    insuredPerson.setIdType(IdType.valueOf(dto.getIdType()));
    insuredPerson.setIdNo(dto.getIdNo());
    insuredPerson.setFatherName(dto.getFatherName());
    insuredPerson.setStartDate(dto.getStartDate());
    insuredPerson.setEndDate(dto.getEndDate());
    insuredPerson.setDateOfBirth(dto.getDateOfBirth());
    insuredPerson.setAge(DateUtils.getAgeForNextYear(dto.getDateOfBirth()));
    insuredPerson.setPeriodMonth(12);
    insuredPerson.setGender(Gender.valueOf(dto.getGender()));
    insuredPerson.setResidentAddress(residentAddress);
    insuredPerson.setName(name);
    CommonCreateAndUpateMarks recorder = new CommonCreateAndUpateMarks();
    recorder.setCreatedDate(new Date());
    insuredPerson.setRecorder(recorder);
    if (occupationOptional.isPresent()) {
      insuredPerson.setOccupation(occupationOptional.get());
    }
    if (customerOptional.isPresent()) {
      insuredPerson.setCustomer(customerOptional.get());
    } else {
      insuredPerson.setCustomer(createNewCustomer(insuredPerson));
      insuredPerson.setNewCustomer(true);
    }


    String insPersonCodeNo = customIdRepo.getNextId("LIFE_INSUREDPERSON_CODENO_ID_GEN", null);
    insuredPerson.setInsPersonCodeNo(insPersonCodeNo);
    insuredPerson.setPrefix("ISLIF008");
    dto.getInsuredPersonBeneficiariesList().forEach(beneficiary -> {
      insuredPerson.getInsuredPersonBeneficiariesList()
          .add(createInsuredPersonBeneficiareis(beneficiary));
    });
    return insuredPerson;
  } catch(DAOException e) {
	  throw new SystemException(e.getErrorCode(), e.getMessage());
  }
  }

  private Customer createNewCustomer(ProposalInsuredPerson dto) {
	Customer customer = new Customer();
	try {
    customer.setInitialId(dto.getInitialId());
    customer.setFatherName(dto.getFatherName());
    customer.setIdNo(dto.getIdNo());
    customer.setDateOfBirth(dto.getDateOfBirth());
    customer.setGender(dto.getGender());
    customer.setIdType(dto.getIdType());
    customer.setResidentAddress(dto.getResidentAddress());
    customer.setName(dto.getName());
    customer.setOccupation(dto.getOccupation());
    customer.setRecorder(dto.getRecorder());
    customer = customerRepo.save(customer);
	}catch(Exception e) {
		e.printStackTrace();
	}
	 return customer;
  }

  private InsuredPersonBeneficiaries createInsuredPersonBeneficiareis( GroupFarmerProposalInsuredPersonBeneficiariesDTO dto) {
  try {
    Optional<Township> townshipOptional = townShipService.findById(dto.getTownshipId());
    Optional<RelationShip> relationshipOptional = relationshipService.findById(dto.getRelationshipID());
    ResidentAddress residentAddress = new ResidentAddress();
    residentAddress.setResidentAddress(dto.getResidentAddress());

    residentAddress.setResidentTownship(townshipOptional.get());

    Name name = new Name();
    name.setFirstName(dto.getFirstName());
    name.setMiddleName(dto.getMiddleName());
    name.setLastName(dto.getLastName());

    InsuredPersonBeneficiaries beneficiary = new InsuredPersonBeneficiaries();
    beneficiary.setInitialId(dto.getInitialId());
    beneficiary.setDateOfBirth(dto.getDob());
    beneficiary.setPercentage(dto.getPercentage());
    beneficiary.setIdType(IdType.valueOf(dto.getIdType()));
    beneficiary.setIdNo(dto.getIdNo());
    beneficiary.setGender(Gender.valueOf(dto.getGender()));
    beneficiary.setResidentAddress(residentAddress);
    beneficiary.setName(name);
    if (relationshipOptional.isPresent()) {
      beneficiary.setRelationship(relationshipOptional.get());
    }
    String beneficiaryNo = customIdRepo.getNextId("LIFE_BENEFICIARY_ID_GEN", null);
    beneficiary.setBeneficiaryNo(beneficiaryNo);
    beneficiary.setPrefix("ISLIF004");
    CommonCreateAndUpateMarks recorder = new CommonCreateAndUpateMarks();
    recorder.setCreatedDate(new Date());
    beneficiary.setRecorder(recorder);
    return beneficiary;
  }catch(DAOException e) {
	  throw new SystemException(e.getErrorCode(), e.getMessage());
   }
  }

  private List<LifePolicy> convertGroupFarmerProposalToPolicy( List<LifeProposal> farmerProposalList) {
    List<LifePolicy> policyList = new ArrayList<>();
    farmerProposalList.forEach(proposal -> {
      LifePolicy policy = new LifePolicy(proposal);
      String policyNo = customIdRepo.getNextId("FARMER_LIFE_POLICY_NO", null);
      policy.setPolicyNo(policyNo);
      policy.setFromBank(proposal.getFromBank());
      policy.setToBank(proposal.getToBank());
      policy.setChequeNo(proposal.getChequeNo());
      policy.setPaymentChannel(proposal.getPaymentChannel());
      policy.setActivedPolicyStartDate(policy.getPolicyInsuredPersonList().get(0).getStartDate());
      policy.setActivedPolicyEndDate(policy.getPolicyInsuredPersonList().get(0).getEndDate());
      policy.setCommenmanceDate(proposal.getSubmittedDate());
      policy.setLastPaymentTerm(1);
      CommonCreateAndUpateMarks recorder = new CommonCreateAndUpateMarks();
      recorder.setCreatedDate(new Date());
      policy.setRecorder(recorder);
      for (PolicyInsuredPerson insuredPerson : policy.getPolicyInsuredPersonList()) {
        insuredPerson.setRecorder(recorder);
        for (PolicyInsuredPersonBeneficiaries beneficiary : insuredPerson
            .getPolicyInsuredPersonBeneficiariesList()) {
          beneficiary.setRecorder(recorder);
        }
      }
      policyList.add(policy);
    });
    return policyList;
  }

  private List<Payment> convertGroupFarmerToPayment(GroupFarmerProposal groupFarmerProposal,
      FarmerProposalDTO farmerProposalDTO) {
    List<Payment> paymentList = new ArrayList<Payment>();
    try {
      Optional<Bank> fromBankOptional = Optional.empty();
      Optional<Bank> toBankOptional = Optional.empty();
      if (farmerProposalDTO.getFromBank() != null) {
        fromBankOptional = bankService.findById(farmerProposalDTO.getFromBank());
      }
      if (farmerProposalDTO.getToBank() != null) {
        toBankOptional = bankService.findById(farmerProposalDTO.getToBank());
      }
      Payment payment = new Payment();
      double rate = 1.0;
      String receiptNo = "";
      CommonCreateAndUpateMarks recorder = new CommonCreateAndUpateMarks();
      recorder.setCreatedDate(new Date());
      if (farmerProposalDTO.getPaymentChannel().equalsIgnoreCase("CSH")) {
        receiptNo = customIdRepo.getNextId("CASH_RECEIPT_ID_GEN", null);
        payment.setPaymentChannel(PaymentChannel.CASHED);
      } else if (farmerProposalDTO.getPaymentChannel().equalsIgnoreCase("CHQ")) {
        payment.setPO(true);
        payment.setPaymentChannel(PaymentChannel.CHEQUE);
        receiptNo = customIdRepo.getNextId("CHEQUE_RECEIPT_ID_GEN", null);
        payment.setChequeNo(farmerProposalDTO.getChequeNo());
      } else if (farmerProposalDTO.getPaymentChannel().equalsIgnoreCase("TRF")) {
        payment.setPoNo(farmerProposalDTO.getChequeNo());
        payment.setPaymentChannel(PaymentChannel.TRANSFER);
        receiptNo = customIdRepo.getNextId("TRANSFER_RECEIPT_ID_GEN", null);
      } else if (farmerProposalDTO.getPaymentChannel().equalsIgnoreCase("RCV")) {
        payment.setPaymentChannel(PaymentChannel.SUNDRY);
        receiptNo = customIdRepo.getNextId("CHEQUE_RECEIPT_ID_GEN", null);
        payment.setPO(true);
      }
      payment.setReceiptNo(receiptNo);
      payment.setPaymentType(groupFarmerProposal.getPaymentType());
      payment.setReferenceType(PolicyReferenceType.GROUP_FARMER_PROPOSAL);
      payment.setConfirmDate(new Date());
      payment.setPaymentDate(new Date());
      if (toBankOptional.isPresent()) {
        payment.setAccountBank(toBankOptional.get());
      }
      if (fromBankOptional.isPresent()) {
        payment.setBank(fromBankOptional.get());
      }
      payment.setReferenceNo(groupFarmerProposal.getId());
      payment.setBasicPremium(groupFarmerProposal.getPremium());
      payment.setFromTerm(1);
      payment.setToTerm(1);
      payment.setCur("KYT");
      payment.setRate(rate);
      payment.setComplete(true);
      payment.setAmount(payment.getNetPremium());
      payment.setHomeAmount(payment.getNetPremium());
      payment.setHomePremium(payment.getBasicPremium());
      payment.setHomeAddOnPremium(payment.getAddOnPremium());
      payment.setCommonCreateAndUpateMarks(recorder);
      paymentList.add(payment);
    } catch (DAOException e) {
    	throw new SystemException(e.getErrorCode(),e.getMessage());
    }
    return paymentList;
  }

  private List<AgentCommission> convertGroupFarmerToAgentCommission(
      GroupFarmerProposal groupFarmerProposal) {
    List<AgentCommission> agentCommissionList = new ArrayList<AgentCommission>();
    try {
      /* get agent commission of each policy */
      double commissionPercent = 10.0;
      Payment payment = paymentRepository.findByPaymentReferenceNo(groupFarmerProposal.getId());
      double rate = payment.getRate();
      double firstAgentCommission = groupFarmerProposal.getAgentCommission();
      agentCommissionList.add(new AgentCommission(groupFarmerProposal.getId(),
          PolicyReferenceType.GROUP_FARMER_PROPOSAL, groupFarmerProposal.getAgent(),
          firstAgentCommission, new Date(), payment.getReceiptNo(),
          groupFarmerProposal.getPremium(), commissionPercent,
          AgentCommissionEntryType.UNDERWRITING, rate, (rate * firstAgentCommission), "KYT",
          (rate * groupFarmerProposal.getPremium())));
    } catch (Exception e) {
      e.printStackTrace();
    }

    return agentCommissionList;
  }

  private List<TLF> convertStudentLifePolicyToTLF(List<LifePolicy> studentLifePolicyList) {
    List<TLF> TLFList = new ArrayList<TLF>();
    try {
    String accountCode = "STUDENT_LIFE_PREMIUM_INCOME";
    for (LifePolicy lifePolicy : studentLifePolicyList) {
      Payment payment = paymentRepository.findByPaymentReferenceNo(lifePolicy.getId());
      TLF tlf1 = addNewTLF_For_CashDebitForPremium(payment,
          lifePolicy.getCustomer() == null ? lifePolicy.getOrganization().getId()
              : lifePolicy.getCustomer().getId(),
          lifePolicy.getBranch(), payment.getReceiptNo(), false, "KYT", lifePolicy.getSalePoint(),
          lifePolicy.getPolicyNo());
      TLFList.add(tlf1);
      TLF tlf2 = addNewTLF_For_PremiumCredit(payment,
          lifePolicy.getCustomer() == null ? lifePolicy.getOrganization().getId()
              : lifePolicy.getCustomer().getId(),
          lifePolicy.getBranch(), accountCode, payment.getReceiptNo(), false, "KYT",
          lifePolicy.getSalePoint(), lifePolicy.getPolicyNo());
      TLFList.add(tlf2);

      if (lifePolicy.getPaymentChannel().equals(PaymentChannel.CHEQUE)
          || lifePolicy.getPaymentChannel().equals(PaymentChannel.SUNDRY)) {
        String customerId = lifePolicy.getCustomer() == null ? lifePolicy.getOrganization().getId()
            : lifePolicy.getCustomer().getId();
        TLF tlf3 =
            addNewTLF_For_PremiumDebitForRCVAndCHQ(payment, customerId, lifePolicy.getBranch(),
                payment.getAccountBank().getAcode(), false, payment.getReceiptNo(), true, false,
                "KYT", lifePolicy.getSalePoint(), lifePolicy.getPolicyNo());
        TLFList.add(tlf3);
        TLF tlf4 = addNewTLF_For_CashCreditForPremiumForRCVAndCHQ(payment, customerId,
            lifePolicy.getBranch(), false, payment.getReceiptNo(), true, false, "KYT",
            lifePolicy.getSalePoint(), lifePolicy.getPolicyNo());
        TLFList.add(tlf4);
      }
      if (lifePolicy.getAgent() != null) {
          double firstAgentCommission = lifePolicy.getAgentCommission();
          AgentCommission ac =
              new AgentCommission(lifePolicy.getId(), PolicyReferenceType.STUDENT_LIFE_POLICY,
            		  lifePolicy.getAgent(), firstAgentCommission, new Date());
          TLF tlf5 = addNewTLF_For_AgentCommissionDr(ac, false, lifePolicy.getBranch(), payment,
              payment.getId(), false, "KYT", lifePolicy.getSalePoint(), lifePolicy.getPolicyNo());
          TLFList.add(tlf5);
          TLF tlf6 = addNewTLF_For_AgentCommissionCredit(ac, false, lifePolicy.getBranch(), payment,
              payment.getId(), false, "KYT", lifePolicy.getSalePoint(), lifePolicy.getPolicyNo());
          TLFList.add(tlf6);
        }
      }
    }catch(Exception e) {
    	e.printStackTrace();
    }
    return TLFList;
  }

  private List<TLF> convertGroupFarmerToTLF(GroupFarmerProposal proposal) {
    List<TLF> TLFList = new ArrayList<TLF>();
    try {
      String accountCode = "Farmer_Premium";
      Payment payment = paymentRepository.findByPaymentReferenceNo(proposal.getId());

      TLF tlf1 = addNewTLF_For_CashDebitForPremium(payment, proposal.getOrganization().getId(),
          proposal.getBranch(), payment.getReceiptNo(), false, "KYT", proposal.getSalePoint(),
          proposal.getProposalNo());
      TLFList.add(tlf1);
      TLF tlf2 = addNewTLF_For_PremiumCredit(payment, proposal.getOrganization().getId(),
          proposal.getBranch(), accountCode, payment.getReceiptNo(), false, "KYT",
          proposal.getSalePoint(), proposal.getProposalNo());
      TLFList.add(tlf2);

      if (payment.getPaymentChannel().equals(PaymentChannel.CHEQUE)
          || payment.getPaymentChannel().equals(PaymentChannel.SUNDRY)) {
        String customerId = proposal.getOrganization().getId();
        TLF tlf3 = addNewTLF_For_PremiumDebitForRCVAndCHQ(payment, customerId, proposal.getBranch(),
            payment.getAccountBank().getAcode(), false, payment.getReceiptNo(), true, false, "KYT",
            proposal.getSalePoint(), proposal.getProposalNo());
        TLFList.add(tlf3);
        TLF tlf4 = addNewTLF_For_CashCreditForPremiumForRCVAndCHQ(payment, customerId,
            proposal.getBranch(), false, payment.getReceiptNo(), true, false, "KYT",
            proposal.getSalePoint(), proposal.getProposalNo());
        TLFList.add(tlf4);
      }

      if (proposal.getAgent() != null) {
        double firstAgentCommission = proposal.getAgentCommission();
        AgentCommission ac =
            new AgentCommission(proposal.getId(), PolicyReferenceType.GROUP_FARMER_PROPOSAL,
                proposal.getAgent(), firstAgentCommission, new Date());
        TLF tlf5 = addNewTLF_For_AgentCommissionDr(ac, false, proposal.getBranch(), payment,
            payment.getId(), false, "KYT", proposal.getSalePoint(), proposal.getProposalNo());
        TLFList.add(tlf5);
        TLF tlf6 = addNewTLF_For_AgentCommissionCredit(ac, false, proposal.getBranch(), payment,
            payment.getId(), false, "KYT", proposal.getSalePoint(), proposal.getProposalNo());
        TLFList.add(tlf6);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return TLFList;
  }


  public TLF addNewTLF_For_CashDebitForPremium(Payment payment, String customerId, Branch branch,
      String tlfNo, boolean isRenewal, String currencyCode, SalePoint salePoint, String policyNo) {
    TLF tlf = null;
    try {
      CommonCreateAndUpateMarks recorder = new CommonCreateAndUpateMarks();
      recorder.setCreatedDate(new Date());
      double totalNetPremium = 0;
      double homeAmount = 0;
      String coaCode = null;
      totalNetPremium = payment.getNetPremium();
      homeAmount = totalNetPremium;
      if (PaymentChannel.CASHED.equals(payment.getPaymentChannel())) {
        coaCode = paymentRepository.findCheckOfAccountNameByCode(COACode.CASH,
            branch.getBranchCode(), currencyCode);
      } else if (PaymentChannel.TRANSFER.equals(payment.getPaymentChannel())) {
        coaCode = payment.getAccountBank() == null
            ? paymentRepository.findCheckOfAccountNameByCode(COACode.CHEQUE, branch.getBranchCode(),
                currencyCode)
            : payment.getAccountBank().getAcode();
      } else if (PaymentChannel.CHEQUE.equals(payment.getPaymentChannel())) {
        String coaCodeType = "";
        switch (payment.getReferenceType()) {
          case FARMER_POLICY:
          case GROUP_FARMER_PROPOSAL:
            coaCodeType = COACode.FARMER_PAYMENT_ORDER;
            break;
          case STUDENT_LIFE_POLICY:
        	  coaCodeType=COACode.STUDENT_LIFE_PAYMENT_ORDER;
        	  break;
          default:
            break;
        }
        coaCode = paymentRepository.findCheckOfAccountNameByCode(coaCodeType,
            branch.getBranchCode(), currencyCode);
      } else if (PaymentChannel.SUNDRY.equals(payment.getPaymentChannel())) {
        String coaCodeType = "";
        switch (payment.getReferenceType()) {
          case FARMER_POLICY:
          case GROUP_FARMER_PROPOSAL:
            coaCodeType = COACode.FARMER_SUNDRY;
            break;
          case STUDENT_LIFE_POLICY:
            coaCodeType=COACode.STUDENT_LIFE_SUNDRY;
            break;
          default:
            break;
        }
        coaCode = paymentRepository.findCheckOfAccountNameByCode(coaCodeType,
            branch.getBranchCode(), currencyCode);
      }
      TLFBuilder tlfBuilder =
          new TLFBuilder(DoubleEntry.DEBIT, homeAmount, customerId, branch.getBranchCode(), coaCode,
              tlfNo, getNarrationPremium(payment, isRenewal), payment, isRenewal);
      tlf = tlfBuilder.getTLFInstance();
      tlf.setPolicyNo(policyNo);
      tlf.setSalePoint(salePoint);
      tlf.setCommonCreateAndUpateMarks(recorder);
      tlf.setPaid(true);
      // setIDPrefixForInsert(tlf);
      tlf.setPaymentChannel(payment.getPaymentChannel());
      // paymentDAO.insertTLF(tlf);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return tlf;
  }

  private String getNarrationPremium(Payment payment, boolean isRenewal) {
    StringBuffer nrBf = new StringBuffer();
    String customerName = "";
    String premiumString = "";
    int totalInsuredPerson = 0;
    double si = 0.0;
    double premium = 0.0;

    nrBf.append("Being amount of ");
    switch (payment.getReferenceType()) {
      case FARMER_POLICY:
      case GROUP_FARMER_PROPOSAL:
      case LIFE_POLICY:
        nrBf.append(" Life Premium ");
        // LifePolicy lifePolicy = lifePolicyRepo.getOne(payment.getReferenceNo());
        GroupFarmerProposal groupFarmerProposal =
            groupFarmerRepository.getOne(payment.getReferenceNo());
        si = groupFarmerProposal.getTotalSI();
        premium = groupFarmerProposal.getPremium();
        customerName = groupFarmerProposal.getOrganization().getName();
        totalInsuredPerson = groupFarmerProposal.getNoOfInsuredPerson();
        break;
      case STUDENT_LIFE_POLICY:
    	  nrBf.append("Student Life Premium ");
    	 LifePolicy lifePolicy = lifePolicyRepo.getOne(payment.getReferenceNo());
    	 si=lifePolicy.getSumInsured();
    	 premium=lifePolicy.getTotalPremium();
    	 customerName=lifePolicy.getCustomerName();
    	 break;
      default:
        break;
    }
    nrBf.append(premiumString);
    nrBf.append(" received by ");
    nrBf.append(payment.getReceiptNo());
    nrBf.append(" from ");
    nrBf.append(customerName);
    nrBf.append(" for Sum Insured ");
    nrBf.append(Utils.getCurrencyFormatString(si));
    if(PolicyReferenceType.GROUP_FARMER_PROPOSAL.equals(payment.getReferenceType())) {
    	nrBf.append(" and for total number of insured person ");
    	nrBf.append(Integer.toString(totalInsuredPerson));
    }
   
    nrBf.append(" and the premium amount of ");
    nrBf.append(Utils.getCurrencyFormatString(premium));

    nrBf.append(". ");

    return nrBf.toString();
  }

  public TLF addNewTLF_For_PremiumCredit(Payment payment, String customerId, Branch branch,
      String accountName, String tlfNo, boolean isRenewal, String currenyCode, SalePoint salePoint,
      String policyNo) {
    TLF tlf = null;

    try {
      CommonCreateAndUpateMarks recorder = new CommonCreateAndUpateMarks();
      recorder.setCreatedDate(new Date());
      double homeAmount = payment.getNetPremium();
      if (isRenewal) {
        homeAmount = payment.getRenewalNetPremium();
      }

      String coaCode = paymentRepository.findCheckOfAccountNameByCode(accountName,
          branch.getBranchCode(), currenyCode);
      TLFBuilder tlfBuilder =
          new TLFBuilder(DoubleEntry.CREDIT, homeAmount, customerId, branch.getBranchCode(),
              coaCode, tlfNo, getNarrationPremium(payment, isRenewal), payment, isRenewal);
      tlf = tlfBuilder.getTLFInstance();
      tlf.setPaymentChannel(payment.getPaymentChannel());
      tlf.setSalePoint(salePoint);
      tlf.setPolicyNo(policyNo);
      tlf.setCommonCreateAndUpateMarks(recorder);
      tlf.setPaid(true);
      // setIDPrefixForInsert(tlf);
      /// paymentDAO.insertTLF(tlf);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return tlf;
  }

  public TLF addNewTLF_For_AgentCommissionDr(AgentCommission ac, boolean coInsureance,
      Branch branch, Payment payment, String eno, boolean isRenewal, String currencyCode,
      SalePoint salePoint, String policyNo) {
    TLF tlf = new TLF();
    try {
      CommonCreateAndUpateMarks recorder = new CommonCreateAndUpateMarks();
      recorder.setCreatedDate(new Date());
      String receiptNo = payment.getReceiptNo();
      String coaCode = null;
      String coaCodeMI = null;
      String accountName = null;
      double ownCommission = 0.0;
     
      switch (ac.getReferenceType()) {
        case GROUP_FARMER_PROPOSAL:
        case FARMER_POLICY:
          coaCode = COACode.FARMER_AGENT_COMMISSION;
          break;
        case STUDENT_LIFE_POLICY:
        	coaCode=COACode.STUDENT_LIFE_AGENT_COMMISSION;
        	break;
        default:
          break;
      }
      String cur = payment.getCur();
      double rate = payment.getRate();
      String narration = getNarrationAgent(payment, ac, isRenewal);

      accountName = paymentRepository.findCheckOfAccountNameByCode(coaCode, branch.getBranchCode(),
          currencyCode);
      ownCommission = Utils.getTwoDecimalPoint(ac.getCommission());

      TLFBuilder tlfBuilder = new TLFBuilder(TranCode.TRDEBIT, Status.TDV, ownCommission,
          ac.getAgent().getId(), branch.getBranchCode(), accountName, receiptNo, narration, eno,
          ac.getReferenceNo(), ac.getReferenceType(), isRenewal, cur, rate);
      tlf = tlfBuilder.getTLFInstance();
      tlf.setPaymentChannel(payment.getPaymentChannel());
      tlf.setSalePoint(salePoint);
      tlf.setPolicyNo(policyNo);
      tlf.setAgentTransaction(true);
      tlf.setPaid(true);
      tlf.setCommonCreateAndUpateMarks(recorder);
      // setIDPrefixForInsert(tlf);
      // paymentDAO.insertTLF(tlf);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return tlf;
  }

  public TLF addNewTLF_For_CashCreditForPremiumForRCVAndCHQ(Payment payment, String customerId,
      Branch branch, boolean isEndorse, String tlfNo, boolean isClearing, boolean isRenewal,
      String currencyCode, SalePoint salePoint, String policyNo) {
    TLF tlf = new TLF();
    try {

      double totalNetPremium = 0;
      double homeAmount = 0;
      String narration = null;
      String coaCode = null;
      String enoNo = payment.getReceiptNo();
      CommonCreateAndUpateMarks recorder = new CommonCreateAndUpateMarks();
      recorder.setCreatedDate(new Date());
      if (isRenewal) {
        totalNetPremium = totalNetPremium + payment.getRenewalNetPremium();
      } else {
        totalNetPremium = totalNetPremium + payment.getNetPremium();
      }

      totalNetPremium = totalNetPremium * (isEndorse ? -1 : 1);

      // TLF Home Amount
      homeAmount = totalNetPremium;

      // TLF COAID
      switch (payment.getPaymentChannel()) {
        case TRANSFER: {
          if (payment.getAccountBank() == null) {
            coaCode = paymentRepository.findCheckOfAccountNameByCode(COACode.CHEQUE,
                branch.getBranchCode(), currencyCode);
          } else {
            coaCode = payment.getAccountBank().getAcode();
          }
        }
          break;
        case CASHED:
          coaCode = paymentRepository.findCheckOfAccountNameByCode(COACode.CASH,
              branch.getBranchCode(), currencyCode);
          break;
        case CHEQUE: {
          String coaCodeType = "";
          switch (payment.getReferenceType()) {
            case GROUP_FARMER_PROPOSAL:
            case FARMER_POLICY:
              coaCodeType = COACode.FARMER_PAYMENT_ORDER;
              break;
            case STUDENT_LIFE_POLICY:
            	coaCodeType=COACode.STUDENT_LIFE_PAYMENT_ORDER;
            	break;
            default:
              break;
          }
          coaCode = paymentRepository.findCheckOfAccountNameByCode(coaCodeType,
              branch.getBranchCode(), currencyCode);
        }
          break;
        case SUNDRY: {
          String coaCodeType = "";
          switch (payment.getReferenceType()) {
            case GROUP_FARMER_PROPOSAL:
            case FARMER_POLICY:
              coaCodeType = COACode.FARMER_SUNDRY;
              break;
            case STUDENT_LIFE_POLICY:
            	coaCodeType=COACode.STUDENT_LIFE_SUNDRY;
            default:
              break;
          }
          coaCode = paymentRepository.findCheckOfAccountNameByCode(coaCodeType,
              branch.getBranchCode(), currencyCode);
        }
          break;
      }
      // TLF Narration
      narration = "Cash refund for " + enoNo;
      TLFBuilder tlfBuilder = new TLFBuilder(DoubleEntry.CREDIT, homeAmount, customerId,
          branch.getBranchCode(), coaCode, tlfNo, narration, payment, isRenewal);
      tlf = tlfBuilder.getTLFInstance();
      tlf.setClearing(isClearing);
      tlf.setPaymentChannel(payment.getPaymentChannel());
      tlf.setSalePoint(salePoint);
      tlf.setCommonCreateAndUpateMarks(recorder);
      // tlf.setPolicyNo(policyNo);
      // setIDPrefixForInsert(tlf);
      /// paymentDAO.insertTLF(tlf);

    } catch (Exception e) {
      e.printStackTrace();
    }
    return tlf;
  }

  public TLF addNewTLF_For_PremiumDebitForRCVAndCHQ(Payment payment, String customerId,
      Branch branch, String accountName, boolean isEndorse, String tlfNo, boolean isClearing,
      boolean isRenewal, String currencyCode, SalePoint salePoint, String policyNo) {
    TLF tlf = new TLF();
    try {
      double netPremium = 0.0;
      CommonCreateAndUpateMarks recorder = new CommonCreateAndUpateMarks();
      recorder.setCreatedDate(new Date());
      if (isRenewal) {
        netPremium = payment.getRenewalNetPremium();
      } else {
        netPremium = payment.getNetPremium();
      }

      netPremium = netPremium * (isEndorse ? -1 : 1);
      double homeAmount = 0;
      // TLF Home Amount
      homeAmount = netPremium;

      // TLF COAID
      String coaCode = "";
      if (payment.getAccountBank() == null) {
        coaCode = paymentRepository.findCheckOfAccountNameByCode(accountName,
            branch.getBranchCode(), currencyCode);
      } else {
        coaCode = payment.getAccountBank().getAcode();
      }

      TLFBuilder tlfBuilder =
          new TLFBuilder(DoubleEntry.DEBIT, homeAmount, customerId, branch.getBranchCode(), coaCode,
              tlfNo, getNarrationPremium(payment, isRenewal), payment, isRenewal);
      tlf = tlfBuilder.getTLFInstance();
      tlf.setPaymentChannel(payment.getPaymentChannel());
      tlf.setSalePoint(salePoint);
      tlf.setClearing(isClearing);
      tlf.setCommonCreateAndUpateMarks(recorder);
      // tlf.setPolicyNo(policyNo);
      // setIDPrefixForInsert(tlf);
      // paymentDAO.insertTLF(tlf);

    } catch (Exception e) {
        e.printStackTrace();
    }
    return tlf;
  }

  private String getNarrationAgent(Payment payment, AgentCommission agentCommission,
      boolean isRenewal) {
    StringBuffer nrBf = new StringBuffer();
    double commission = 0.0;
    String agentName = "";
    String insuranceName = "";
    // Agent Commission payable for Fire Insurance(Product Name), Received
    // No to Agent Name for commission amount of Amount
    nrBf.append("Agent Commission payable for ");
    switch (payment.getReferenceType()) {
      case GROUP_FARMER_PROPOSAL:
      case FARMER_POLICY:
        insuranceName = "";
        break;
      case STUDENT_LIFE_POLICY:
    	insuranceName ="Student Life Insurance,";
    	break;
      default:
        break;
    }
    agentName = agentCommission.getAgent() == null ? "" : agentCommission.getAgent().getFullName();
    commission = agentCommission.getCommission();
    nrBf.append(insuranceName);
    nrBf.append(payment.getReceiptNo());
    nrBf.append(" to ");
    nrBf.append(agentName);
    nrBf.append(" for commission amount of ");
    nrBf.append(Utils.getCurrencyFormatString(commission));
    nrBf.append(".");

    return nrBf.toString();
  }

  public TLF addNewTLF_For_AgentCommissionCredit(AgentCommission ac, boolean coInsureance,
      Branch branch, Payment payment, String eno, boolean isRenewal, String currencyCode,
      SalePoint salePoint, String policyNo) {
    TLF tlf = new TLF();
    try {
      CommonCreateAndUpateMarks recorder = new CommonCreateAndUpateMarks();
      recorder.setCreatedDate(new Date());
      String receiptNo = payment.getReceiptNo();
      String coaCode = null;
      String accountName = null;
      double commission = 0.0;

      switch (ac.getReferenceType()) {
        case FARMER_POLICY:
        case GROUP_FARMER_PROPOSAL:
          coaCode = COACode.FARMER_AGENT_PAYABLE;
          break;
        case STUDENT_LIFE_POLICY:
        	coaCode=COACode.STUDENT_LIFE_AGENT_PAYABLE;
        	break;
        default:
          break;
      }

      accountName = paymentRepository.findCheckOfAccountNameByCode(coaCode, branch.getBranchCode(),
          currencyCode);
      commission = Utils.getTwoDecimalPoint(ac.getCommission());
      String narration = getNarrationAgent(payment, ac, isRenewal);
      String cur = payment.getCur();
      double rate = payment.getRate();
      TLFBuilder tlfBuilder = new TLFBuilder(TranCode.TRCREDIT, Status.TCV, commission,
          ac.getAgent().getId(), branch.getBranchCode(), accountName, receiptNo, narration, eno,
          ac.getReferenceNo(), ac.getReferenceType(), isRenewal, cur, rate);
      tlf = tlfBuilder.getTLFInstance();
      // setIDPrefixForInsert(tlf);
      tlf.setPaymentChannel(payment.getPaymentChannel());
      tlf.setSalePoint(salePoint);
      tlf.setPolicyNo(policyNo);
      tlf.setPaid(true);
      tlf.setAgentTransaction(true);
      tlf.setCommonCreateAndUpateMarks(recorder);

    } catch (Exception e) {
      e.printStackTrace();
    }
    return tlf;
  }


  /////////////////////////////////////////////////////////////////////////////////////
  // For Student Life

  @Transactional(propagation = Propagation.REQUIRED)
  public List<LifePolicy> createStudentLifeProposalToPolicy(
      StudentLifeProposalDTO studentLifeProposalDTO) {
	try {
    // convert groupFarmerProposalDTO to lifeproposal
    List<LifeProposal> studentLifeProposalList = convertStudentLifeProposalDTOToProposal(studentLifeProposalDTO);

    // convert lifeproposal to lifepolicy
    List<LifePolicy> policyList = convertStudentLifeProposalToPolicy(studentLifeProposalList);

    // create lifepolicy and return policynoList
    policyList = lifePolicyRepo.saveAll(policyList);

    // create lifepolicy to payment
    List<Payment> paymentList = convertStudentLifePolicyToPayment(policyList);
    paymentRepository.saveAll(paymentList);
    
    //create Agent Commission
    if (null != studentLifeProposalDTO.getAgentID()) {
        List<AgentCommission> agentcommissionList = convertStudentLifePolicyToAgentCommission(policyList);
        CommonCreateAndUpateMarks recorder = new CommonCreateAndUpateMarks();
        recorder.setCreatedDate(new Date());
        agentcommissionList.forEach(agent -> {
          agent.setRecorder(recorder);
        });
        agentCommissionRepo.saveAll(agentcommissionList);
    
    }
    
    // create TLF
    List<TLF> TLFList=convertStudentLifePolicyToTLF(policyList);
    tlfRepository.saveAll(TLFList);
    return policyList;
	}catch (Exception e) {
		logger.error("JOEERROR:" + e.getMessage(), e);
	     throw e;
	}
  }
  // Forstudentlife studentlifeDto to proposal
  public List<LifeProposal> convertStudentLifeProposalDTOToProposal(StudentLifeProposalDTO studentLifeProposalDTO) {
	List<LifeProposal> lifeProposalList = new ArrayList<>();
	try {
    Optional<Branch> branchOptional = branchService.findById(studentLifeProposalDTO.getBranchId());
    Optional<Customer> referralOptional = customerService.findById(studentLifeProposalDTO.getReferralID());
    Optional<Customer> customerOptional = customerService.findById(studentLifeProposalDTO.getCustomerID());
    Optional<PaymentType> paymentTypeOptional = paymentTypeService.findById(studentLifeProposalDTO.getPaymentTypeId());
    Optional<Agent> agentOptional = agentService.findById(studentLifeProposalDTO.getAgentID());
    Optional<SaleMan> saleManOptional = saleManService.findById(studentLifeProposalDTO.getSaleManId());
    Optional<SalePoint> salePointOptional = salePointService.findById(studentLifeProposalDTO.getSalePointId());
  
    studentLifeProposalDTO.getProposalInsuredPersonList().forEach(insuredPerson -> {
    	LifeProposal lifeProposal = new LifeProposal();
      if (studentLifeProposalDTO.getPaymentChannel().equalsIgnoreCase("TRF")) {
          lifeProposal.setPaymentChannel(PaymentChannel.TRANSFER);
          lifeProposal.setToBank(studentLifeProposalDTO.getToBank());
          lifeProposal.setFromBank(studentLifeProposalDTO.getFromBank());
          lifeProposal.setChequeNo(studentLifeProposalDTO.getChequeNo());
        } else if (studentLifeProposalDTO.getPaymentChannel().equalsIgnoreCase("CSH")) {
          lifeProposal.setPaymentChannel(PaymentChannel.CASHED);
        } else if (studentLifeProposalDTO.getPaymentChannel().equalsIgnoreCase("CHQ")) {
          lifeProposal.setPaymentChannel(PaymentChannel.CHEQUE);
          lifeProposal.setChequeNo(studentLifeProposalDTO.getChequeNo());
          lifeProposal.setToBank(studentLifeProposalDTO.getToBank());
          lifeProposal.setFromBank(studentLifeProposalDTO.getFromBank());
        } else if (studentLifeProposalDTO.getPaymentChannel().equalsIgnoreCase("RCV")) {
          lifeProposal.setPaymentChannel(PaymentChannel.SUNDRY);
          lifeProposal.setToBank(studentLifeProposalDTO.getToBank());
          lifeProposal.setFromBank(studentLifeProposalDTO.getFromBank());
        }
      
      lifeProposal.getProposalInsuredPersonList().add(createInsuredPersonForStudentLife(insuredPerson));
      if(studentLifeProposalDTO.getCustomerID() ==null || studentLifeProposalDTO.getCustomerID().isEmpty()) {
    	Customer customer  = createCustomer(studentLifeProposalDTO);
    	lifeProposal.setCustomer(customer);
    	lifeProposal.getProposalInsuredPersonList().get(0).setNewCustomer(true);
      }else if (customerOptional.isPresent()) {
        lifeProposal.setCustomer(customerOptional.get());
      }
      lifeProposal.setComplete(true);
      lifeProposal.setProposalType(ProposalType.UNDERWRITING);
      lifeProposal.setSubmittedDate(studentLifeProposalDTO.getSubmittedDate());
      
      if(branchOptional.isPresent()) {
    	  lifeProposal.setBranch(branchOptional.get());
      }
      if(referralOptional.isPresent()) {
    	  lifeProposal.setReferral(referralOptional.get());
      }
      
      if(agentOptional.isPresent()) {
          lifeProposal.setAgent(agentOptional.get());
      }
      if(saleManOptional.isPresent()) {
          lifeProposal.setSaleMan(saleManOptional.get());
      }
      if(salePointOptional.isPresent()) {
          lifeProposal.setSalePoint(salePointOptional.get());
      }
      if(paymentTypeOptional.isPresent()) {
    	  lifeProposal.setPaymentType(paymentTypeOptional.get());
      }
      CommonCreateAndUpateMarks recorder = new CommonCreateAndUpateMarks();
      recorder.setCreatedDate(new Date());
      lifeProposal.setRecorder(recorder);
      String proposalNo = customIdRepo.getNextId("STUDENT_LIFE_PROPOSAL_NO_ID_GEN", null);
      lifeProposal.setProposalNo(proposalNo);
      lifeProposal.setPrefix("ISLIF001");
      lifeProposalList.add(lifeProposal);
     });
	  }catch(DAOException e) {
		 throw new SystemException(e.getErrorCode(), e.getMessage());
	  }
	  return lifeProposalList;
  }
  
  // ForStudentLife proposal to policy
  private List<LifePolicy> convertStudentLifeProposalToPolicy(List<LifeProposal> studentlifeProposalList) {
    List<LifePolicy> policyList = new ArrayList<>();
      studentlifeProposalList.forEach(proposal -> {
      LifePolicy policy = new LifePolicy(proposal);
      String policyNo = customIdRepo.getNextId("STUDENT_LIFE_POLICY_NO", null);
      policy.setPolicyNo(policyNo);
      policy.setPaymentChannel(proposal.getPaymentChannel());
      policy.setFromBank(proposal.getFromBank());
      policy.setToBank(proposal.getToBank());
      policy.setChequeNo(proposal.getChequeNo());
   
      policy.setActivedPolicyStartDate(policy.getPolicyInsuredPersonList().get(0).getStartDate());
      policy.setActivedPolicyEndDate(policy.getPolicyInsuredPersonList().get(0).getEndDate());
      policy.setCommenmanceDate(proposal.getSubmittedDate());
      policy.setLastPaymentTerm(1);
      Calendar cal = Calendar.getInstance();
	  cal.setTime(policy.getPolicyInsuredPersonList().get(0).getEndDate());
	  cal.add(Calendar.YEAR, -3);
	  policy.setPaymentEndDate(cal.getTime());
      policy.setPolicyStatus(PolicyStatus.INFORCE);
      CommonCreateAndUpateMarks recorder = new CommonCreateAndUpateMarks();
      recorder.setCreatedDate(new Date());
      policy.setRecorder(recorder);
      policyList.add(policy);
    });
    return policyList;
  }
  
  private Customer createCustomer(StudentLifeProposalDTO studentLifeProposalDTO) {
	  try {
  	  Optional<Township> townshipOptional = townShipService.findById(studentLifeProposalDTO.getResidentTownshipId());
      Optional<Country> countryOptional = countryService.findById(studentLifeProposalDTO.getNationalityId());
      Optional<Township> officeTownshipOptional = townShipService.findById(studentLifeProposalDTO.getOfficeTownshipId());
      Optional<Occupation> occupationOptional =occupationService.findById(studentLifeProposalDTO.getOccupationId());
	  Customer customer =new Customer();
	  customer.setInitialId(studentLifeProposalDTO.getSalutation());
	  customer.getName().setFirstName(studentLifeProposalDTO.getFirstName());
	  customer.getName().setMiddleName(studentLifeProposalDTO.getMiddleName());
	  customer.getName().setLastName(studentLifeProposalDTO.getLastName());
	  customer.setFatherName(studentLifeProposalDTO.getFatherName());
	  customer.setIdType(IdType.valueOf(studentLifeProposalDTO.getIdType()));
	  customer.setIdNo(studentLifeProposalDTO.getIdNo());
	  customer.setFullIdNo(studentLifeProposalDTO.getIdNo());
	  customer.setDateOfBirth(studentLifeProposalDTO.getDateOfBirth());
	  customer.setGender(Gender.valueOf(studentLifeProposalDTO.getGender()));
	  customer.getContentInfo().setMobile(studentLifeProposalDTO.getPhone());
	  customer.getContentInfo().setPhone(studentLifeProposalDTO.getPhone());
	  customer.getContentInfo().setEmail(studentLifeProposalDTO.getEmail());
	  loadStateCodeAndTownShipCode(customer);
	  customer.setMaritalStatus(MaritalStatus.valueOf(studentLifeProposalDTO.getMartialStatus()));
	  if(occupationOptional.isPresent()) {
		  customer.setOccupation(occupationOptional.get());
	  }
	  if(officeTownshipOptional.isPresent()) {
		  customer.getOfficeAddress().setTownship(officeTownshipOptional.get());
	  }
	  	 customer.getOfficeAddress().setOfficeAddress(studentLifeProposalDTO.getOfficeAddress());
	  if(countryOptional.isPresent()) {
		  customer.setCountry(countryOptional.get());
	  }
	  if(townshipOptional.isPresent()) {
		  customer.getResidentAddress().setResidentTownship(townshipOptional.get());
		  customer.getPermanentAddress().setTownship(townshipOptional.get());
	  }
	  	customer.getResidentAddress().setResidentAddress(studentLifeProposalDTO.getResidentAddress());
	  	customer.getPermanentAddress().setPermanentAddress(studentLifeProposalDTO.getResidentAddress());
	  	customer.setPrefix("ISSYS001");
	  	customer = customerRepo.save(customer);
		return customer;
	  }catch (DAOException e) {
		 throw new SystemException(e.getErrorCode(),e.getMessage());
	  }
    }
  public void loadStateCodeAndTownShipCode(Customer customer) {
	  String provinceCode="";
	  String townshipCode="";
	  String idConditionType="";
	  String idNo="";
	  String fullIdNo="";
	  try {
		if (customer.getIdType().equals(IdType.NRCNO) && customer.getIdNo() != null) {
			StringTokenizer token = new StringTokenizer(customer.getIdNo(), "/()");
			provinceCode = token.nextToken();
			townshipCode = token.nextToken();
			idConditionType = token.nextToken();
			idNo = token.nextToken();
			fullIdNo = provinceCode == null ? "" : fullIdNo;
		} else if (customer.getIdType().equals(IdType.FRCNO) || customer.getIdType().equals(IdType.PASSPORTNO)) {
			idNo = fullIdNo == null ? "" : fullIdNo;
		}
		Optional <StateCode>  stateCode =stateCodeService.findByCodeNo(provinceCode);
		Optional<TownshipCode> tcode=townShipCodeService.findByTownshipcodeno(townshipCode,stateCode.get().getId());
		if(stateCode.isPresent()) {
			customer.setStateCode(stateCode.get());
		 }
		if(tcode.isPresent()) {
			customer.setTownshipCode(tcode.get());
		}
		customer.setIdConditionType(IdConditionType.valueOf(idConditionType));
	  }catch(DAOException e) {
		  throw new SystemException(e.getErrorCode(), e.getMessage());
	  }
	}


  private ProposalInsuredPerson createInsuredPersonForStudentLife(StudentLifeProposalInsuredPersonDTO dto) {
	try {
    Optional<Product> productOptional = productService.findById(studentLifeProductId);
    Optional<Township> townshipOptional = townShipService.findById(dto.getTownshipId());
    Optional<GradeInfo> gradeOptional = gradeInfoService.findById(dto.getGradeInfo());
    Optional<School> school =schoolService.findById(dto.getSchoolId());
    Optional<RelationShip> relationShip=relationshipService.findById(dto.getRelationshipId());
    CommonCreateAndUpateMarks recorder = new CommonCreateAndUpateMarks();
    recorder.setCreatedDate(new Date());
    ResidentAddress residentAddress = new ResidentAddress();
    residentAddress.setResidentAddress(dto.getResidentAddress());
    residentAddress.setResidentTownship(townshipOptional.get());

    Name name = new Name();
    name.setFirstName(dto.getFirstName());
    name.setMiddleName(dto.getMiddleName());
    name.setLastName(dto.getLastName());

    ProposalInsuredPerson insuredPerson = new ProposalInsuredPerson();
    if(productOptional.isPresent()) {
    	insuredPerson.setProduct(productOptional.get());
    }
    if(relationShip.isPresent()) {
    	insuredPerson.setRelationship(relationShip.get());
    }
    insuredPerson.setInitialId(dto.getInitialId());
    insuredPerson.setBpmsInsuredPersonId(dto.getBpmsInsuredPersonId());
    insuredPerson.setProposedSumInsured(dto.getProposedSumInsured());
    insuredPerson.setProposedPremium(dto.getProposedPremium());
    insuredPerson.setApprovedSumInsured(dto.getApprovedSumInsured());
    insuredPerson.setApprovedPremium(dto.getApprovedPremium());
    insuredPerson.setBasicTermPremium(dto.getBasicTermPremium());
    insuredPerson.setIdType(IdType.valueOf(dto.getIdType()));
    insuredPerson.setIdNo(dto.getIdNo());
    insuredPerson.setClsOfHealth(ClassificationOfHealth.FIRSTCLASS);
    insuredPerson.setParentName(dto.getMotherName());
    insuredPerson.setParentIdNo(dto.getMotherIdNo());
    insuredPerson.setParentDOB(dto.getMotherDOB());
    insuredPerson.setParentIdType(IdType.valueOf(dto.getMotherIdType()));
    insuredPerson.setDateOfBirth(dto.getDateOfBirth());
    insuredPerson.setAge(DateUtils.getAgeForNextYear(dto.getDateOfBirth()));
    insuredPerson.setRecorder(recorder);
    insuredPerson.setGender(Gender.valueOf(dto.getGender()));
    int maxTerm = productOptional.get().getMaxTerm();
    int periodYears=(maxTerm - insuredPerson.getAgeForNextYear() + 1);
    insuredPerson.setPeriodMonth(periodYears * 12);
    
    if(school.isPresent()) {
        insuredPerson.setSchool(school.get());
    }
    if(gradeOptional.isPresent()) {
    	insuredPerson.setGradeInfo(gradeOptional.get());
    }
    insuredPerson.setFatherName(dto.getFatherName());
    insuredPerson.setStartDate(dto.getStartDate());
    insuredPerson.setEndDate(dto.getEndDate());
    insuredPerson.setGender(Gender.valueOf(dto.getGender()));
    insuredPerson.setResidentAddress(residentAddress);
    insuredPerson.setName(name);
    String insPersonCodeNo = customIdRepo.getNextId("LIFE_INSUREDPERSON_CODENO_ID_GEN", null);
    insuredPerson.setInsPersonCodeNo(insPersonCodeNo);
    insuredPerson.setPrefix("ISLIF008");
    return insuredPerson;
	}catch(DAOException e) {
		throw new SystemException(e.getErrorCode(),e.getMessage());
	}
  }

  // for student life payment
  private List<Payment> convertStudentLifePolicyToPayment(List<LifePolicy> studentlifePolicyList) {
	  List<Payment> paymentList = new ArrayList<Payment>();
	 try {
		 studentlifePolicyList.forEach(lifePolicy -> {
      Optional<Bank> fromBankOptional = Optional.empty();
      Optional<Bank> toBankOptional = Optional.empty();
      if (lifePolicy.getFromBank() != null) {
        fromBankOptional = bankService.findById(lifePolicy.getFromBank());
      }
      if (lifePolicy.getToBank() != null) {
        toBankOptional = bankService.findById(lifePolicy.getToBank());
      }
      Payment payment = new Payment();
      double rate = 1.0;
      String receiptNo = "";
      CommonCreateAndUpateMarks recorder = new CommonCreateAndUpateMarks();
      recorder.setCreatedDate(new Date());
      if (PaymentChannel.CASHED.equals(lifePolicy.getPaymentChannel())) {
        receiptNo = customIdRepo.getNextId("CASH_RECEIPT_ID_GEN", null);
        payment.setPaymentChannel(PaymentChannel.CASHED);
      } else if (PaymentChannel.CHEQUE.equals(lifePolicy.getPaymentChannel())) {
        payment.setPO(true);
        payment.setPaymentChannel(PaymentChannel.CHEQUE);
        receiptNo = customIdRepo.getNextId("CHEQUE_RECEIPT_ID_GEN", null);
        payment.setChequeNo(lifePolicy.getChequeNo());
      } else if (PaymentChannel.TRANSFER.equals(lifePolicy.getPaymentChannel())) {
        payment.setPoNo(lifePolicy.getChequeNo());
        payment.setPaymentChannel(PaymentChannel.TRANSFER);
        receiptNo = customIdRepo.getNextId("TRANSFER_RECEIPT_ID_GEN", null);
      } else if (PaymentChannel.SUNDRY.equals(lifePolicy.getPaymentChannel())) {
        payment.setPaymentChannel(PaymentChannel.SUNDRY);
        receiptNo = customIdRepo.getNextId("CHEQUE_RECEIPT_ID_GEN", null);
        payment.setPO(true);
      }
      payment.setReceiptNo(receiptNo);
      payment.setPaymentType(lifePolicy.getPaymentType());
      payment.setReferenceType(PolicyReferenceType.STUDENT_LIFE_POLICY);
      payment.setConfirmDate(new Date());
      payment.setPaymentDate(new Date());
      if (toBankOptional.isPresent()) {
        payment.setAccountBank(toBankOptional.get());
      }
      if (fromBankOptional.isPresent()) {
        payment.setBank(fromBankOptional.get());
      }
      payment.setReferenceNo(lifePolicy.getId());
      payment.setBasicPremium(lifePolicy.getTotalBasicTermPremium());
      payment.setAddOnPremium(lifePolicy.getTotalAddOnTermPremium());
      payment.setFromTerm(1);
      payment.setToTerm(1);
      payment.setCur("KYT");
      payment.setRate(rate);
      payment.setComplete(true);
      payment.setAmount(payment.getNetPremium());
      payment.setHomeAmount(payment.getNetPremium());
      payment.setHomePremium(payment.getBasicPremium());
      payment.setHomeAddOnPremium(payment.getAddOnPremium());
      payment.setCommonCreateAndUpateMarks(recorder);
      paymentList.add(payment);
    });
	}catch (DAOException e) {
	      throw new SystemException(e.getErrorCode(), e.getMessage());
	}catch(Exception e){
		e.printStackTrace();
	 }
	 return paymentList;
  }


  // agent Commission
  private List<AgentCommission> convertStudentLifePolicyToAgentCommission(List<LifePolicy> studentlifePolicyList) {
	List<AgentCommission> agentCommissionList = new ArrayList<AgentCommission>();
	try {
    /* get agent commission of each policy */
    studentlifePolicyList.forEach(lifePolicy -> {
      Product product = lifePolicy.getPolicyInsuredPersonList().get(0).getProduct();
      double commissionPercent = product.getFirstCommission();
      Payment payment = paymentRepository.findByPaymentReferenceNo(lifePolicy.getId());
      double rate = payment.getRate();
      double firstAgentCommission = lifePolicy.getAgentCommission();
      agentCommissionList.add(new AgentCommission(lifePolicy.getId(),
          PolicyReferenceType.STUDENT_LIFE_POLICY, lifePolicy.getAgent(), firstAgentCommission,
          new Date(), payment.getReceiptNo(), lifePolicy.getTotalTermPremium(), commissionPercent,
          AgentCommissionEntryType.UNDERWRITING, rate, (rate * firstAgentCommission), "KYT",
          (rate * lifePolicy.getTotalTermPremium())));
    	});
	}catch(Exception e) {
		e.printStackTrace();
	}
	 return agentCommissionList;
  }


}
