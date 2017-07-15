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

import java.util.List;

import javax.inject.Named;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Shell;

import com.cxy.redisclient.domain.NodeType;
import com.cxy.redisclient.domain.Server;

import redisclient_e4.models.DbElement;
import redisclient_e4.models.RedisTreeNode;
import redisclient_e4.parts.RedisExplorerPart;
import redisclient_e4.parts.RedisListKeyDetailPart;
/**
 * @author Administrator
 *
 */
public class RefreshDbHandler extends AbstractRedisHandler {
	
	public static final String CMDID = "redisclient_e4.command.refreshdb";
	MPart activePart;
	@Execute
	public void execute(Shell shell,			@Optional @Named(IServiceConstants.ACTIVE_SELECTION) RedisTreeNode node){
		super.execute(shell);
		this.activePart = partService.showPart(RedisExplorerPart.ID, PartState.ACTIVATE);
		RedisExplorerPart part = (RedisExplorerPart) activePart.getObject(); 
		if(node==null)return;
		List rootList = part.getRootList();
		TableViewer tableViewer = part.getTableViewer();
		
		
		Object object = node.getValue();
		if(object instanceof String)return;
		Server server = (Server) object;
		
		int amount = 0;
		try {
			amount = service1.listDBs(server.getId());
		} catch (Exception e) {
			e.printStackTrace();
			MessageDialog.openError(shell, "error", "open redis connect failed!");
			server.setOpen(false);
		}	
		server.setOpen(true);
		rootList.clear();
		for (int i = 0; i < amount; i++) {
			DbElement element = new DbElement();
			element.setServerId(server.getId());
			element.setDbId(i);
			element.setName(DB_PREFIX+i);
			element.setType(NodeType.DATABASE);
			element.setServer(server);
			rootList.add(element);
		}
		tableViewer.setInput(rootList);
		
	}
	
	 
}
