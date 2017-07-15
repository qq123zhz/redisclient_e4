/*******************************************************************************
 * Copyright (c) 2010 - 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Lars Vogel <lars.Vogel@gmail.com> - Bug 419770
 *******************************************************************************/
package redisclient_e4.parts;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.osgi.service.event.Event;

import com.cxy.redisclient.domain.RedisVersion;
import com.cxy.redisclient.domain.Server;

import redisclient_e4.handlers.RefreshServerHandler;
import redisclient_e4.models.RedisTreeNode;
import redisclient_e4.provider.NodeContentProvider;
import redisclient_e4.utils.ImageUtils;
import redisclient_e4.utils.MyEventConstants;
import redisclient_e4.utils.PlatFormUtils;

public class RedisServerListPart extends AbstractPart  {

	private TreeViewer treeViewer;
	private List<RedisTreeNode>rootList = new ArrayList<RedisTreeNode>();
	
//	@Inject EMenuService menuService;
//	@Inject ESelectionService selectionService;
	
	private String menuId = "redisclient_e4.popupmenu.server";
	@Inject private ECommandService commandService;
	@Inject private EHandlerService handlerService;
	
	public List<RedisTreeNode> getRootList() {
		return rootList;
	}
	public TreeViewer getTreeViewer() {
		return treeViewer;
	}
	
	public RedisServerListPart() {
		RedisTreeNode rootNode = new RedisTreeNode("Redis servers");
		List<RedisTreeNode>children = new ArrayList<>();
		rootNode.setChildren(children.toArray(new RedisTreeNode[]{}));
		rootList.add(rootNode);
		
	}

	@PostConstruct
	public void createComposite(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		treeViewer = new  TreeViewer(parent);
		Tree tree = treeViewer.getTree();
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		treeViewer.setContentProvider(new NodeContentProvider());
		treeViewer.setLabelProvider(new LabelProvider(){
			@Override
			public String getText(Object element) {
				RedisTreeNode node = (RedisTreeNode) element;
				Object object = node.getValue();
				if(object instanceof String)
					return object.toString();
				else {
					Server server = (Server) object;
					return server.getName();
					
				}
			}
			@Override
			public Image getImage(Object element) {
				RedisTreeNode node = (RedisTreeNode) element;
				Object object = node.getValue();
				if(object instanceof String)
					return ImageUtils.createImage(getClass(), "icons/server/server-2.gif");
				else {
					Server server = (Server) object;
					if(server.isOpen()){
						return ImageUtils.createImage(getClass(), "icons/server/server_start.gif");
					}
					return ImageUtils.createImage(getClass(), "icons/server/server_stop.gif");
					
				}
			}
		});
		
		treeViewer.setInput(rootList.toArray(new RedisTreeNode[]{}));
		treeViewer.expandAll();
		treeViewer.expandToLevel(-1);
		treeViewer.addDoubleClickListener(new IDoubleClickListener() {
			
			@Override
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				RedisTreeNode node =  (RedisTreeNode) selection.getFirstElement();
				
				broker.post(MyEventConstants.TOPIC_DBS_REFRESH, node);
				
			}
		} );
		setSelection(treeViewer);
		registerContextMenu(treeViewer.getControl(), menuId);
		
		 
	}

	@Focus
	public void setFocus() {
		treeViewer.getTree().setFocus();
		
	}
	 
	
	/**
	 * @param node
	 */
	@Inject
	@Optional
	public void getSelection(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) RedisTreeNode node) {
		if (node == null) {
			return;
		}
		Object object = node.getValue();
		boolean f = object instanceof String;
		if(f)return;
		Server server = (Server) object;
		RedisVersion version = null;
		try {
			version = service2.listServerVersion(server.getId());
		} catch (Exception e) {
			e.printStackTrace();
			openError("connect error:"+e.getLocalizedMessage());
		}
		if(version!=null){
			server.setOpen(true);
		}
		treeViewer.refresh(node);
		System.out.println(node.toString());
	}
	
	/**事件监听
	 * @param event
	 */
	@Inject
	@Optional
	public void appStartUpComplete(@UIEventTopic(UIEvents.UILifeCycle.APP_STARTUP_COMPLETE) Event event) {
		System.out.println(event);
		/*MPart part = (MPart) event.getProperty(UIEvents.EventTags.ELEMENT);
		if(part==null)return;
		if (part.getObject() == this) {
		}*/
		PlatFormUtils.executeCommand(commandService, handlerService, RefreshServerHandler.CMDID);
	}
	 

}