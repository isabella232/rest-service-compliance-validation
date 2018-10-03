package gov.nsf.psm.compliancevalidation.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.kie.api.runtime.ExecutionResults;
import org.kie.api.runtime.StatelessKieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import gov.nsf.psm.compliancevalidation.commandExecutor.BudgetCheckCommandExecutor;
import gov.nsf.psm.compliancevalidation.commandExecutor.CoverSheetCheckCommandExecutor;
import gov.nsf.psm.compliancevalidation.commandExecutor.PDFUploadCommandExecutor;
import gov.nsf.psm.compliancevalidation.commandExecutor.ProposalSectionsCheckCommandExecutor;
import gov.nsf.psm.compliancevalidation.commandExecutor.ProposalUpdateJustficationCheckCommandExecutor;
import gov.nsf.psm.compliancevalidation.conversion.utility.ComplianceConstants;
import gov.nsf.psm.compliancevalidation.conversion.utility.DocumentFactModelConversionUtils;
import gov.nsf.psm.factmodel.BudgetRecordFactModel;
import gov.nsf.psm.factmodel.ComplianceResults;
import gov.nsf.psm.factmodel.CoverSheetFactModel;
import gov.nsf.psm.factmodel.DocumentFactModel;
import gov.nsf.psm.factmodel.DocCompMessagesEnum;
import gov.nsf.psm.factmodel.InstitutionBudgetFactModel;
import gov.nsf.psm.factmodel.PSMMessage;
import gov.nsf.psm.factmodel.ProposalFactModel;
import gov.nsf.psm.factmodel.ProposalUpdateJustificationFactModel;
import gov.nsf.psm.foundation.exception.CommonUtilException;
import gov.nsf.psm.foundation.model.Section;




public class ComplianceValidationServiceImpl implements
		ComplianceValidationService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ComplianceValidationServiceImpl.class);

	@Autowired
	StatelessKieSession kieSession;

	@Autowired
	MessageLocaleService messageLocaleService;
	


	@Value("${file.size.max-limit}")
	private  String fileSizeMaxLimit;
	
	@Value("${file.name.max-length}")
	private  String fileNameCharacterLimit;


	@Value("${line.spacing.min}")
	private String lineSpacingLimit;
		
		
	@Value("${margin.inch-to-points}")
	private  String marginInchToPoints;
	
	
	@Value("${margin.toler-adj-factor-inches}")
	private  String marginToleranceAdjFactor;

	@Value("${heading.lower-boundary}")
	private  String headingLowerBoundary;

	@Value("${heading.upper-boundary}")
	private  String headingUpperBoundary;

	@Value("${font.name-size}")
	private  String fontTypes;
	
	@Value("${font.size-default}")
	private  String fontSizeDefault;
	
	@Value("${font.toler-adj-factor}")
	private  String fontSizeAdjTolerance;
	
	
	private  Map<String, String> compliancePropertiesMap =new HashMap<String, String>();
	

	@Override
	public ComplianceResults getPdfUploadComplianceCheckFindings(DocumentFactModel docFactModel) throws CommonUtilException{
		try {

			ComplianceResults compResultsFired = new ComplianceResults();
			List<PSMMessage> psmMsgList = new ArrayList<PSMMessage>();
			compResultsFired.setMessages(psmMsgList);
			compResultsFired.setCompliant(false);
			DocumentFactModel docModel;

			LOGGER.debug("Document Fact Model before Conversion -"
					+ docFactModel.toString());
			LOGGER.debug("Mime Type -" + docFactModel.getMimeType());
			LOGGER.debug("Is PDF Mime Type -"
					+ docFactModel.isCorrectMimeType());
			LOGGER.debug("Section Name -" + docFactModel.getSectionName());
			
			populateCompliancePropertiesMap ();
	
			docFactModel.getFile().setMaxFileSize(new Float(fileSizeMaxLimit));
			docFactModel.getFile().setMaxFileName(fileNameCharacterLimit);
				
			if (docFactModel.getMimeType().equalsIgnoreCase(ComplianceConstants.MIMETYPE_PDF)
					&& docFactModel.isCorrectMimeType()
					&& !docFactModel.getPages().isEmpty()) {
				// Perform the Conversion of Fact Model to derive evaluated
				// facts for PDF Uploads and if there are more than one page

				docModel = DocumentFactModelConversionUtils
						.convertDocumentFactModel(docFactModel,compliancePropertiesMap);
				LOGGER.debug("Document Fact Model conversion completion for evaluated facts.");

			} else {
				// Document does not contain any text or is not the
				// correctMimeType (Case when the FileSize>10MB or incorrect
				// FileType is uploaded)
				LOGGER.info("Document does not contain any text or is not the PDF MimeType");
				docModel = docFactModel;
			}
			LOGGER.info("Document Fact Model before Rules Execution -"
					+ docFactModel.toString());
			ExecutionResults results = PDFUploadCommandExecutor
					.executeBusinessRules(docModel, compResultsFired,
							kieSession);
			int noOfRulesFired = (Integer) results.getValue(ComplianceConstants.NUMBER_OF_RULES_FIRED);
			LOGGER.info(" Number of Rules Fired -" + noOfRulesFired);
			compResultsFired = (ComplianceResults) results
					.getValue(ComplianceConstants.COMPLAINCE_RESULTS_FIRED);
			List<PSMMessage> psmRulesTriggerdMessageList = compResultsFired
					.getMessages();
			List<PSMMessage> populatedPsmRulesTriggerdMessageList = new ArrayList<PSMMessage>();
			for (PSMMessage psmMessg : psmRulesTriggerdMessageList) {
				DocCompMessagesEnum msgId = psmMessg.getMsgIdEnum();

				if (psmMessg.getConcatMsg1().isEmpty()) {
					LOGGER.debug("Concatenated message -"
							+ psmMessg.getConcatMsg1());
				}

				psmMessg = messageLocaleService.getMessage(msgId,
						psmMessg.getConcatMsg1(),psmMessg.getConcatMsg2());

				LOGGER.debug("Message Description  -"
						+ psmMessg.getDescription());
				populatedPsmRulesTriggerdMessageList.add(psmMessg);

			}
			compResultsFired.setMessages(populatedPsmRulesTriggerdMessageList);

			if (noOfRulesFired == 0) {
				LOGGER.debug("No Errors or warning returned");

				compResultsFired.setCompliant(true);
			}

			return compResultsFired;
		} catch (Exception e) {
			throw new CommonUtilException(
					ComplianceConstants.GET_ALL_FINDINGS_DOCUMET_UPLOAD_ERROR,
					e);
		}
	}

	@Override
	public ComplianceResults getBudgetComplianceCheckFindings(InstitutionBudgetFactModel instBudgetFactModel) throws CommonUtilException{
		try {
			ComplianceResults compResultsFired = new ComplianceResults();
			List<PSMMessage> psmMsgList = new ArrayList<PSMMessage>();
			compResultsFired.setMessages(psmMsgList);
			compResultsFired.setCompliant(false);

			List<BudgetRecordFactModel> budgetRecordList = instBudgetFactModel
					.getBudgetRecordList();
			/* Evaluate Missing Budget Maps */

			instBudgetFactModel
					.populateIndirectCostFactModelMissingMaps(budgetRecordList);
			LOGGER.debug("Completed the populateIndirectCostFactModelMissingMaps");

			instBudgetFactModel
					.populateParticipantCostFactModelMissingYearsList(budgetRecordList);
			LOGGER.debug("Completed the populateParticipantCostFactModelMissingYearsList");
			instBudgetFactModel
					.populateEquipmentCostFactModelMissingMaps(budgetRecordList);
			LOGGER.debug("Completed the populateEquipmentCostFactModelMissingMaps");
			instBudgetFactModel
					.populateOtherPersonnelCostFactModelMissingYearsList(budgetRecordList);
			LOGGER.debug("Completed the populateOtherPersonnelCostFactModelMissingYearsList");
			instBudgetFactModel
					.populateSnPersonnelCostFactModelMissingYearsList(budgetRecordList);
			LOGGER.debug("Completed the populateSnPersonnelCostFactModelMissingYearsList");

			LOGGER.debug("Institution Budget Fact Model before Rules Execution -"
					+ instBudgetFactModel.toString());
			ExecutionResults results = BudgetCheckCommandExecutor
					.executeBusinessRules(instBudgetFactModel,
							compResultsFired, kieSession);
			int noOfRulesFired = (Integer) results.getValue(ComplianceConstants.NUMBER_OF_RULES_FIRED);
			LOGGER.info(" Number of Rules Fired -" + noOfRulesFired);

			compResultsFired = (ComplianceResults) results
					.getValue(ComplianceConstants.COMPLAINCE_RESULTS_FIRED);
			List<PSMMessage> psmRulesTriggerdMessageList = compResultsFired
					.getMessages();
			List<PSMMessage> populatedPsmRulesTriggerdMessageList = new ArrayList<PSMMessage>();

			for (PSMMessage psmMessg : psmRulesTriggerdMessageList) {
				DocCompMessagesEnum msgId = psmMessg.getMsgIdEnum();

				psmMessg = messageLocaleService.getMessage(msgId,
						psmMessg.getConcatMsg1());

				psmMessg.setSectionCode(Section.BUDGETS.getCode());
				LOGGER.debug("Message Description  -"
						+ psmMessg.getDescription());
				populatedPsmRulesTriggerdMessageList.add(psmMessg);

			}
			compResultsFired.setMessages(populatedPsmRulesTriggerdMessageList);

			if (noOfRulesFired == 0) {
				LOGGER.debug("No Errors or warning returned");
				compResultsFired.setCompliant(true);
			}

			return compResultsFired;

		} catch (Exception e) {
			throw new CommonUtilException(
					ComplianceConstants.GET_ALL_FINDINGS_BUDGET_CHECK_ERROR, e);
		}

	}

	@Override
	public ComplianceResults getCoverSheetComplianceCheckFindings(CoverSheetFactModel coverSheetFactModel)throws CommonUtilException {
		try {
			ComplianceResults compResultsFired = new ComplianceResults();
			List<PSMMessage> psmMsgList = new ArrayList<PSMMessage>();
			compResultsFired.setMessages(psmMsgList);
			compResultsFired.setCompliant(false);

			LOGGER.info("CoverSheet Fact Model before Rules Execution-"
					+ coverSheetFactModel.toString());
			ExecutionResults results = CoverSheetCheckCommandExecutor
					.executeBusinessRules(coverSheetFactModel,
							compResultsFired, kieSession);
			int noOfRulesFired = (Integer) results.getValue(ComplianceConstants.NUMBER_OF_RULES_FIRED);
			LOGGER.info(" Number of Rules Fired -" + noOfRulesFired);
			compResultsFired = (ComplianceResults) results
					.getValue(ComplianceConstants.COMPLAINCE_RESULTS_FIRED);
			List<PSMMessage> psmRulesTriggerdMessageList = compResultsFired
					.getMessages();
			List<PSMMessage> populatedPsmRulesTriggerdMessageList = new ArrayList<PSMMessage>();
			for (PSMMessage psmMessg : psmRulesTriggerdMessageList) {
				DocCompMessagesEnum msgId = psmMessg.getMsgIdEnum();
				psmMessg = messageLocaleService.getMessage(msgId,
						psmMessg.getConcatMsg1());
				LOGGER.debug("Message Description  -"
						+ psmMessg.getDescription());
				psmMessg.setSectionCode(Section.COVER_SHEET.getCode());
				populatedPsmRulesTriggerdMessageList.add(psmMessg);

			}
			compResultsFired.setMessages(populatedPsmRulesTriggerdMessageList);

			if (noOfRulesFired == 0) {
				LOGGER.debug("No Errors or warning returned");
				compResultsFired.setCompliant(true);
			}

			return compResultsFired;

		} catch (Exception e) {
			throw new CommonUtilException(
					ComplianceConstants.GET_ALL_FINDINGS_COVERSHEET_CHECK_ERROR,
					e);
		}

	}

	@Override
	public ComplianceResults getProposalSectionsComplianceCheckFindings(ProposalFactModel proposalFactModel) throws CommonUtilException {
		try {
			ComplianceResults compResultsFired = new ComplianceResults();
			List<PSMMessage> psmMsgList = new ArrayList<PSMMessage>();
			compResultsFired.setMessages(psmMsgList);
			compResultsFired.setCompliant(false);

			
			proposalFactModel.populateMissingBiographicalSketchesFactModel(proposalFactModel.getBiogSketchesFactModelList());
			proposalFactModel.populateMissingCurrentPendingFactModel(proposalFactModel.getCurrPendSuppFactModelList());
			proposalFactModel.populateMissingCOAFactModel(proposalFactModel.getCoaFactModelList());
			
			proposalFactModel.checkDeadLineDatePassed(proposalFactModel.getDeadlineFactModel());
			proposalFactModel.checkDeadLineDate24Hours(proposalFactModel.getDeadlineFactModel());
			proposalFactModel.checkFundingOpportunityExpired(proposalFactModel.getFundingOppFactModel());
			LOGGER.info("Proposal Fact Model before Rules Execution -" + proposalFactModel.toString());
			ExecutionResults results = ProposalSectionsCheckCommandExecutor
					.executeBusinessRules(proposalFactModel, compResultsFired,
							kieSession);
			int noOfRulesFired = (Integer) results.getValue(ComplianceConstants.NUMBER_OF_RULES_FIRED);
			LOGGER.info(" Number of Rules Fired -" + noOfRulesFired);
			compResultsFired = (ComplianceResults) results
					.getValue(ComplianceConstants.COMPLAINCE_RESULTS_FIRED);
			List<PSMMessage> psmRulesTriggerdMessageList = compResultsFired
					.getMessages();
			List<PSMMessage> populatedPsmRulesTriggerdMessageList = new ArrayList<PSMMessage>();

			String sectionCode = "";
			for (PSMMessage psmMessg : psmRulesTriggerdMessageList) {
				sectionCode = psmMessg.getSectionCode();
				DocCompMessagesEnum msgId = psmMessg.getMsgIdEnum();
				
				psmMessg = messageLocaleService.getMessage(msgId,
						psmMessg.getConcatMsg1(),psmMessg.getConcatMsg2());
				LOGGER.debug("Message Description  -"
						+ psmMessg.getDescription());

				psmMessg.setSectionCode(sectionCode);
				LOGGER.debug("SectionCode -" + psmMessg.getSectionCode());
				populatedPsmRulesTriggerdMessageList.add(psmMessg);

			}
			compResultsFired.setMessages(populatedPsmRulesTriggerdMessageList);

			if (noOfRulesFired == 0) {
				LOGGER.debug("No Errors or warning returned");
				compResultsFired.setCompliant(true);
			}

			return compResultsFired;

		} catch (Exception e) {
			throw new CommonUtilException(
					ComplianceConstants.GET_ALL_FINDINGS_PROPOSAL_SECTIONS_CHECK_ERROR,
					e);
		}
	}
	
	@Override
	  public ComplianceResults getProposalFileUpdateComplianceCheckFindings(ProposalUpdateJustificationFactModel propUpdateJustificationFactModel)throws CommonUtilException
	  {
		  		  
			try {
				ComplianceResults compResultsFired = new ComplianceResults();
				List<PSMMessage> psmMsgList = new ArrayList<PSMMessage>();
				compResultsFired.setMessages(psmMsgList);
				compResultsFired.setCompliant(false);
				LOGGER.info("Proposal Update Justifaction Factmodel before Rules Execution" + propUpdateJustificationFactModel);
				ExecutionResults results = ProposalUpdateJustficationCheckCommandExecutor
						.executeBusinessRules(propUpdateJustificationFactModel, compResultsFired,
								kieSession);
				int noOfRulesFired = (Integer) results.getValue(ComplianceConstants.NUMBER_OF_RULES_FIRED);
				LOGGER.info(" Number of Rules Fired -" + noOfRulesFired);
				compResultsFired = (ComplianceResults) results
						.getValue(ComplianceConstants.COMPLAINCE_RESULTS_FIRED);
				List<PSMMessage> psmRulesTriggerdMessageList = compResultsFired
						.getMessages();
				List<PSMMessage> populatedPsmRulesTriggerdMessageList = new ArrayList<PSMMessage>();

				String sectionCode = "";
				for (PSMMessage psmMessg : psmRulesTriggerdMessageList) {
					sectionCode = psmMessg.getSectionCode();
					DocCompMessagesEnum msgId = psmMessg.getMsgIdEnum();
					
					psmMessg = messageLocaleService.getMessage(msgId,
							psmMessg.getConcatMsg1());
					LOGGER.debug("Message Description  -"
							+ psmMessg.getDescription());

					psmMessg.setSectionCode(sectionCode);
					LOGGER.debug("SectionCode -" + psmMessg.getSectionCode());
					populatedPsmRulesTriggerdMessageList.add(psmMessg);

				}
				compResultsFired.setMessages(populatedPsmRulesTriggerdMessageList);
				
				if (noOfRulesFired == 0) {
					LOGGER.debug("No Errors or warning returned");
					compResultsFired.setCompliant(true);
				}

				return compResultsFired;

			} catch (Exception e) {
				throw new CommonUtilException(
						ComplianceConstants.GET_ALL_FINDINGS_PROPOSAL_UPDATE_JUSTIFICATION_CHECK_ERROR,
						e);
			}
		  
	  }
	
	@Override
	public void populateCompliancePropertiesMap()
	{
		
		compliancePropertiesMap.put("LINE_SPACING_MIN",lineSpacingLimit);
		compliancePropertiesMap.put("TOLERANCE_ADJ_FACTOR_INCHES",marginToleranceAdjFactor);
		compliancePropertiesMap.put("MARGIN_MIN",marginInchToPoints);
		compliancePropertiesMap.put("HEADING_LOWER_BOUNDARY",headingLowerBoundary);
		compliancePropertiesMap.put("HEADING_UPPER_BOUNDARY",headingUpperBoundary);
		compliancePropertiesMap.put("FONT_TYPES", fontTypes);
		compliancePropertiesMap.put("FONT_SIZE_DEFAULT", fontSizeDefault);
		compliancePropertiesMap.put("FONT_TOLERANCE_ADJ_FACTOR",fontSizeAdjTolerance);
	
	}

}
