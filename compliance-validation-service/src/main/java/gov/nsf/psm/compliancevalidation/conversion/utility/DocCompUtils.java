package gov.nsf.psm.compliancevalidation.conversion.utility;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DocCompUtils {

    private DocCompUtils() {
    }
    
    public static Double roundComplianceValue(double size) {
        BigDecimal bd = BigDecimal.valueOf(size);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
