package gov.nsf.psm.compliancevalidation.service;

import java.util.Map;

import gov.nsf.psm.factmodel.ComplianceResults;
import gov.nsf.psm.factmodel.CoverSheetFactModel;
import gov.nsf.psm.factmodel.DocumentFactModel;
import gov.nsf.psm.factmodel.InstitutionBudgetFactModel;
import gov.nsf.psm.factmodel.ProposalFactModel;
import gov.nsf.psm.factmodel.ProposalUpdateJustificationFactModel;
import gov.nsf.psm.foundation.exception.CommonUtilException;

public interface ComplianceValidationService {

     public ComplianceResults getPdfUploadComplianceCheckFindings(DocumentFactModel docFactModel) throws CommonUtilException;
     public ComplianceResults getBudgetComplianceCheckFindings(InstitutionBudgetFactModel instBudget)throws CommonUtilException;
     public ComplianceResults getCoverSheetComplianceCheckFindings(CoverSheetFactModel coverSheet)throws CommonUtilException;
     public ComplianceResults getProposalSectionsComplianceCheckFindings(ProposalFactModel propFactModel)throws CommonUtilException;
     public ComplianceResults getProposalFileUpdateComplianceCheckFindings(ProposalUpdateJustificationFactModel propUpdateJustificationFactModel)throws CommonUtilException;
     public void populateCompliancePropertiesMap();
}
