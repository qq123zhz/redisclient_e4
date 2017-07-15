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
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.cxy.redisclient.integration.I18nFile;
import com.cxy.redisclient.presentation.RedisClient;
import com.cxy.redisclient.presentation.WatchDialog;
import com.cxy.redisclient.presentation.string.StringDataContent;
import com.cxy.redisclient.service.NodeService;

import redisclient_e4.models.KeysElement;

public class RedisStringKeyDetailPart extends AbstractKeyDetailPart{


	@Inject
	private MDirtyable dirty;

	private StringDataContent content;
	
	private KeysElement  element;
	
	@Inject NodeService service ;
	@Inject private Shell shell;

	private String value="";

	private Button btnOk;

	private Button btnCancel;

	private Button btnRefresh;

	private Button btnWatch;
	private Composite keyscomposite;
	private Text keyTxt;
	private Text valueTxt;
	
	public void setElement(KeysElement element) {
		this.element = element;
		value = service.readString(element.getServerId(), element.getDbId(), element.getName());
		valueTxt.setText(value);
		keyTxt.setText(element.getName());
	}
	
	
	@PostConstruct
	public void createComposite(Composite dataComposite) { 
		dataComposite.setLayout(new GridLayout(1, false));
		
		keyscomposite = new Composite(dataComposite, SWT.NONE);
		keyscomposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		keyscomposite.setLayout(new GridLayout(2, false));
		
		Label lblKey = new Label(keyscomposite, SWT.NONE);
		lblKey.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblKey.setBounds(0, 0, 61, 17);
		lblKey.setText("key:");
		
		keyTxt = new Text(keyscomposite, SWT.BORDER);
		keyTxt.setEditable(false);
		keyTxt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		keyTxt.setBounds(0, 0, 73, 23);
		
		Label lblValue = new Label(keyscomposite, SWT.NONE);
		lblValue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblValue.setBounds(0, 0, 61, 17);
		lblValue.setText("value:");
		
		valueTxt = new Text(keyscomposite, SWT.BORDER);
		valueTxt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Composite composite = new Composite(dataComposite, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		composite.setLayout(new GridLayout(4, false));

		btnOk = new Button(composite, SWT.NONE);
		btnOk.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnOk.setText(RedisClient.i18nFile.getText(I18nFile.APPLY));
//		setApply(false);

		btnCancel = new Button(composite, SWT.NONE);
		btnCancel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnCancel.setEnabled(false);
		btnCancel.setText(RedisClient.i18nFile.getText(I18nFile.CANCEL));

		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String key = element.getName();
				value = valueTxt.getText();

				service.updateString(element.getServerId(), element.getDbId(), key, value);
//				setApply(false);
				btnCancel.setEnabled(false);
			}
		});

		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				valueTxt.setText(value);
//				setApply(false);
				btnCancel.setEnabled(false);
			}
		});
		btnRefresh = new Button(composite, SWT.NONE);
		btnRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				value = service.readString(element.getServerId(), element.getDbId(), element.getName());

				valueTxt.setText(value);
			}
		});
		btnRefresh.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		btnRefresh.setEnabled(true);
		btnRefresh.setText(RedisClient.i18nFile.getText(I18nFile.REFRESH));

		btnWatch = new Button(composite, SWT.NONE);
		btnWatch.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		btnWatch.setText(RedisClient.i18nFile.getText(I18nFile.WATCH));
		btnWatch.addSelectionListener(new SelectionAdapter() {
			

			@Override
			public void widgetSelected(SelectionEvent e) {
				WatchDialog dialog = new WatchDialog(shell, null, valueTxt.getText());
				dialog.open();
			}
		});
	
	}
	

	@Focus
	public void setFocus() {
	}
	/**事件监听
	 * @param event
	 */
	/*@Inject
	@Optional
	public void appStartUpComplete(@UIEventTopic(UIEvents.UILifeCycle.BRINGTOTOP) Event event) {
		MPart part = (MPart) event.getProperty(UIEvents.EventTags.ELEMENT);
		if(part==null)return;
		if (part.getObject() == this) {
			value = service.readString(element.getServerId(), element.getDbId(), element.getName());
		}
//		PlatFormUtils.executeCommand(commandService, handlerService, RefreshServerHandler.CMDID);
	}*/

	@Persist
	public void save() {
		dirty.setDirty(false);
	}
}