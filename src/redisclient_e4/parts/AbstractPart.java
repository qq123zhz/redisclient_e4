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

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.e4.ui.services.internal.events.EventBroker;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.cxy.redisclient.service.NodeService;
import com.cxy.redisclient.service.ServerService;

public abstract class AbstractPart {
	@Inject private ESelectionService selectionService;
	@Inject private EMenuService menuService;
	@Inject  protected ServerService service1;
	@Inject  protected NodeService service2;
	@Inject protected ECommandService commandService;
	@Inject protected EHandlerService handlerService;
	@Inject protected EventBroker broker;
	@Inject protected Shell shell;
	

	@PostConstruct
	public void createComposite(Composite parent) { 
		
	}
	
	public void openError(String msg) {
		 MessageDialog.openError(shell, "error", msg);
	}
	
	/**
	 * @param columnViewer
	 */
	protected void setSelection(ColumnViewer columnViewer) {
		columnViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
                IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                // set the selection to the service
                selectionService.setSelection(
                    selection.size() == 1 ? selection.getFirstElement() : selection.toArray());
              }
		});

	}
	
	/**
	 * 添加菜单
	 */
	protected void registerContextMenu(Control control,String menuId ) {
		menuService.registerContextMenu(control, menuId );

	}

	@Focus
	public void setFocus() {
	}

}