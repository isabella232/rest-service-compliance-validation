package gov.nsf.psm.compliancevalidation.config;

import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.StatelessKieSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


import gov.nsf.psm.compliancevalidation.service.ComplianceValidationService;
import gov.nsf.psm.compliancevalidation.service.ComplianceValidationServiceImpl;

@Configuration
@ComponentScan(basePackages = "org.kie.spring.annotations")
public class ComplianceValidationServiceConfig {
    
     
    @Bean
    public ComplianceValidationService ComplianceValidationService(){
        return new ComplianceValidationServiceImpl();
    }
    
     
    /**
     * By defining the {@link StatelessKieSession} as a bean here, we ensure that
     * Drools will hunt out the kmodule.xml and rules on application startup.
     * Those can be found in <code>src/main/resources</code>.
     * 
     * @return The {@link StatelessKieSession}.
     */
 
    	
 @Bean 
 public StatelessKieSession kieSession() 
 {
		KieServices services = KieServices.Factory.get();
		KieContainer kieContainer = services.getKieClasspathContainer();
		KieBase kbase = 	kieContainer.getKieBase("ComplianceKbase");
  	    return kbase.newStatelessKieSession();
    		
   }
}
