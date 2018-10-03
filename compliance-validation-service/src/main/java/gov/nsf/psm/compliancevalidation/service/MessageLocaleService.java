package gov.nsf.psm.compliancevalidation.service;

import java.util.List;

import gov.nsf.psm.factmodel.PSMMessage;

public interface MessageLocaleService {

    public PSMMessage getMessage(Enum<?> msgEnum);
    public PSMMessage getMessage(Enum<?> msgEnum, String p1);
    public PSMMessage getMessage(Enum<?> msgEnum, String p1, String p2);
    public PSMMessage getMessage(Enum<?> msgEnum, List<String> parameters);
    
}
