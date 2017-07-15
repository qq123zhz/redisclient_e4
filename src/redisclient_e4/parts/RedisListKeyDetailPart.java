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
import org.eclipse.swt.widgets.Text;

import com.cxy.redisclient.integration.I18nFile;
import com.cxy.redisclient.presentation.RedisClient;
import com.cxy.redisclient.presentation.WatchDialog;
import com.cxy.redisclient.presentation.component.EditListener;
import com.cxy.redisclient.presentation.component.PagingListener;
import com.cxy.redisclient.presentation.list.CurrentData;
import com.cxy.redisclient.presentation.list.ListPage;
import com.cxy.redisclient.presentation.list.Status;
import com.cxy.redisclient.presentation.string.StringDataContent;
import com.cxy.redisclient.service.ListService;

import redisclient_e4.models.KeysElement;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class RedisListKeyDetailPart {

	@Inject
	private MDirtyable dirty;
	@Inject
	private Shell shell;

	private Table table;
	private Button btnAppendTail;
	private Button btnDeleteHead;
	private Button btnDeleteTail;
	private ListService service = new ListService();
	private Status status;
	private Button btnInsertHead;
	private Button btnApply;
	private Button btnCancel;
	private Button btnRefresh;
	private Button btnWatch;
	private CurrentData currentData = new CurrentData();
	private EditListener listener;
	private Text editor;
	private PagingListener pageListener;

	private Group grpValues;

	private TableColumn tblclmnNewColumn;
	
	private KeysElement  element;
	private int id;
	private int db;
	private String key;
	
	public void setElement(KeysElement element) {
		this.element = element;
		this.id = element.getServerId();
		this.db = element.getDbId();
		this.key = element.getName();
		
		pageListener = new PagingListener(table, new ListPage(id, db, key));
		table.addListener(SWT.SetData, pageListener);
	}
	public RedisListKeyDetailPart() {
		status = Status.Normal;
	}
	
	@PostConstruct
	public void createComposite(Composite dataComposite) {
		dataComposite.setLayout(new GridLayout(1, false));

		grpValues = new Group(dataComposite, SWT.NONE);
		grpValues.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4,
				1));
		grpValues.setText(RedisClient.i18nFile.getText(I18nFile.VALUES));
		grpValues.setLayout(new GridLayout(4, false));

		table =  new Table(grpValues, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI | SWT.VIRTUAL);
		table.setHeaderVisible(true);
		
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 5));
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tableItemSelected();
			}
		});
		table.setLinesVisible(true);
		listener = new EditListener(table, true);
		table.addListener(SWT.MouseDown, listener);
//		pageListener = new PagingListener(table, new ListPage(id, db, key));
//		table.addListener(SWT.SetData, pageListener);

		tblclmnNewColumn = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn.setText(RedisClient.i18nFile.getText(I18nFile.VALUE));
		tblclmnNewColumn.setWidth(200);

		btnInsertHead = new Button(grpValues, SWT.NONE);
		btnInsertHead.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnInsertHead.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				InputDialog dialog = new InputDialog(shell, RedisClient.i18nFile.getText(I18nFile.INSERTHEAD), RedisClient.i18nFile.getText(I18nFile.INPUTVALUES), "", null);
				if(dialog.open() == InputDialog.OK){
				    String value = dialog.getValue();
				    service.addHead(id, db, key, value);
				    refresh();
				}
			}
		});
		btnInsertHead.setText(RedisClient.i18nFile.getText(I18nFile.INSERTHEAD));

		btnAppendTail = new Button(grpValues, SWT.NONE);
		btnAppendTail.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false,
				1, 1));
		btnAppendTail.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				InputDialog dialog = new InputDialog(shell, RedisClient.i18nFile.getText(I18nFile.APPENDTAIL), RedisClient.i18nFile.getText(I18nFile.INPUTVALUES), "", null);
				if(dialog.open() == InputDialog.OK){
				    String value = dialog.getValue();
				    service.addTail(id, db, key, value);
				    pageListener.setCount();
				    table.clear(table.getItemCount()-1);
				    table.setSelection(table.getItemCount()-1);
				    table.setSelection(-1);
				    currentData.setItem(null);
				    status = Status.Normal;
					statusChanged();
				}
			}
		});
		btnAppendTail.setText(RedisClient.i18nFile.getText(I18nFile.APPENDTAIL));

		btnDeleteHead = new Button(grpValues, SWT.NONE);
		btnDeleteHead.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnDeleteHead.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				service.removeFirst(id, db, key);
				refresh();
			}
		});
		btnDeleteHead.setText(RedisClient.i18nFile.getText(I18nFile.DELETEHEAD));

		btnDeleteTail = new Button(grpValues, SWT.NONE);
		btnDeleteTail.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1,
				1));
		btnDeleteTail.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				service.removeLast(id, db, key);
				pageListener.setCount();
				table.getItem(table.getItemCount()-1);
				table.setSelection(table.getItemCount()-1);
			    table.setSelection(-1);
			    currentData.setItem(null);
			    status = Status.Normal;
				statusChanged();
			}
		});
		btnDeleteTail.setText(RedisClient.i18nFile.getText(I18nFile.DELETETAIL));
		
		btnApply = new Button(grpValues, SWT.NONE);
		btnApply.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem[] items = table.getSelection();
				if(items==null){
					MessageDialog.openError(shell, "error", "please select item!");return;
				}
				service.setValue(id, db, key, table.getSelectionIndex(), items[0].getText());
				table.setSelection(-1);
				currentData.setItem(null);
				status = Status.Normal;
				statusChanged();
			}
		});
		btnApply.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnApply.setText(RedisClient.i18nFile.getText(I18nFile.APPLY));
		
		btnCancel = new Button(grpValues, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				switch(status){
				case Normal:
					break;
				case Update:
					table.setSelection(-1);
					status = Status.Normal;
					currentData.setItem(null);
					statusChanged();
					break;
				case Updating:
					currentData.reset();
					status = Status.Update;
					listener.clickRow(currentData.getItem(), 0);
					addModifyTextListener();
					statusChanged();
					break;
				}
				
			}
		});
		btnCancel.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnCancel.setEnabled(false);
		btnCancel.setText(RedisClient.i18nFile.getText(I18nFile.CANCEL));
		
		btnRefresh = new Button(grpValues, SWT.NONE);
		btnRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				refresh();
			}
		});
		btnRefresh.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnRefresh.setEnabled(true);
		btnRefresh.setText(RedisClient.i18nFile.getText(I18nFile.REFRESH));

		btnWatch = new Button(grpValues, SWT.NONE);
		btnWatch.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnWatch.setText(RedisClient.i18nFile.getText(I18nFile.WATCH));
		btnWatch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				WatchDialog dialog = new WatchDialog(shell, null, currentData.getValue());
				dialog.open();
			}
		});
	}
	
	protected void tableItemSelected() {
		TableItem[] items = table.getSelection();
		addModifyTextListener();
		
		if(currentData.isItemChanged(items[0])){
			switch(status){
			case Normal:
				status = Status.Update;
				statusChanged();
				
				break;
			
			case Update:
				break;
				
			case Updating:
				service.setValue(id, db, key, table.indexOf(currentData.getItem()), currentData.getItem().getText());
				status = Status.Update;
				statusChanged();
				break;
			}
			currentData.setItem(items[0]);
		}
	}
	
	public void statusChanged(){
		switch(status){
		case Normal:
			btnInsertHead.setEnabled(true);
			btnAppendTail.setEnabled(true);
			
			btnDeleteHead.setEnabled(true);
			btnDeleteTail.setEnabled(true);
			btnCancel.setEnabled(false);
			btnRefresh.setEnabled(true);
			btnWatch.setEnabled(false);
			break;
			
		case Update:
			btnInsertHead.setEnabled(true);
			btnAppendTail.setEnabled(true);
			
			btnDeleteHead.setEnabled(true);
			btnDeleteTail.setEnabled(true);
			btnCancel.setEnabled(true);
			btnRefresh.setEnabled(true);
			btnWatch.setEnabled(true);
			break;
			
		case Updating:
			btnInsertHead.setEnabled(false);
			btnAppendTail.setEnabled(false);
			
			btnDeleteHead.setEnabled(false);
			btnDeleteTail.setEnabled(false);
//			setApply(true);
			btnCancel.setEnabled(true);
			btnRefresh.setEnabled(false);
			btnWatch.setEnabled(false);
			break;
		
		}
		
	}
	/**
	 * 
	 */
	private void refresh() {
		pageListener.setCount();
		table.clearAll();
		table.setSelection(0);
		table.setSelection(-1);
		currentData.setItem(null);
		status = Status.Normal;
		statusChanged();
	}
	private void addModifyTextListener() {
		editor = listener.getText();
		if(!editor.isDisposed()){
			editor.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					switch(status){
					case Normal:
						break;
					
					case Update:
						status = Status.Updating;
						statusChanged();
						
						break;
					case Updating:
						break;
					}
				}
			});
		}
	}

	@Focus
	public void setFocus() {
	}

	@Persist
	public void save() {
		dirty.setDirty(false);
	}
}