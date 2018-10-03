package gov.nsf.psm.compliancevalidation.conversion.utility;

public class ComplianceConstants {

    // PDF Constants
	//public static final Integer FILE_NAME_MAX_CHARS_LIMIT = 260;
  //  public static final Long FILE_SIZE_MAX_MB = 10L;
   // public static final Double FONT_SIZE_MIN_ARIAL = 10.0d;
  //  public static final Double FONT_SIZE_MIN_COURIER_NEW = 10.0d;
  //  public static final Double FONT_SIZE_MIN_PALATINO_LINOTYPE = 10.0d;
//    public static final Double FONT_SIZE_MIN_SYMBOL = 11.0d;
//    public static final Double FONT_SIZE_MIN_TIMES_ROMAN = 11.0d;
    public static final Double FONT_SIZE_MIN_DEFAULT = 11.0d;
//    public static final Double LINE_SPACING_MIN = 12.0d;
//    public static final Double TOLERANCE_ADJ_FACTOR_INCHES = 0.02d;
//    public static final Integer INCH_TO_POINTS = 72;
//    public static final Integer MARGIN_MIN = INCH_TO_POINTS;
//    public static final Integer HEADING_LOWER_BOUNDARY = 3;
//    public static final Integer HEADING_UPPER_BOUNDARY = 3;
    public static final String HEADING_PROJECT_OVERVIEW_TEXT = "Overview";
    public static final String HEADING_INTELLECTUAL_MERIT_TEXT = "Intellectual Merit";
    public static final String HEADING_BROADER_IMPACTS_TEXT = "Broader Impacts";
    public static final String HEADING_PROFESSIONAL_PREPARATION_TEXT = "Professional Preparation";
    public static final String HEADING_APPOINTMENTS_TEXT = "Appointments";
    public static final String HEADING_PRODUCTS_TEXT = "Products";
    public static final String HEADING_PUBLICATIONS_TEXT = "Publications";
    public static final String HEADING_SYNERGISTIC_ACTIVITIES_TEXT = "Synergistic Activities";
    public static final String HEADING_OR_SEPARATOR = " or ";
    public static final String MIMETYPE_PDF = "application/pdf";
    public static final String PROJSUMM_AGENDAGROUP = "ProjectSummary_AgendaGroup" ;
    public static final String PDFUPLOAD_AGENDAGROUP = "PDFUpload_AgendaGroup" ;
    public static final String PROJDESC_AGENDAGROUP= "ProjDesc_AgendaGroup" ; 
    public static final String BIOSKETCHES_AGENDAGROUP= "BioSketches_AgendaGroup" ;
    public static final String BUDGJUST_AGENDAGROUP= "BudgJust_AgendaGroup" ;
    public static final String MENTPLAN_AGENDAGROUP= "MentoringPlan_AgendaGroup" ;
    public static final String DATAMGMTPLAN_AGENDAGROUP= "DataMgmtPlan_AgendaGroup" ;
    public static final String PROPOSALUPDATEJUST_AGENDAGROUP= "PUJ_AgendaGroup" ;
    public static final String COA_AGENDAGROUP= "COA_AgendaGroup" ;
    public static final String OSD_AGENDAGROUP= "OSD_AgendaGroup" ;
    public static final String SECTIONEXISTS_AGENDAGROUP= "SectionExists_AgendaGroup" ;
    public static final String BUDGET_AGENDAGROUP= "Budget_AgendaGroup" ;
    public static final String COVERSHEET_AGENDAGROUP= "CoverSheet_AgendaGroup" ;
    
    public static final String NUMBER_OF_RULES_FIRED="noOfRulesFired";
    public static final String COMPLAINCE_RESULTS_FIRED="compResultsFired";
    
    public static final String GET_ALL_FINDINGS_DOCUMET_UPLOAD_ERROR    = "Compliance Validation Service encountered an error while retreival of findings for Document upload";
    public static final String GET_ALL_FINDINGS_BUDGET_CHECK_ERROR    = "Compliance Validation Service encountered an error while retreival of findings for Budget";
    public static final String GET_ALL_FINDINGS_COVERSHEET_CHECK_ERROR    = "Compliance Validation Service encountered an error while retreival of findings for Coversheet";
    public static final String GET_ALL_FINDINGS_PROPOSAL_SECTIONS_CHECK_ERROR    = "Compliance Validation Service encountered an error while retreival of findings for Proposal Sections";
    public static final String GET_ALL_FINDINGS_PROPOSAL_UPDATE_JUSTIFICATION_CHECK_ERROR    = "Compliance Validation Service encountered an error while retreival of findings for Proposal Update Justifaction Section";

   
    private ComplianceConstants() {

    }

}
