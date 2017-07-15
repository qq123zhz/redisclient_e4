package redisclient_e4.handlers;

import java.util.Map;

import javax.inject.Named;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.widgets.Shell;

import com.cxy.redisclient.domain.Server;
import com.cxy.redisclient.presentation.server.PropertiesDialog;

import redisclient_e4.models.RedisTreeNode;
/**
 * @author Administrator
 *
 */
public class PropertyServerHandler extends AbstractRedisHandler {
	
	
	 MPart activePart;

	
	@Execute
	public void execute(Shell shell,@Active MPart activePart ,
			@Optional @Named(IServiceConstants.ACTIVE_SELECTION) RedisTreeNode treeNode){
		super.execute(shell);
		this.activePart = activePart;
		if(treeNode==null)return;
		
		Server server = (Server) treeNode.getValue();
		if(server ==null)return;
		Map<String, String[]> values = service1.listInfo(server.getId());

		PropertiesDialog dialog = new PropertiesDialog(shell, iconImage, server,		values);
		dialog.open();
		 
	}
	
}
