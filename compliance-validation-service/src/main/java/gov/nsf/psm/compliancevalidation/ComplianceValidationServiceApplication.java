package gov.nsf.psm.compliancevalidation;

import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@SpringBootApplication
public class ComplianceValidationServiceApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ComplianceValidationServiceApplication.class);
    }
    
    public static void main(String[] args) {
        setEmbeddedContainerEnvironmentProperties();
        SpringApplication.run(ComplianceValidationServiceApplication.class, args);
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        setExternalContainerEnvironmentProperties();
        super.onStartup(servletContext);
    }

    private static void setEmbeddedContainerEnvironmentProperties() {
       setEnvironmentProperties();
       System.setProperty("server.context-path", "/compliance-validation-service");
    }

    private static void setExternalContainerEnvironmentProperties() {
      setEnvironmentProperties();
    }

    private static void setEnvironmentProperties() {
      System.setProperty("spring.config.name", "compliance-validation-service");
    }
    
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.US);
        return slr;
    }
    
    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:locale/messages");
        messageSource.setCacheSeconds(3600); //refresh cache once per hour
        return messageSource;
    }

}