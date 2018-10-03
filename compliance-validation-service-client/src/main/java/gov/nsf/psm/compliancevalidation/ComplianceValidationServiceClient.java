package gov.nsf.psm.compliancevalidation;

import java.util.List;
import gov.nsf.psm.factmodel.CoverSheetFactModel;
import gov.nsf.psm.factmodel.PSMMessage;
import gov.nsf.psm.factmodel.DocumentFactModel;
import gov.nsf.psm.factmodel.InstitutionBudgetFactModel;
import gov.nsf.psm.factmodel.ProposalFactModel;
import gov.nsf.psm.factmodel.ProposalUpdateJustificationFactModel;
import gov.nsf.psm.foundation.exception.CommonUtilException;

public interface ComplianceValidationServiceClient {
	
	public List<PSMMessage> getDocumentUploadComplianceCheckFindings(DocumentFactModel docFactModel) throws CommonUtilException;

	public List<PSMMessage> getBudgetComplianceCheckFindings(InstitutionBudgetFactModel instBudget) throws CommonUtilException;

	public List<PSMMessage> getCoverSheetComplianceCheckFindings(CoverSheetFactModel coverSheet) throws CommonUtilException;
	
	public List<PSMMessage> getProposalSectionsComplianceCheckFindings (ProposalFactModel coverSheet) throws CommonUtilException;
	
	public List<PSMMessage> getProposalFileUpdateComplianceCheckFindings (ProposalUpdateJustificationFactModel propUpdateJustificationFactModel) throws CommonUtilException;
}