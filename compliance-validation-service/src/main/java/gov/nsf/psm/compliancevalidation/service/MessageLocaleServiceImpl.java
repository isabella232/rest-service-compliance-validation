package gov.nsf.psm.compliancevalidation.service;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import gov.nsf.psm.compliancevalidation.conversion.utility.PSMMessageUtils;
import gov.nsf.psm.factmodel.PSMMessage;

@Component
public class MessageLocaleServiceImpl implements MessageLocaleService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MessageLocaleServiceImpl.class);
	
    @Autowired
    private MessageSource messageSource;

    @Override
    public PSMMessage getMessage(Enum<?> msgEnum) {
        Locale locale = LocaleContextHolder.getLocale();
        String code = msgEnum.toString();
        String msgDesc = messageSource.getMessage(code,null,locale);
        PSMMessage message = new PSMMessage();
        LOGGER.debug("Code-" + code);
        LOGGER.debug("msgDesc -" + msgDesc);
        LOGGER.debug("Code Type -" + PSMMessageUtils.getMessageType(code));
        
        
        message.setId(code);
        message.setType(PSMMessageUtils.getMessageType(code));
        message.setDescription(msgDesc);
        return message;
    }
    
    @Override
    public PSMMessage getMessage(Enum<?> msgEnum, String p1) {
        Locale locale = LocaleContextHolder.getLocale();
        String code = msgEnum.toString();
        String msgDesc = messageSource.getMessage(code,null,locale);
        PSMMessage message = new PSMMessage();
        message.setId(code);
        message.setType(PSMMessageUtils.getMessageType(code));
        message.setDescription(MessageFormat.format(msgDesc, StringUtils.isEmpty(p1) ? "" : p1));
       
        LOGGER.debug("Code-" + code);
        LOGGER.debug("msgDesc -" + msgDesc);
        LOGGER.debug("Code Type -" + PSMMessageUtils.getMessageType(code));
        return message;
    }
    
    @Override
    public PSMMessage getMessage(Enum<?> msgEnum, String p1, String p2) {
        Locale locale = LocaleContextHolder.getLocale();
        String code = msgEnum.toString();
        String msgDesc = messageSource.getMessage(code,null,locale);
        PSMMessage message = new PSMMessage();
        message.setId(code);
        message.setType(PSMMessageUtils.getMessageType(code));
        message.setDescription(MessageFormat.format(msgDesc, StringUtils.isEmpty(p1) ? "" : p1,
            StringUtils.isEmpty(p2) ? "" : p2));
        return message;
    }
    
    @Override
    public PSMMessage getMessage(Enum<?> msgEnum, List<String> parameters) {
    	Locale locale = LocaleContextHolder.getLocale();
    	LOGGER.debug("Parameters List-" + parameters.toString());
        String code = msgEnum.toString();
        String msgDesc = messageSource.getMessage(code,parameters.toArray(),locale);
        LOGGER.debug("msgDesc -" + msgDesc);
        PSMMessage message = new PSMMessage();
        message.setId(code);
        message.setType(PSMMessageUtils.getMessageType(code));
        message.setDescription(msgDesc);
        LOGGER.debug("Code-" + code);
        LOGGER.debug("msgDesc -" + msgDesc);
        LOGGER.debug("Code Type -" + PSMMessageUtils.getMessageType(code));
     
        return message;
    }
    
	private void sortMessages(List<String> parameters) {
		Collections.sort(parameters, new Comparator<String>() {
			@Override
			public int compare(String a, String b) {
				String[] as = a.split(",");
				String[] bs = b.split(",");
				int result = Integer.valueOf(as[0]).compareTo(Integer.valueOf(bs[0]));
				if (result == 0)
					result = Integer.valueOf(as[1]).compareTo(Integer.valueOf(bs[1]));
				return result;
			} 
		}
		);
    }
   

}
