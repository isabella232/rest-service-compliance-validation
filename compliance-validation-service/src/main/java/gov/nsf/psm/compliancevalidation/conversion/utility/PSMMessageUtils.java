package gov.nsf.psm.compliancevalidation.conversion.utility;

import gov.nsf.psm.factmodel.PSMMessageType;

public class PSMMessageUtils {
    
    private PSMMessageUtils() {
        // Private Constructor
    }
    
    public static PSMMessageType getMessageType(String code) {
        PSMMessageType msgType;
        switch (code.charAt(code.indexOf("_")+1)) {
            case 'I':
                msgType = PSMMessageType.INFORMATION;
                break;
            case 'W':
                msgType = PSMMessageType.WARNING;
                break;
            default:
                msgType = PSMMessageType.ERROR;
                break;
        }
        return msgType;
    }

}
