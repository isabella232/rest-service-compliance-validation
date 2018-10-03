package gov.nsf.psm.compliancevalidation.commandExecutor;

import gov.nsf.psm.compliancevalidation.conversion.utility.ComplianceConstants;
import gov.nsf.psm.factmodel.ComplianceResults;
import gov.nsf.psm.factmodel.CoverSheetFactModel;
import java.util.ArrayList;
import java.util.List;
import org.kie.api.KieServices;
import org.kie.api.command.Command;
import org.kie.api.command.KieCommands;
import org.kie.api.runtime.ExecutionResults;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.internal.command.CommandFactory;

public class CoverSheetCheckCommandExecutor {
	
	private CoverSheetCheckCommandExecutor(){
		
	}
	public static ExecutionResults executeBusinessRules(CoverSheetFactModel covrSheetFactModel, ComplianceResults compResultsFired, StatelessKieSession kieSession)

	{
		KieServices service = KieServices.Factory.get();
		// Retrieve the object responsible for Command creation
		KieCommands commandFactory = service.getCommands();

		// Define a list of commands to be executed
		List<Command> commands = new ArrayList<Command>();

		// Create an insert command and add it to the list
		Command cmdFactModel = commandFactory.newInsert(covrSheetFactModel, "covrSheetFactModel");
		commands.add(cmdFactModel);

		Command cmdComplianceResults = commandFactory.newInsert(compResultsFired, ComplianceConstants.COMPLAINCE_RESULTS_FIRED);
		commands.add(cmdComplianceResults);
		
		
		Command cmdAgendaGrp = commandFactory.newAgendaGroupSetFocus(ComplianceConstants.COVERSHEET_AGENDAGROUP);
		commands.add(cmdAgendaGrp);
		
		
		// Create a fireAllRules command to execute it
		Command fireAll = commandFactory.newFireAllRules(ComplianceConstants.NUMBER_OF_RULES_FIRED);
		commands.add(fireAll);
				
		// Execute Commands
		return kieSession.execute(CommandFactory.newBatchExecution(commands));

	}

}
