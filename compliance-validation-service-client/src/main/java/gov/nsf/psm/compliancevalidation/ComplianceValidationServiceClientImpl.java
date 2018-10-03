package gov.nsf.psm.compliancevalidation;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import gov.nsf.psm.factmodel.ComplianceResults;
import gov.nsf.psm.factmodel.CoverSheetFactModel;
import gov.nsf.psm.factmodel.DocumentFactModel;
import gov.nsf.psm.factmodel.InstitutionBudgetFactModel;
import gov.nsf.psm.factmodel.PSMMessage;
import gov.nsf.psm.factmodel.ProposalFactModel;
import gov.nsf.psm.factmodel.ProposalUpdateJustificationFactModel;
import gov.nsf.psm.foundation.exception.CommonUtilException;
import gov.nsf.psm.foundation.restclient.NsfRestTemplate;

public class ComplianceValidationServiceClientImpl implements ComplianceValidationServiceClient {

    boolean serviceEnabled;
	private String serverURL;
	private String username;
	private String password;
	private int requestTimeout;
	private boolean authenticationRequired;
	private String checkCompDocUploadURL = "/checkComplianceDocumentUpload";
	private String checkCompBudgetURL = "/checkComplianceBudget";
	private String checkCompCoverSheetURL = "/checkComplianceCoverSheet";
	private String checkCompProposalSectionsURL = "/checkComplianceProposalSections";
	private String checkCompProposalUpdJustSectionsURL = "/checkComplianceProposalUpdJustSection";
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ComplianceValidationServiceClientImpl.class);
	
	public String getServerURL() {
		return serverURL;
	}

	public void setServerURL(String serverURL) {
		this.serverURL = serverURL;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isAuthenticationRequired() {
		return authenticationRequired;
	}

	public void setAuthenticationRequired(boolean authenticationRequired) {
		this.authenticationRequired = authenticationRequired;
	}

	public int getRequestTimeout() {
		return requestTimeout;
	}

	public void setRequestTimeout(int requestTimeout) {
		this.requestTimeout = requestTimeout;
	}

    public Boolean getServiceEnabled() {
        return serviceEnabled;
    }

    public void setServiceEnabled(Boolean serviceEnabled) {
        this.serviceEnabled = serviceEnabled;
    }
    
    /**
     * Helper method to create headers with basic authentication
     * @param username
     * @param password
     * @return
     */
	private static HttpHeaders createHttpHeaders(boolean authenticationRequired, String username, String password) {
		return authenticationRequired ? NsfRestTemplate.createHeaderswithAuthentication(username, password) : new HttpHeaders();
	}
	
	@Override
	public List<PSMMessage> getDocumentUploadComplianceCheckFindings(DocumentFactModel docFactModel) throws CommonUtilException {

		ResponseEntity<ComplianceResults> response = null;
				
		try {
			RestTemplate compliancevalidationServiceClient = NsfRestTemplate.setupRestTemplate(authenticationRequired,
					requestTimeout);
			StringBuilder endpointURL = new StringBuilder(serverURL);
            endpointURL.append(checkCompDocUploadURL);
            
            HttpHeaders headers = createHttpHeaders(authenticationRequired, username, password);
            HttpEntity<DocumentFactModel> httpEntity = new HttpEntity<DocumentFactModel>(docFactModel, headers);
            
            LOGGER.debug("Executing POST request on: " + endpointURL);
            response = compliancevalidationServiceClient.exchange(endpointURL.toString(), HttpMethod.POST, httpEntity, ComplianceResults.class);
           
		} catch (Exception e) {
			throw new CommonUtilException(e);
		}
		return response != null ? response.getBody().getMessages() : new ArrayList<PSMMessage>(); 
	}
	
	@Override
	public List<PSMMessage> getBudgetComplianceCheckFindings(InstitutionBudgetFactModel instBudget) throws CommonUtilException
	{
		ResponseEntity<ComplianceResults> response = null;
		
		try {
			RestTemplate compliancevalidationServiceClient = NsfRestTemplate.setupRestTemplate(authenticationRequired,
					requestTimeout);
			StringBuilder endpointURL = new StringBuilder(serverURL);
            endpointURL.append(checkCompBudgetURL);
            
            HttpHeaders headers = createHttpHeaders(authenticationRequired, username, password);
            HttpEntity<InstitutionBudgetFactModel> httpEntity = new HttpEntity<InstitutionBudgetFactModel>(instBudget, headers);
            
            LOGGER.debug("Executing POST request on: " + endpointURL);
            response = compliancevalidationServiceClient.exchange(endpointURL.toString(), HttpMethod.POST, httpEntity, ComplianceResults.class);
           
		} catch (Exception e) {
			throw new CommonUtilException(e);
		}
		return response != null ? response.getBody().getMessages() : new ArrayList<PSMMessage>(); 
	
	}
	
	@Override
	public List<PSMMessage> getCoverSheetComplianceCheckFindings(CoverSheetFactModel coverSheetFactModel) throws CommonUtilException
	{
		ResponseEntity<ComplianceResults> response = null;
		
		try {
			RestTemplate compliancevalidationServiceClient = NsfRestTemplate.setupRestTemplate(authenticationRequired,
					requestTimeout);
			StringBuilder endpointURL = new StringBuilder(serverURL);
            endpointURL.append(checkCompCoverSheetURL);
            
            HttpHeaders headers = createHttpHeaders(authenticationRequired, username, password);
            HttpEntity<CoverSheetFactModel> httpEntity = new HttpEntity<CoverSheetFactModel>(coverSheetFactModel, headers);
            
            LOGGER.debug("Executing POST request on: " + endpointURL);
            response = compliancevalidationServiceClient.exchange(endpointURL.toString(), HttpMethod.POST, httpEntity, ComplianceResults.class);
           
		} catch (Exception e) {
			throw new CommonUtilException(e);
		}
		return response != null ? response.getBody().getMessages() : new ArrayList<PSMMessage>(); 
	}
	
	@Override
	public List<PSMMessage> getProposalSectionsComplianceCheckFindings (ProposalFactModel propFactModel) throws CommonUtilException
	{
		ResponseEntity<ComplianceResults> response = null;
		
		try {
			RestTemplate compliancevalidationServiceClient = NsfRestTemplate.setupRestTemplate(authenticationRequired,
					requestTimeout);
			StringBuilder endpointURL = new StringBuilder(serverURL);
            endpointURL.append(checkCompProposalSectionsURL);
            
            HttpHeaders headers = createHttpHeaders(authenticationRequired, username, password);
            HttpEntity<ProposalFactModel> httpEntity = new HttpEntity<ProposalFactModel>(propFactModel, headers);
            
            LOGGER.debug("Executing POST request on: " + endpointURL);
            response = compliancevalidationServiceClient.exchange(endpointURL.toString(), HttpMethod.POST, httpEntity, ComplianceResults.class);
           
		} catch (Exception e) {
			throw new CommonUtilException(e);
		}
		return response != null ? response.getBody().getMessages() : new ArrayList<PSMMessage>(); 
	}
	
	@Override
	public List<PSMMessage> getProposalFileUpdateComplianceCheckFindings(ProposalUpdateJustificationFactModel propUpdateJustificationFactModel) throws CommonUtilException
	{
		ResponseEntity<ComplianceResults> response = null;
		
		try {
			RestTemplate compliancevalidationServiceClient = NsfRestTemplate.setupRestTemplate(authenticationRequired,
					requestTimeout);
			StringBuilder endpointURL = new StringBuilder(serverURL);
            endpointURL.append(checkCompProposalUpdJustSectionsURL);
            
            HttpHeaders headers = createHttpHeaders(authenticationRequired, username, password);
            HttpEntity<ProposalUpdateJustificationFactModel> httpEntity = new HttpEntity<ProposalUpdateJustificationFactModel>(propUpdateJustificationFactModel, headers);
            
            LOGGER.debug("Executing POST request on: " + endpointURL);
            response = compliancevalidationServiceClient.exchange(endpointURL.toString(), HttpMethod.POST, httpEntity, ComplianceResults.class);
           
		} catch (Exception e) {
			throw new CommonUtilException(e);
		}
		return response != null ? response.getBody().getMessages() : new ArrayList<PSMMessage>(); 
	}
	
}
