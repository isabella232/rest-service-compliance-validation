package gov.nsf.psm.compliancevalidation.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import gov.nsf.psm.compliancevalidation.service.ComplianceValidationService;
import gov.nsf.psm.factmodel.ComplianceResults;
import gov.nsf.psm.factmodel.CoverSheetFactModel;
import gov.nsf.psm.factmodel.DocumentFactModel;
import gov.nsf.psm.factmodel.InstitutionBudgetFactModel;
import gov.nsf.psm.factmodel.ProposalFactModel;
import gov.nsf.psm.factmodel.ProposalUpdateJustificationFactModel;
import gov.nsf.psm.foundation.controller.PsmBaseController;
import gov.nsf.psm.foundation.exception.CommonUtilException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/v1")

@ApiResponses(value = { @ApiResponse(code = 404, message = "Resource not found"),
        @ApiResponse(code = 500, message = "Internal server error") })

public class ComplianceValidationServiceController extends PsmBaseController {

	@Autowired
	ComplianceValidationService service;

	private static final Logger LOGGER = Logger.getLogger(ComplianceValidationServiceController.class);

	@ApiOperation(value = "Retrieves a list of Compliance Check Findings Document Upload", notes = "This API Retrieves a list of Compliance Check Findings Document Upload. ", response = ComplianceResults.class)

	@RequestMapping(path = "/checkComplianceDocumentUpload", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ComplianceResults getDocumentUploadCheckFindings(@RequestBody DocumentFactModel docFactModel) throws CommonUtilException{
		LOGGER.info("ComplianceValidationServiceController.getDocumentUploadCheckFindings()");
		
		ComplianceResults complianceResults = service.getPdfUploadComplianceCheckFindings(docFactModel);
		
		return complianceResults;
	}
	
	
	@ApiOperation(value = "Retrieves a list of Compliance Check Findings for Budget Save", notes = "This API Retrieves a list of Compliance Check Findings for Budget Save. ", response = ComplianceResults.class)

	@RequestMapping(path = "/checkComplianceBudget", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ComplianceResults getBudgetCheckFindings(@RequestBody InstitutionBudgetFactModel instBudget) throws CommonUtilException {
		LOGGER.info("ComplianceValidationServiceController.getBudgetCheckFindings()");
		
		ComplianceResults complianceResults = service.getBudgetComplianceCheckFindings(instBudget);
		
		return complianceResults;
	}
	
	@ApiOperation(value = "Retrieves a list of Compliance Check Findings for CoverSheet", notes = "This API Retrieves a list of Compliance Check Findings for Coversheet. ", response = ComplianceResults.class)

	@RequestMapping(path = "/checkComplianceCoverSheet", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ComplianceResults getCoversheetCheckFindings(@RequestBody CoverSheetFactModel covrSheetFactModel) throws CommonUtilException {
		LOGGER.info("ComplianceValidationServiceController.getCoversheetCheckFindings()");
		
		ComplianceResults complianceResults = service.getCoverSheetComplianceCheckFindings(covrSheetFactModel);
		
		return complianceResults;
	}
	
	@ApiOperation(value = "Retrieves a list of Compliance Check Findings for Proposal", notes = "This API Retrieves a list of Compliance Check Findings for Missing Proposal Sections", response = ComplianceResults.class)

	@RequestMapping(path = "/checkComplianceProposalSections", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ComplianceResults getComplainceProposalCheckFindings(@RequestBody ProposalFactModel proposalFactModel) throws CommonUtilException {
		LOGGER.info("ComplianceValidationServiceController.getComplainceProposalCheckFindings()");
		
		ComplianceResults complianceResults = service.getProposalSectionsComplianceCheckFindings(proposalFactModel);
		
		return complianceResults;
	}
	
	@ApiOperation(value = "Retrieves the Compliance Check Finding for Proposal Update Justification ", notes = "This API Retrieves the Compliance Check Finding for Missing Proposal Update Justification", response = ComplianceResults.class)

	@RequestMapping(path = "/checkComplianceProposalUpdJustSection", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ComplianceResults getComplainceProposalUpdateJustificationCheckFindings(@RequestBody ProposalUpdateJustificationFactModel proposalUpdJustFactModel) throws CommonUtilException {
		LOGGER.info("ComplianceValidationServiceController.getComplainceProposalUpdateJustificationCheckFindings()");
		
		ComplianceResults complianceResults = service.getProposalFileUpdateComplianceCheckFindings(proposalUpdJustFactModel);
		
		return complianceResults;
	}
}
