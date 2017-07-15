package redisclient_e4.handlers;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.commands.ParameterValueConversionException;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import com.cxy.redisclient.domain.NodeType;
import com.cxy.redisclient.domain.Server;
import com.cxy.redisclient.integration.I18nFile;

import redisclient_e4.handlers.parm.MyParameterConverter;
import redisclient_e4.models.KeysElement;
import redisclient_e4.models.RedisTreeNode;
import redisclient_e4.parts.RedisHashKeyDetailPart;
import redisclient_e4.parts.RedisListKeyDetailPart;
import redisclient_e4.parts.RedisSetKeyDetailPart;
import redisclient_e4.parts.RedisSortSetKeyDetailPart;
import redisclient_e4.parts.RedisStringKeyDetailPart;
/**
 * @author Administrator
 *
 */
public class RemoveServerHandler extends AbstractRedisHandler {
	
	
	 MPart activePart;

	
	@Execute
	public void execute(Shell shell,@Active MPart activePart,
			@Optional @Named(IServiceConstants.ACTIVE_SELECTION) RedisTreeNode treeNode){
		super.execute(shell);
		this.activePart = activePart;
		Server server = (Server) treeNode.getValue();
		boolean ok = MessageDialog.openConfirm(shell,
				i18nFile.getText(I18nFile.REMOVESERVER),
				i18nFile.getText(I18nFile.CONFIRMREMOVESERVER));
		if (ok) {
			service1.delete(server.getId());
		}
		 
	}
	
}
