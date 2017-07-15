package redisclient_e4.utils;

import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.swt.widgets.Display;

public class PlatFormUtils {
	/**
     * @param runnable
     */
    public static void runOnSWTThread(Runnable runnable) {
        Display display = Display.getDefault();
        display.syncExec(runnable);
    }
	/**
     * @param commandService
     * @param handlerService
     * @param commandId
     */
    public static void executeCommand(ECommandService commandService,EHandlerService handlerService,String commandId) {
    	Command command = commandService.getCommand(commandId);

    	// check if the command is defined
    	System.out.println(command.isDefined());


    	// prepare execution of command
    	ParameterizedCommand cmd =
    	    commandService.createCommand(commandId, null);

    	// check if the command can get executed
    	if (handlerService.canExecute(cmd)){
    	    // execute the command
    	    handlerService.executeHandler(cmd);
    	}
	}
    /**
     * @param commandService
     * @param handlerService
     * @param commandId
     * @param paramMap
     */
    public static void executeCommand(ECommandService commandService,EHandlerService handlerService,String commandId,Map paramMap) {
    	Command command = commandService.getCommand(commandId);
    	
    	// check if the command is defined
    	System.out.println(command.isDefined());
    	
    	
    	// prepare execution of command
    	ParameterizedCommand cmd =
    			commandService.createCommand(commandId, paramMap);
    	
    	// check if the command can get executed
    	if (handlerService.canExecute(cmd)){
    		// execute the command
    		handlerService.executeHandler(cmd);
    	}
    }

}
