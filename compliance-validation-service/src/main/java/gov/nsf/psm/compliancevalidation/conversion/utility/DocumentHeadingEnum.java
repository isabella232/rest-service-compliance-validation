package gov.nsf.psm.compliancevalidation.conversion.utility;

import java.util.Arrays;
import java.util.Optional;

import com.google.common.base.Joiner;

import gov.nsf.psm.foundation.model.Section;
import gov.nsf.psm.factmodel.DocCompMessagesEnum;

public enum DocumentHeadingEnum {

    BUDGET_JUSTIFICATION(Section.BUDGET_JUST.getName(), 3L), 
    PROJECT_OVERVIEW(ComplianceConstants.HEADING_PROJECT_OVERVIEW_TEXT), 
    INTELLECTUAL_MERIT(ComplianceConstants.HEADING_INTELLECTUAL_MERIT_TEXT), 
    BROADER_IMPACTS(ComplianceConstants.HEADING_BROADER_IMPACTS_TEXT), 
    PROFESSIONAL_PREPARATION(ComplianceConstants.HEADING_PROFESSIONAL_PREPARATION_TEXT), 
    APPOINTMENTS(ComplianceConstants.HEADING_APPOINTMENTS_TEXT), 
    PRODUCTS(ComplianceConstants.HEADING_PRODUCTS_TEXT), 
    PUBLICATIONS(ComplianceConstants.HEADING_PUBLICATIONS_TEXT), 
    PRODUCTS_PUBLICATIONS(new String[] {DocumentHeadingEnum.PRODUCTS.value(),DocumentHeadingEnum.PUBLICATIONS.value()},
        ComplianceConstants.HEADING_OR_SEPARATOR,
        "Products (or as applicable, Publications)"), 
    SYNERGISTIC_ACTIVITIES(ComplianceConstants.HEADING_SYNERGISTIC_ACTIVITIES_TEXT), 
    PROJECT_SUMMARY(Section.PROJ_SUMM.getName(),1L,DocCompMessagesEnum.CV_E_0103,
        new DocumentHeadingEnum[] {DocumentHeadingEnum.PROJECT_OVERVIEW,DocumentHeadingEnum.INTELLECTUAL_MERIT,DocumentHeadingEnum.BROADER_IMPACTS}), 
    PROJECT_DESCRIPTION(Section.PROJ_DESC.getName(),15L,DocCompMessagesEnum.CV_W_0124,
        new DocumentHeadingEnum[] {DocumentHeadingEnum.BROADER_IMPACTS, DocumentHeadingEnum.INTELLECTUAL_MERIT }), 
    BIOSKETCH(Section.BIOSKETCH.getName(),2L,DocCompMessagesEnum.CV_W_0203,
        new DocumentHeadingEnum[] {DocumentHeadingEnum.PROFESSIONAL_PREPARATION,DocumentHeadingEnum.APPOINTMENTS,DocumentHeadingEnum.PRODUCTS_PUBLICATIONS,DocumentHeadingEnum.SYNERGISTIC_ACTIVITIES}),
    DMP(Section.DMP.getName(),2L),
    PMP(Section.PMP.getName(),1L);
    private final String value;
    private final String msgInsert;
    private final Long pageLimit;
    private final DocumentHeadingEnum[] subHeadings;
    private final DocCompMessagesEnum validationMsg;

    private DocumentHeadingEnum(String value) {
        this.value = value;
        this.pageLimit = null;
        this.subHeadings = null;
        this.validationMsg = null;
        this.msgInsert = null;
    }

    private DocumentHeadingEnum(String value, Long pageLimit) {
        this.value = value;
        this.pageLimit = pageLimit;
        this.subHeadings = null;
        this.validationMsg = null;
        this.msgInsert = null;
    }

    private DocumentHeadingEnum(String[] headingValues, String sepChar, String msgInsert) {
        this.value = Joiner.on(sepChar).join(headingValues);
        this.pageLimit = null;
        this.subHeadings = null;
        this.validationMsg = null;
        this.msgInsert = msgInsert;
    }

    private DocumentHeadingEnum(String value, Long pageLimit, DocCompMessagesEnum validationMsg,
            DocumentHeadingEnum[] subHeadings) {
        this.value = value;
        this.pageLimit = pageLimit;
        this.subHeadings = subHeadings;
        this.validationMsg = validationMsg;
        this.msgInsert = null;
    }

    public Long pageLimit() {
        return pageLimit;
    }

    public String value() {
        return value;
    }

    public String msgInsert() {
        return msgInsert;
    }

    public DocumentHeadingEnum[] subHeadings() {
        return Arrays.copyOf(subHeadings,subHeadings.length);
    }

    public DocCompMessagesEnum validationMsg() {
        return validationMsg;
    }

    public boolean hasSubHeadings() {
        return subHeadings != null;
    }

    public static Optional<DocumentHeadingEnum> findHeading(String value) {
        return Arrays.asList(DocumentHeadingEnum.values()).stream()
                .filter(DocumentHeadingEnum -> value.equalsIgnoreCase(DocumentHeadingEnum.value())).findFirst();
    }

}
