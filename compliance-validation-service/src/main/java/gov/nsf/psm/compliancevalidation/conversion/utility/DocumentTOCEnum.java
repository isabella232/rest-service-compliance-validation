package gov.nsf.psm.compliancevalidation.conversion.utility;

import org.apache.commons.lang.ObjectUtils;

public enum DocumentTOCEnum {

    TOC_ONE(new DocumentHeadingEnum[] { DocumentHeadingEnum.PROJECT_SUMMARY }), 
    TOC_TWO(new DocumentHeadingEnum[] { DocumentHeadingEnum.PROJECT_DESCRIPTION }), 
    TOC_THREE(new DocumentHeadingEnum[] { DocumentHeadingEnum.BUDGET_JUSTIFICATION }), 
    TOC_FOUR(new DocumentHeadingEnum[] { DocumentHeadingEnum.BIOSKETCH });

    private final DocumentHeadingEnum[] topHeadings;

    private DocumentTOCEnum(DocumentHeadingEnum[] topHeadings) {
        this.topHeadings = topHeadings;
    }

    public DocumentHeadingEnum[] topHeadings() {
        return (DocumentHeadingEnum[]) ObjectUtils.clone(topHeadings);
    }

}
