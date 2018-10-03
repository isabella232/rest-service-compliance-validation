package gov.nsf.psm.compliancevalidation.commandExecutor;

import gov.nsf.psm.compliancevalidation.conversion.utility.ComplianceConstants;
import gov.nsf.psm.factmodel.ComplianceResults;
import gov.nsf.psm.factmodel.DocumentFactModel;
import gov.nsf.psm.foundation.model.Section;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.drools.core.util.StringUtils;
import org.kie.api.KieServices;
import org.kie.api.command.Command;
import org.kie.api.command.KieCommands;
import org.kie.api.runtime.ExecutionResults;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.internal.command.CommandFactory;

public class PDFUploadCommandExecutor {

	private static final Logger LOGGER = LoggerFactory.getLogger(PDFUploadCommandExecutor.class);
	private PDFUploadCommandExecutor(){
	
	}

	public static ExecutionResults executeBusinessRules(DocumentFactModel docModel, ComplianceResults compResultsFired,
			StatelessKieSession kieSession)

	{
		KieServices service = KieServices.Factory.get();
		// Retrieve the object responsible for Command creation
		KieCommands commandFactory = service.getCommands();

		// Define a list of commands to be executed
		List<Command> commands = new ArrayList<Command>();

		// Create an insert command and add it to the list
		Command cmdFactModel = commandFactory.newInsert(docModel, "docModel");
		commands.add(cmdFactModel);

		Command cmdComplianceResults = commandFactory.newInsert(compResultsFired,
				ComplianceConstants.COMPLAINCE_RESULTS_FIRED);
		commands.add(cmdComplianceResults);

		List<String> agendaGroupList = determineAgendaGrouping(docModel);

		// Command for the inserting the Agenda Group in the List
		for (String agendaGroup : agendaGroupList) {
			LOGGER.debug("Agenda Group -" + agendaGroup);
			Command cmdAgendaGrp = commandFactory.newAgendaGroupSetFocus(agendaGroup);
			commands.add(cmdAgendaGrp);

		}

		// Create a fireAllRules command to execute it
		Command fireAll = commandFactory.newFireAllRules(ComplianceConstants.NUMBER_OF_RULES_FIRED);
		commands.add(fireAll);

		// Execute Commands
		return kieSession.execute(CommandFactory.newBatchExecution(commands));

	}

	public static List<String> determineAgendaGrouping(DocumentFactModel docFactModel) {

		List<String> agendaGroupList = new ArrayList<String>();

		agendaGroupList.add(ComplianceConstants.PDFUPLOAD_AGENDAGROUP);

		if (docFactModel != null && (!StringUtils.isEmpty(docFactModel.getSectionName()))){
				LOGGER.debug("Section Name - " + docFactModel.getSectionName());
				if (docFactModel.getSectionName().equalsIgnoreCase(Section.PROJ_SUMM.getName())) {
					agendaGroupList.add(ComplianceConstants.PROJSUMM_AGENDAGROUP);

				} else if (docFactModel.getSectionName().equalsIgnoreCase(Section.PROJ_DESC.getName())) {
					agendaGroupList.add(ComplianceConstants.PROJDESC_AGENDAGROUP);

				} else if (docFactModel.getSectionName().equalsIgnoreCase(Section.BUDGET_JUST.getName())) {
					agendaGroupList.add(ComplianceConstants.BUDGJUST_AGENDAGROUP);

				} else if (docFactModel.getSectionName().equalsIgnoreCase(Section.DMP.getName())) {
					agendaGroupList.add(ComplianceConstants.DATAMGMTPLAN_AGENDAGROUP);

				} else if (docFactModel.getSectionName().equalsIgnoreCase(Section.BIOSKETCH.getName())) {
					agendaGroupList.add(ComplianceConstants.BIOSKETCHES_AGENDAGROUP);

				} else if (docFactModel.getSectionName().equalsIgnoreCase(Section.PMP.getName())) {
					agendaGroupList.add(ComplianceConstants.MENTPLAN_AGENDAGROUP);

				}

				else if (docFactModel.getSectionName().equalsIgnoreCase(Section.OSD.getName())) {
					agendaGroupList.remove(ComplianceConstants.PDFUPLOAD_AGENDAGROUP);
					agendaGroupList.add(ComplianceConstants.OSD_AGENDAGROUP);

				}

				else if (docFactModel.getSectionName().equalsIgnoreCase(Section.COA.getName())) {
					agendaGroupList.remove(ComplianceConstants.PDFUPLOAD_AGENDAGROUP);
					agendaGroupList.add(ComplianceConstants.COA_AGENDAGROUP);

				}

				else {
					LOGGER.debug("Section Name - " + docFactModel.getSectionName());
				}
			}
		
		return agendaGroupList;
	}
}
