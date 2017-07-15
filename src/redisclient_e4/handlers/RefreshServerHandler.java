/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Lars Vogel <lars.Vogel@gmail.com> - Bug 419770
 *******************************************************************************/
package redisclient_e4.handlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.swt.widgets.Shell;

import com.cxy.redisclient.domain.Server;

import redisclient_e4.models.RedisTreeNode;
import redisclient_e4.parts.RedisServerListPart;
/**
 * @author Administrator
 *
 */
public class RefreshServerHandler extends AbstractRedisHandler {
//	 protected ServerService service1 = new ServerService();
//	@Inject  protected ServerService service1;
	public static final String CMDID = "redisclient_e4.command.refreshserver";
	
	MPart activePart;
	@Execute
	public void execute(Shell shell,@Active MPart activePart ){
		super.execute(shell);
		this.activePart = activePart;
		initServers();
		
	}
	/**
	 * 
	 */
	public  void initServers() {
		List<Server> servers = service1.listAll();
		RedisServerListPart listPart =   (RedisServerListPart) activePart.getObject();
		List<RedisTreeNode>rootList = listPart.getRootList();
		RedisTreeNode rootNode =  rootList.get(0);
		
		
		RedisTreeNode[]children = rootNode.getChildren();
		List<RedisTreeNode>childList = null;
		if(children==null){
			childList = new ArrayList<>();
		}else{
			childList = new ArrayList(Arrays.asList(children));
		}

		for (Server server : servers) {
			server.toString();
			RedisTreeNode treeNode =  new RedisTreeNode(server);
			if(childList.contains(treeNode))continue;
			childList.add(treeNode);
		}
		rootNode.setChildren(childList.toArray(new RedisTreeNode[]{}));
		listPart.getTreeViewer().refresh();
		listPart.getTreeViewer().expandAll();
		listPart.getTreeViewer().expandToLevel(-1);

	}
	
	 
}
