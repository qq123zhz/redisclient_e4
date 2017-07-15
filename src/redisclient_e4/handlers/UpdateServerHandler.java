package redisclient_e4.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TreeItem;

import com.cxy.redisclient.domain.Server;
import com.cxy.redisclient.presentation.server.UpdateServerDialog;

import redisclient_e4.models.RedisTreeNode;
/**
 * @author Administrator
 *
 */
public class UpdateServerHandler extends AbstractRedisHandler {
	
	
	 MPart activePart;

	
	@Execute
	public void execute(Shell shell,@Active MPart activePart ,
			@Optional @Named(IServiceConstants.ACTIVE_SELECTION) RedisTreeNode treeNode  ){
		super.execute(shell);
		this.activePart = activePart;
		Server server =  (Server) treeNode.getValue();
		int id = server.getId();
		 
		UpdateServerDialog dialog = new UpdateServerDialog(shell, iconImage,	server);
		server = (Server) dialog.open();
		if (server != null) {
			service1.update(id, server.getName(), server.getHost(),	server.getPort(), server.getPassword());
			
		}
	}
	
}
