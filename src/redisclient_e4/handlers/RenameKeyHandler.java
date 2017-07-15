package redisclient_e4.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import com.cxy.redisclient.dto.RenameInfo;
import com.cxy.redisclient.integration.I18nFile;
import com.cxy.redisclient.presentation.key.RenameKeysDialog;

import redisclient_e4.models.Element;
import redisclient_e4.models.KeysElement;
import redisclient_e4.parts.RedisKeyListPart;
import redisclient_e4.utils.MyEventConstants;
import redisclient_e4.utils.PlatFormUtils;
/**
 * @author Administrator
 *
 */
public class RenameKeyHandler extends AbstractRedisHandler {
//	protected ServerService service1 = new ServerService();
//	@Inject  protected ServerService service1;
	
	MPart activePart;
	
	@Execute
	public void execute(Shell shell,@Active MPart activePart,
			@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Element element){
		super.execute(shell);
		this.activePart = activePart;
		boolean f = element instanceof KeysElement;
		if(!f)return;
		KeysElement keyElement = (KeysElement) element;
		String serverName = keyElement.getDbElement().getServer().getName();
		int  db = keyElement.getDbId();
		String key = keyElement.getName();
		int id = keyElement.getServerId();
		
		RenameKeysDialog dialog = new RenameKeysDialog(shell, iconImage,	serverName, db, key);
		RenameInfo rinfo = (RenameInfo) dialog.open();
		if (rinfo != null) {
			boolean result = false;
			try {
				result = service2.renameKey(id, db,	key, rinfo.getNewContainer(), rinfo.isOverwritten());
			} catch (Exception e) {
				e.printStackTrace();
				MessageDialog.openError(shell, "error", "error: "+e.getMessage());
				
			}

			if (!rinfo.isOverwritten() && !result) {
				String failString = i18nFile.getText(I18nFile.RENAMEKEYFAIL);
				MessageDialog.openError(shell,
						i18nFile.getText(I18nFile.RENAMEKEYRESULT), failString);return;
			}
			RedisKeyListPart part =  (RedisKeyListPart) activePart.getObject();
//			 PlatFormUtils.executeCommand(commandService, handlerService, RefreshKeysHandler.CMDID);
//			send event
			broker.send(MyEventConstants.TOPIC_KEYS_REFRESH, keyElement);
		}
		
	}
	
	/**
	 * 
	 */
}
