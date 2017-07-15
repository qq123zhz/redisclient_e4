package redisclient_e4.parts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import redisclient_e4.models.RedisTreeNode;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import redisclient_e4.handlers.OpenKeysHandler;
import redisclient_e4.models.DbElement;
import redisclient_e4.models.KeysElement;
import redisclient_e4.provider.TableViewerLabelProvider;
import redisclient_e4.utils.PlatFormUtils;

/**
 * @author Administrator
 *
 */
public class RedisKeyListPart extends AbstractPart{

	private TableViewer tableViewer;
	private List<KeysElement>rootList = new ArrayList<KeysElement>();
	@Inject private ECommandService commandService;
	@Inject private EHandlerService handlerService;
	private String menuId = "redisclient_e4.popupmenu.keylist";
	private DbElement element;
	
	public RedisKeyListPart() {
		 
	}
	
	public List<KeysElement> getRootList() {
		return rootList;
	}
	public TableViewer getTableViewer() {
		return tableViewer;
	}

	@PostConstruct
	public void createComposite(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		tableViewer = new  TableViewer(parent,SWT.FULL_SELECTION|SWT.SINGLE);
		Table tree = tableViewer.getTable();
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		tableViewer.setLabelProvider(new TableViewerLabelProvider());
		
		tableViewer.setInput(rootList.toArray(new KeysElement[]{}));
		
		setSelection(tableViewer);
		
		TableColumn tblclmnName = new TableColumn(tree, SWT.NONE);
		tblclmnName.setWidth(200);
		tblclmnName.setText("name");
		
		TableColumn tblclmnType = new TableColumn(tree, SWT.NONE);
		tblclmnType.setWidth(200);
		tblclmnType.setText("type");
		
		TableColumn tblclmnSize = new TableColumn(tree, SWT.NONE);
		tblclmnSize.setWidth(100);
		tblclmnSize.setText("size");
		registerContextMenu(tableViewer.getControl(), menuId );
		tableViewer.addDoubleClickListener(new IDoubleClickListener() {
			
			@Override
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection selection =  (IStructuredSelection) event.getSelection();
				KeysElement element =  (KeysElement) selection.getFirstElement();
				
				Map paramMap = new HashMap<>();
				paramMap.put("KeysElement", element.toString());
				PlatFormUtils.executeCommand(commandService, handlerService, OpenKeysHandler.CMDID, paramMap);
				
			}
		});
		 
	}

	@Focus
	public void setFocus() {
		tableViewer.getTable().setFocus();
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
		
	}

	/**
	 * @param element
	 */
	public void setDbElement(DbElement element) {
		this.element = element;
		
	}
	public DbElement getElement() {
		return element;
	}

}