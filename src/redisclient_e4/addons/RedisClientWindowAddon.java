package redisclient_e4.addons;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.modeling.IWindowCloseHandler;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import redisclient_e4.handlers.RefreshDbHandler;
import redisclient_e4.handlers.RefreshKeysHandler;
import redisclient_e4.models.Element;
import redisclient_e4.models.RedisTreeNode;
import redisclient_e4.utils.MyEventConstants;
import redisclient_e4.utils.PlatFormUtils;

public class RedisClientWindowAddon {
	
	@Inject private ECommandService commandService;
	@Inject  private EHandlerService handlerService;

	@PostConstruct
	 public void init(IEclipseContext context) {
		
		
	}
	@Inject
	@Optional
	private void subscribeKeyRefreshEvent(@Optional @UIEventTopic(MyEventConstants.TOPIC_KEYS_REFRESH)Element element){
		if(element==null)return;
		PlatFormUtils.executeCommand(commandService, handlerService, RefreshKeysHandler.CMDID);
	}
	@Inject
	@Optional
	private void subscribeDbRefreshEvent(@Optional  @UIEventTopic(MyEventConstants.TOPIC_DBS_REFRESH)RedisTreeNode element){
		if(element==null)return;
		PlatFormUtils.executeCommand(commandService, handlerService, RefreshDbHandler.CMDID);
	}
	
	
	@Inject
	@Optional
	private void subscribeApplicationCompleted(@UIEventTopic(UIEvents.UILifeCycle.APP_STARTUP_COMPLETE) MApplication application,
			 IWorkbench workbench){
		 WindowCloseHandler closeHandler=new WindowCloseHandler();
		    MWindow window = application.getChildren().get(0);
		    window.getContext().set(IWindowCloseHandler.class, closeHandler);
	}
	
	private static class WindowCloseHandler implements IWindowCloseHandler{

	    @Override
	    public boolean close(MWindow window) {
	        Shell shell = new Shell();
	        if (MessageDialog.openConfirm(shell, "Confirmation",
	                "Do you want to exit?")) {
	            return true;
	        }
	        return false;
	    } 
	 }
	

}
