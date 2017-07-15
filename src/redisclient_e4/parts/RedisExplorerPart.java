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
import org.eclipse.jface.dialogs.MessageDialog;
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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.cxy.redisclient.domain.NodeType;
import com.cxy.redisclient.domain.Server;

import redisclient_e4.handlers.OpenDbHandler;
import redisclient_e4.models.DbElement;
import redisclient_e4.provider.TableViewerLabelProvider;
import redisclient_e4.utils.MyEventConstants;
import redisclient_e4.utils.PlatFormUtils;

/**
 * @author Administrator
 *
 */
public class RedisExplorerPart extends AbstractPart{

	public static final String ID = "redisclient_e4.part.dataexplorer";
	private TableViewer tableViewer;
	private List<DbElement>rootList = new ArrayList<DbElement>();
	
	private String menuId = "redisclient_e4.popupmenu.dataexplorer";
	
	@Inject Shell shell;
	public RedisExplorerPart() {
		 
	}
	
	public List<DbElement> getRootList() {
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
		
		tableViewer.setInput(rootList.toArray(new DbElement[]{}));
		tableViewer.addDoubleClickListener(new IDoubleClickListener() {
			
			@Override
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection selection =  (IStructuredSelection) event.getSelection();
				DbElement element =  (DbElement) selection.getFirstElement();
				
				Map paramMap = new HashMap<>();
				paramMap.put("DbElement", element.toString());
				PlatFormUtils.executeCommand(commandService, handlerService, OpenDbHandler.CMDID, paramMap);
				
				
			}
		});
		
		setSelection(tableViewer);
		
		TableColumn tblclmnName = new TableColumn(tree, SWT.NONE);
		tblclmnName.setWidth(100);
		tblclmnName.setText("name");
		
		TableColumn tblclmnType = new TableColumn(tree, SWT.NONE);
		tblclmnType.setWidth(100);
		tblclmnType.setText("type");
		
		TableColumn tblclmnSize = new TableColumn(tree, SWT.NONE);
		tblclmnSize.setWidth(100);
		tblclmnSize.setText("size");
		registerContextMenu(tableViewer.getControl(), menuId );
		 
	}

	@Focus
	public void setFocus() {
		tableViewer.getTable().setFocus();
	}
	

}