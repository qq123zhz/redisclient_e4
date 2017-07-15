package redisclient_e4.handlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.swt.widgets.Shell;

import com.cxy.redisclient.domain.Server;
import com.cxy.redisclient.presentation.server.AddServerDialog;

import redisclient_e4.models.RedisTreeNode;
import redisclient_e4.parts.RedisServerListPart;
/**
 * @author Administrator
 *
 */
public class AddServerHandler extends AbstractRedisHandler {
	
	static MPart activePart;
	
	@Execute
	public void execute(Shell shell,@Active MPart activePart){
		super.execute(shell);
		AddServerHandler.activePart = activePart;
		addServer();
	}
	
	/**
	 * 
	 */
	public  void addServer() {
		AddServerDialog dialog = new AddServerDialog(shell, iconImage);
		Server server = (Server) dialog.open();

		if (server != null) {
			server.setId(service1.add(server.getName(), server.getHost(),
					server.getPort(), server.getPassword()));
			
			 RedisServerListPart listPart =  (RedisServerListPart) activePart.getObject();
			 List<RedisTreeNode>rootList = listPart.getRootList();
			 RedisTreeNode rootNode = rootList.get(0);
			 RedisTreeNode[]childrenNodes = rootNode.getChildren();
			 List<RedisTreeNode>children = null;
			 if(childrenNodes==null||childrenNodes.length==0){
				 children= new ArrayList<>();
			 }else{
				 children= new ArrayList( Arrays.asList(rootNode.getChildren()));
			 }
			 RedisTreeNode serverNode = new RedisTreeNode(server);
			 children.add(serverNode);
			 serverNode.setParent(rootNode);
			 rootNode.setChildren(children.toArray(new RedisTreeNode[]{}));
			 
//			 listPart.getTreeViewer().setInput(listPart.getRootList().toArray(new TreeNode[]{}));
			 listPart.getTreeViewer().refresh();
			
			
		}

	}
}
