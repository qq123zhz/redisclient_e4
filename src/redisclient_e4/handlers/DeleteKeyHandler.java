package redisclient_e4.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.widgets.Shell;

import redisclient_e4.models.KeysElement;
import redisclient_e4.utils.MyEventConstants;
import redisclient_e4.utils.PlatFormUtils;
/**
 * @author Administrator
 *
 */
public class DeleteKeyHandler extends AbstractRedisHandler {
	
	MPart activePart;
	
	@Execute
	public void execute(Shell shell,@Active MPart activePart,
			@Optional @Named(IServiceConstants.ACTIVE_SELECTION) KeysElement element){
		super.execute(shell);
		this.activePart = activePart;
		if(element==null)return;
		int id = element.getServerId();
		int db = element.getDbId();
		String key = element.getName();
		
		
		try {
			service2.deleteKey(id, db, key);
		} catch (Exception e) {
			e.printStackTrace();
		}
//		 PlatFormUtils.executeCommand(commandService, handlerService, RefreshKeysHandler.CMDID);
		broker.send(MyEventConstants.TOPIC_KEYS_REFRESH, element);
	}
	
}
