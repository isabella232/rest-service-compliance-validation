package gov.nsf.psm.compliancevalidation.conversion.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;









import java.util.Map.Entry;

import org.drools.core.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;

import com.google.common.base.Joiner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.nsf.psm.factmodel.DocumentFactModel;
import gov.nsf.psm.factmodel.FontFactModel;
import gov.nsf.psm.factmodel.MarginFactModel;
import gov.nsf.psm.factmodel.PageFactModel;
import gov.nsf.psm.factmodel.SectionFactModel;
import gov.nsf.psm.foundation.model.Section;
import gov.nsf.psm.foundation.model.UploadableProposalSection;

public class DocumentFactModelConversionUtils {
    


   
    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentFactModelConversionUtils.class);
    
    private static final String SIZE_MEASURE = "pt";
    private static final String COMPLIANT_STR = "is compliant";
    private static final String NON_COMPLIANT_STR = "is not compliant";
    private static Map<String, String> complianceDocumentMap =new HashMap<String, String>();
	private static Map <String, Double> fontFamilyMap = new HashMap<String, Double>(); 
    
    private DocumentFactModelConversionUtils() {}
    

    
    public static boolean leadingIsCompliant(double leading) {
        
    	double lineSpacingMin = Double.parseDouble(complianceDocumentMap.get("LINE_SPACING_MIN"));
    	return leading >= lineSpacingMin;
    }

    public static boolean marginLeftIsCompliant(double margin) {
        
    	double marginMin = Double.parseDouble(complianceDocumentMap.get("MARGIN_MIN"));
    	return DocCompUtils.roundComplianceValue(margin) >= getPointSizeAdjustmentMargin(marginMin, false);
    }

    public static boolean marginRightIsCompliant(double margin) {
    	double marginMin = Double.parseDouble(complianceDocumentMap.get("MARGIN_MIN"));
    	return DocCompUtils.roundComplianceValue(margin) >= getPointSizeAdjustmentMargin(marginMin, false);
    }

    public static boolean marginTopIsCompliant(double margin) {
     	double marginMin = Double.parseDouble(complianceDocumentMap.get("MARGIN_MIN"));
         return DocCompUtils.roundComplianceValue(margin) >= getPointSizeAdjustmentMargin(marginMin, false);
    }

    public static boolean marginBottomIsCompliant(double margin) {
    	double marginMin = Double.parseDouble(complianceDocumentMap.get("MARGIN_MIN"));
    	return DocCompUtils.roundComplianceValue(margin) >= getPointSizeAdjustmentMargin(marginMin, false);
    }


    
    public static boolean fontNameIsCompliant(String type) {
        boolean isCompliant = false;
        for (Entry<String, Double> entry : fontFamilyMap.entrySet()) {
            if (type.indexOf(entry.getKey()) > -1) {
                LOGGER.debug("Compliant font family: " + type);
                isCompliant = true;
            }
        }
        if (!isCompliant) {
            LOGGER.debug("Non-compliant font family: " + type);
        }
        return isCompliant;
    }
    
   

    public static boolean fontSizeIsCompliant(String type, double size) {
        
        boolean isCompliant = false;
        Double roundedPointSize = DocCompUtils.roundComplianceValue(size);
      
        for (Entry<String, Double> fontEntry : fontFamilyMap.entrySet()) {
            if(type.indexOf(fontEntry.getKey()) > -1 
                 && (roundedPointSize >= getPointSizeAdjustment(fontEntry.getValue(), false))) {
                     isCompliant = true;
            }
        }
       
        if (!isCompliant) {
        	
        	Double fontSizeMinDefault = new Double(complianceDocumentMap.get("FONT_SIZE_DEFAULT"));
            if (roundedPointSize >= fontSizeMinDefault) {
                LOGGER.debug("Compliant font size: " + roundedPointSize);
                isCompliant = true;
            } else {
                LOGGER.debug("Non-compliant font size: " + roundedPointSize);
            }
        } else {
            LOGGER.debug("Compliant font size: " + roundedPointSize);
        }

        return isCompliant;
    }
    
  
    
    public static List<String> getLeadingCompliancePages(DocumentFactModel model) {
        List<String> leadingCompliancePages = new ArrayList<>();
        LOGGER.debug("");
        LOGGER.debug("---------------- Checking line spacing (leading) ----------------");
        for(PageFactModel pageFactModel : model.getPages()) {
            if (pageFactModel.getTextLines().size() > 1) {
                Double leading = pageFactModel.getLeading().get(0);
                LOGGER.debug("Leading: " + leading + SIZE_MEASURE);
                if (!DocumentFactModelConversionUtils.leadingIsCompliant(leading)) { // Leading
                    LOGGER.debug("Leading " + NON_COMPLIANT_STR);
                    leadingCompliancePages.add(String.valueOf(pageFactModel.getPageNumber()));
                } else {
                    LOGGER.debug("Leading " + COMPLIANT_STR);
                }
            } else {
                LOGGER.debug("Document has only one line of text");
            }
        }
        return leadingCompliancePages;
    }
    
    public static Set<DocumentHeadingEnum> getCompliantHeadings(DocumentHeadingEnum[] headings, DocumentFactModel docFactModel) {
        Set<DocumentHeadingEnum> headingCompliancePages = new LinkedHashSet<>();
        if (!ObjectUtils.isEmpty(headings)) {
            List<DocumentHeadingEnum> pageCompliantHeadingList = new ArrayList<>();
            for (DocumentHeadingEnum subHeadingEnum : headings) {
                if (subHeadingEnum.value().indexOf(ComplianceConstants.HEADING_OR_SEPARATOR) > -1) {
                    String[] headingSelections = subHeadingEnum.value().split(ComplianceConstants.HEADING_OR_SEPARATOR);
                    pageCompliantHeadingList.addAll(getCompliantHeadingList(subHeadingEnum, headingSelections, docFactModel));
                } else {
                    pageCompliantHeadingList.addAll(getCompliantHeadingList(subHeadingEnum, docFactModel));
                }
            }
            Set<DocumentHeadingEnum> s1 = new HashSet<>(pageCompliantHeadingList);
            headingCompliancePages.addAll(s1);
        }
        return headingCompliancePages;
        
    }
    
    public static List<String> checkDocumentHeadings(Set<DocumentHeadingEnum> headingCompliancePages, DocumentHeadingEnum[] headings) {
        LOGGER.debug("");
        LOGGER.debug("---------------- Checking headings ----------------");
        List<String> missingHeadings = new ArrayList<>();
        List<DocumentHeadingEnum> compliantHeadingList = Arrays.asList(headings);
        Set<DocumentHeadingEnum> s1 = new HashSet<DocumentHeadingEnum>(compliantHeadingList);
        s1.removeAll(headingCompliancePages);
        if (!s1.isEmpty()) {
            for (DocumentHeadingEnum missingHeader : s1) {
                missingHeadings.add(missingHeader.value());
            }
            LOGGER.debug(
                    "Document is missing the following heading(s): " + Joiner.on(", ").join(missingHeadings));
        } else {
            LOGGER.debug("Document has compliant headings");
        }
        Collections.sort(missingHeadings, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareTo(s2);
            }
        });
        return missingHeadings;
    }

    public static List<String> checkDocumentHeadingsForProjectSummary(Set<DocumentHeadingEnum> headingCompliancePages, DocumentHeadingEnum[] headings) {
        LOGGER.debug("");
        LOGGER.debug("---------------- Checking headings ----------------");
        List<String> missingHeadings = new ArrayList<>();
        List<DocumentHeadingEnum> compliantHeadingList = Arrays.asList(headings);
        Set<DocumentHeadingEnum> s1 = new HashSet<DocumentHeadingEnum>(compliantHeadingList);
        s1.removeAll(headingCompliancePages);
        if (!s1.isEmpty()) {
            for (DocumentHeadingEnum missingHeader : s1) {
                missingHeadings.add(missingHeader.value());
            }
            LOGGER.debug(
                    "Document is missing the following heading(s): " + Joiner.on(", ").join(missingHeadings));
        } else {
            LOGGER.debug("Document has compliant headings");
        }
        Collections.sort(missingHeadings, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s2.compareTo(s1);
            }
        });
        return missingHeadings;
    }
    private static List<DocumentHeadingEnum> getCompliantHeadingList(DocumentHeadingEnum subHeadingEnum, DocumentFactModel docModel) {
        List<DocumentHeadingEnum> pageCompliantHeadingList = new ArrayList<>();
        if(docModel.getSections() != null) {
            for (SectionFactModel section : docModel.getSections()) {
                String headingTxt = section.getHeading().getValue();
                boolean headingExists = getTargetHeadingExists(headingTxt, subHeadingEnum.value());
                if (headingExists) {
                    pageCompliantHeadingList.add(subHeadingEnum);
                }
            }
        }
        return pageCompliantHeadingList;
    }
    
    private static List<DocumentHeadingEnum> getCompliantHeadingList(DocumentHeadingEnum subHeadingEnum, String[] headingSelections, DocumentFactModel docModel) {
        List<DocumentHeadingEnum> pageCompliantHeadingList = new ArrayList<>();
        for (String headingSelection : headingSelections) {
            boolean checkOptionalHeading = false;
            for (SectionFactModel section : docModel.getSections()) {
                String headingTxt = section.getHeading().getValue();
                boolean headingExists = getTargetHeadingExists(headingTxt, headingSelection);
                if (headingExists && !checkOptionalHeading) {
                    pageCompliantHeadingList.add(subHeadingEnum);
                    checkOptionalHeading = true;
                }
            }
        }
        return pageCompliantHeadingList;
    }
    
    private static Double getPointSizeAdjustment(Double pointSize, boolean isAdditive) {
        Double adjVal = new Double (complianceDocumentMap.get("FONT_TOLERANCE_ADJ_FACTOR"));
        if(isAdditive) {
            return pointSize + adjVal;
        } else {
            return pointSize - adjVal;
        }
    }
    
    private static Double getPointSizeAdjustmentMargin(Double pointSize, boolean isAdditive) {
        Double adjVal = new Double (complianceDocumentMap.get("TOLERANCE_ADJ_FACTOR_INCHES")) * new Integer(complianceDocumentMap.get("MARGIN_MIN"));
        if(isAdditive) {
            return pointSize + adjVal;
        } else {
            return pointSize - adjVal;
        }
    }
    
    private static boolean getTargetHeadingExists(String src, String tgt) {
        boolean isTarget = false;
        String srcHeading = src.toUpperCase();
        String tgtHeading = tgt.toUpperCase();
        int idx = srcHeading.indexOf(tgtHeading);
        if(idx >= 0) {
            String leftChars = srcHeading.substring(0,idx).replaceAll(" ", "");
            String rightChars = srcHeading.substring(idx + tgtHeading.length(),srcHeading.length()).replaceAll(" ", "");
            isTarget = leftChars.length() <= new Integer(complianceDocumentMap.get("HEADING_LOWER_BOUNDARY")).intValue()
                && rightChars.length() <= new Integer(complianceDocumentMap.get("HEADING_UPPER_BOUNDARY")).intValue();
        }
        return isTarget;
    }
    
    public static DocumentFactModel convertDocumentFactModel(DocumentFactModel docFactModel, Map<String,String> compliancePropertiesMap) {
      	complianceDocumentMap=compliancePropertiesMap ;
    	
       	LOGGER.debug("Line Spacing Limit - " + complianceDocumentMap.get("LINE_SPACING_MIN"));
      	LOGGER.debug("Margin Min Size - " + complianceDocumentMap.get("MARGIN_MIN"));
      	LOGGER.debug("Tolerance Adj Factor - " + complianceDocumentMap.get("TOLERANCE_ADJ_FACTOR_INCHES"));
      	LOGGER.debug("Heading Lower Boundary - " + complianceDocumentMap.get("HEADING_LOWER_BOUNDARY"));
    	LOGGER.debug("Heading Upper Boundary - " + complianceDocumentMap.get("HEADING_UPPER_BOUNDARY"));

    	
    	//==================================================
    	String fontTypes = complianceDocumentMap.get("FONT_TYPES");
    	
		LOGGER.debug("Font Types String -"+fontTypes );
		String[] fontValues = fontTypes.split(",");
		for(String fontNameAndSize : fontValues)
		{
			LOGGER.debug("FontNameSize -"+ fontNameAndSize);
			
			String[] fontTypeNameSizeValues = fontNameAndSize.split("\\+");
			String fontName = fontTypeNameSizeValues[0];
			String fontSize = fontTypeNameSizeValues[1];
			fontFamilyMap.put(fontName, new Double(fontSize));
			
		}
		LOGGER.debug("Font FamilyMap"+fontFamilyMap.toString());
		
      	
        // Project Summary Headings
    	if(docFactModel != null)
    	{
         if(!StringUtils.isEmpty(docFactModel.getSectionName()) && docFactModel.getSectionName().equalsIgnoreCase(Section.PROJ_SUMM.getName())) 
         {
            Set<DocumentHeadingEnum> compliantHeadings = DocumentFactModelConversionUtils.getCompliantHeadings(DocumentTOCEnum.TOC_ONE.topHeadings()[0].subHeadings(), docFactModel);
            LOGGER.debug("Project Summary Headings -" + compliantHeadings);
            docFactModel.setMissingHeadings(DocumentFactModelConversionUtils.checkDocumentHeadingsForProjectSummary(compliantHeadings, DocumentTOCEnum.TOC_ONE.topHeadings()[0].subHeadings()));
         }
        // Project Description Headings
        if(!StringUtils.isEmpty(docFactModel.getSectionName()) && docFactModel.getSectionName().equalsIgnoreCase(Section.PROJ_DESC.getName())) {
            Set<DocumentHeadingEnum> compliantHeadings = DocumentFactModelConversionUtils.getCompliantHeadings(DocumentTOCEnum.TOC_TWO.topHeadings()[0].subHeadings(), docFactModel);
            docFactModel.setMissingHeadings(DocumentFactModelConversionUtils.checkDocumentHeadings(compliantHeadings, DocumentTOCEnum.TOC_TWO.topHeadings()[0].subHeadings()));
        }
        // Biographical Sketches Headings
        if(!StringUtils.isEmpty(docFactModel.getSectionName()) && docFactModel.getSectionName().equalsIgnoreCase(Section.BIOSKETCH.getName())) {
            Set<DocumentHeadingEnum> compliantHeadings = DocumentFactModelConversionUtils.getCompliantHeadings(DocumentTOCEnum.TOC_FOUR.topHeadings()[0].subHeadings(), docFactModel);
            docFactModel.setMissingHeadings(DocumentFactModelConversionUtils.checkDocumentHeadings(compliantHeadings, DocumentTOCEnum.TOC_FOUR.topHeadings()[0].subHeadings()));
        }
        
    	//URL Links
        List<String> urlLiksList = null;
        urlLiksList = docFactModel.getUrls();
        
		if (urlLiksList != null) {
			if (urlLiksList.size() > 1) {
				LOGGER.debug("URL Link list on the DocFactModel - " + urlLiksList.toString());
			}
		}
        if(docFactModel.getPages() != null) {
        	
        	boolean hasCompliantLeftMargins = true;
        	boolean hasCompliantRightMargins = true;
        	boolean hasCompliantBottomMargins = true;
        	boolean hasCompliantTopMargins = true;
        	
       	 	List<String> leadingNonCompliancePages = new ArrayList<>();
            
            List<PageFactModel> pages = new ArrayList<>();
    		for (PageFactModel pageFactModel : docFactModel.getPages()) {
    		    
    			LOGGER.debug("");
    			LOGGER.debug("================ Page no. " + pageFactModel.getPageNumber() + " ========");

    			
    			// Leading
    		    LOGGER.debug("");
    		    LOGGER.debug("---------------- Checking line spacing (leading) ----------------");
    			if ( pageFactModel.getTextLines()!=null  && pageFactModel.getTextLines().size() > 1) {
    	                Double leading = pageFactModel.getLeading().get(0);
    	                LOGGER.debug("Leading: " + leading + SIZE_MEASURE);
    	                if (!DocumentFactModelConversionUtils.leadingIsCompliant(leading)) { // Leading
    	                    LOGGER.debug("Leading " + NON_COMPLIANT_STR);
    	                    leadingNonCompliancePages.add(String.valueOf(pageFactModel.getPageNumber()));
    	                } else {
    	                    LOGGER.debug("Leading " + COMPLIANT_STR);
    	                }
    	            } else {
    	                LOGGER.debug("Document has only one line of text");
    	            }
   			

    			
    			MarginFactModel marginFactModel = pageFactModel.getMargin();
    			if(marginFactModel != null) {
	    			// Margin Compliance
	    			if(marginLeftIsCompliant(marginFactModel.getMarginLeft())) {
	    				LOGGER.debug("Compliant Left Margin - " + marginFactModel.getMarginLeft());
	    			}
	    			else {
	    				LOGGER.debug("Non-Compliant Left Margin - " + marginFactModel.getMarginLeft());
	    				hasCompliantLeftMargins = false;
	    			}
	    			if(marginRightIsCompliant(marginFactModel.getMarginRight())) {
	    				LOGGER.debug("Compliant Left Right - " + marginFactModel.getMarginRight());
	    			}
	    			else {
	    				LOGGER.debug("Non-Compliant Left Right - " + marginFactModel.getMarginRight());
	    				hasCompliantRightMargins = false;
	    			}
	    			if(marginTopIsCompliant(marginFactModel.getMarginTop())) {
	    				LOGGER.debug("Compliant Top Margin - " + marginFactModel.getMarginTop());
	    			}
	    			else {
	    				LOGGER.debug("Non-Compliant Top Margin - " + marginFactModel.getMarginTop());
	    				hasCompliantTopMargins = false;
	    			}
	    			if(marginBottomIsCompliant(marginFactModel.getMarginBottom())) {
	    				LOGGER.debug("Compliant Bottom Margin - " + marginFactModel.getMarginBottom());
	    			}
	    			else {
	    				LOGGER.debug("Non-Compliant Bottom Margin - " + marginFactModel.getMarginBottom());
	    				hasCompliantBottomMargins = false;
	    			}
    			}
    			
    			
    			// FontSize and Font types Compliance 
    			List<FontFactModel> fonts = new ArrayList<>();
    			
    		if(pageFactModel.getTextFonts() != null) {	
    			for (FontFactModel font : pageFactModel.getTextFonts()) {
    				LOGGER.debug("Font Name -  " + font.getName() + "-" +font.getSize());
    				if (fontSizeIsCompliant(font.getName(), font.getSize()))
    				{
    					LOGGER.debug("Compliant Font Size -" + font.getSize());
    					font.setFontSizeCompliant(true);
    				}
    				else
    				{
    					font.setFontSizeCompliant(false);
    					LOGGER.debug("Non Compliant Font Size -" + font.getSize());
    				}
    				if (fontNameIsCompliant(font.getName())) {
    					font.setFontNameCompliant(true);
    				} else {
    					font.setFontNameCompliant(false);
    				}
    				fonts.add(font);
    			}
    		}
			
			pageFactModel.setTextFonts(fonts);
    		pages.add(pageFactModel);
    
    		}
    		docFactModel.setNonCompliantLeadingPages(leadingNonCompliancePages);
    		
    	
    		//Set Margin findings
            docFactModel.setHasCompliantBottomMargins(hasCompliantBottomMargins);
            docFactModel.setHasCompliantLeftMargins(hasCompliantLeftMargins);
            docFactModel.setHasCompliantRightMargins(hasCompliantRightMargins);
            docFactModel.setHasCompliantTopMargins(hasCompliantTopMargins);
    		
            docFactModel.setPages(pages);
        	LOGGER.debug("Document Fact Model -"+docFactModel.toString());
    		
        }
    	}
		return docFactModel;
	}


}
