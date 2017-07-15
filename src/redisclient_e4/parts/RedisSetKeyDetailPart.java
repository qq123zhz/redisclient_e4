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

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.cxy.redisclient.integration.I18nFile;
import com.cxy.redisclient.presentation.RedisClient;
import com.cxy.redisclient.presentation.WatchDialog;
import com.cxy.redisclient.presentation.component.EditListener;
import com.cxy.redisclient.presentation.component.PagingListener;
import com.cxy.redisclient.presentation.list.Status;
import com.cxy.redisclient.presentation.set.SetPage;
import com.cxy.redisclient.presentation.string.StringDataContent;
import com.cxy.redisclient.service.SetService;

import redisclient_e4.models.KeysElement;

public class RedisSetKeyDetailPart extends AbstractKeyDetailPart{

	private Table table;
	private Button btnDelete;
	private PagingListener pageListener;
	private SetService service = new SetService();
	private Group grpValues;
	private Button btnAdd;
	private Button btnRefresh;
	private Button btnWatch;
	private TableColumn tblclmnNewColumn;

	@Inject
	private MDirtyable dirty;
	
	
	private KeysElement  element;
	
	public void setElement(KeysElement element) {
		this.element = element;
		this.id = element.getServerId();
		this.db = element.getDbId();
		this.key = element.getName();
		
		pageListener = new PagingListener(table, new SetPage(id, db, key));
		table.addListener(SWT.SetData, pageListener);
	}
	public RedisSetKeyDetailPart() {
		
	}
	
	@PostConstruct
	public void createComposite(Composite dataComposite) {
		dataComposite.setLayout(new GridLayout(1, false));
//		
		grpValues = new Group(dataComposite, SWT.NONE);
		grpValues.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4,
				1));
		grpValues.setText(RedisClient.i18nFile.getText(I18nFile.VALUES));
		grpValues.setLayout(new GridLayout(4, false));

		table = new Table(grpValues, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI | SWT.VIRTUAL);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 4));
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tableItemSelected();
			}
		});
		table.setLinesVisible(true);
		table.addListener(SWT.MouseDown, new EditListener(table, false));
//		pageListener = new PagingListener(table, new SetPage(id, db, key));
//		table.addListener(SWT.SetData, pageListener);
		
		tblclmnNewColumn = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn.setText(RedisClient.i18nFile.getText(I18nFile.VALUE));
		tblclmnNewColumn.setWidth(200);

		btnAdd = new Button(grpValues, SWT.NONE);
		btnAdd.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnAdd.addSelectionListener(new SelectionAdapter() {
			

			@Override
			public void widgetSelected(SelectionEvent e) {
				InputDialog inputDialog = new InputDialog(shell,
						RedisClient.i18nFile.getText(I18nFile.INPUTVALUES),
						RedisClient.i18nFile.getText(I18nFile.LISTINPUTFORMAT), "", null);
				if (inputDialog.open() == InputDialog.OK) {
					String values = inputDialog.getValue();
					String[] setValues = values.split(";");
					long size = service.addValues(id, db, key, setValues);
					if(size == 0)
						MessageDialog.openInformation(shell, RedisClient.i18nFile.getText(I18nFile.INPUTVALUES), RedisClient.i18nFile.getText(I18nFile.ADDSETVALUES));
					refresh();
				}
			}
		});
		btnAdd.setText(RedisClient.i18nFile.getText(I18nFile.ADD));

		btnDelete = new Button(grpValues, SWT.NONE);
		btnDelete.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false,
				1, 1));
		btnDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Set<String> values = new HashSet<String>();
				
				TableItem[] items = table.getSelection();
				for (TableItem item : items) {
					values.add(item.getText());
				}
				service.remove(id, db, key, values);
				for (TableItem item : items) {
					item.dispose();
				}
				tableItemSelected();
				
			}
		});
		btnDelete.setEnabled(false);
		btnDelete.setText(RedisClient.i18nFile.getText(I18nFile.DELETE));
		
		btnRefresh = new Button(grpValues, SWT.NONE);
		btnRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				refresh();
			}
		});
		btnRefresh.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnRefresh.setText(RedisClient.i18nFile.getText(I18nFile.REFRESH));
		
		btnWatch = new Button(grpValues, SWT.NONE);
		btnWatch.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnWatch.setText(RedisClient.i18nFile.getText(I18nFile.WATCH));
		btnWatch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem[] items = table.getSelection();
				if(items==null||items.length==0)	return;
				
				WatchDialog dialog = new WatchDialog(shell, null, items[0].getText());
				dialog.open();
			}
		});
	

	}
	protected void tableItemSelected() {
		TableItem[] items = table.getSelection();
		if (items != null && items.length >= 1) {
			btnDelete.setEnabled(true);
			btnWatch.setEnabled(true);
		} else {
			btnDelete.setEnabled(false);
			btnWatch.setEnabled(false);
		}
	}
	public void refreshLangUI() {
		grpValues.setText(RedisClient.i18nFile.getText(I18nFile.VALUES));
		tblclmnNewColumn.setText(RedisClient.i18nFile.getText(I18nFile.VALUE));
		btnAdd.setText(RedisClient.i18nFile.getText(I18nFile.ADD));
		btnDelete.setText(RedisClient.i18nFile.getText(I18nFile.DELETE));
		btnRefresh.setText(RedisClient.i18nFile.getText(I18nFile.REFRESH));
		btnWatch.setText(RedisClient.i18nFile.getText(I18nFile.WATCH));
	}


	private void refresh() {
		pageListener.setCount();
		table.clearAll();
		table.setSelection(-1);
		tableItemSelected();
	}

	@Focus
	public void setFocus() {
	}

	@Persist
	public void save() {
		dirty.setDirty(false);
	}
}