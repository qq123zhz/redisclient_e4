package redisclient_e4.handlers;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.core.commands.ParameterValueConversionException;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.model.application.ui.basic.MStackElement;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Shell;

import com.cxy.redisclient.domain.DataNode;
import com.cxy.redisclient.domain.Node;

import redisclient_e4.handlers.parm.MyParameterConverter;
import redisclient_e4.models.DbElement;
import redisclient_e4.models.KeysElement;
import redisclient_e4.parts.RedisKeyListPart;
/**
 * @author Administrator
 *
 */
public class OpenDbHandler extends AbstractRedisHandler {
	
	 public static final String CMDID = "redisclient_e4.command.opendb";
	
	 MPart activePart;
	 @Inject
	private MyParameterConverter converter;	 
	 @Inject EModelService modelService;
	 @Inject MApplication app;
	 @Inject EPartService partService;

	private String dykeyListPartId = "redisclient_e4.partdescriptor.keylist";

	private String partStackId = "redisclient_e4.partstack.explorStack";

	
	@Execute
	public void execute(Shell shell,@Active MPart activePart,@Optional ParameterizedCommand command  ){
		super.execute(shell);
		this.activePart = activePart;
		partService.activate(activePart);
		Object objid = command.getParameterMap().get("DbElement");
		if(objid==null)return;
		DbElement element = null;
		converter.setElement(DbElement.class);
		try {
			element = (DbElement) converter.convertToObject(objid.toString());
		} catch (ParameterValueConversionException e) {
			e.printStackTrace();
		}
		
		
		MPart keyListPart = partService.createPart(dykeyListPartId);
		MPartStack partStack = (MPartStack) modelService.find(partStackId , app);
		String label = element.getServer().getName()+" "+element.getName()+" keysList";
		if(partStack!=null){
			 List<MStackElement> stackElements = partStack.getChildren();
			 if(stackElements.isEmpty()){
				 partStack.getChildren().add(keyListPart);
				 keyListPart.setLabel(label);
				 partService.activate(keyListPart);
				 partService.bringToTop(keyListPart);
				 return;
			 }
			 boolean flag = false;
			 for (MStackElement mStackElement : stackElements) {
				 boolean f = mStackElement instanceof MPart;
				 if(!f)continue;
				 MPart mPart = (MPart) mStackElement;
				 if(mPart.getLabel().equals(label)){
					 flag = true;
					 keyListPart = mPart;
					 partService.activate(mPart);
					 partService.bringToTop(mPart);continue;
				 }
				 
			}
			 if(!flag){
				 
				 partStack.getChildren().add(keyListPart);
				 keyListPart.setLabel(label);
				 partService.activate(keyListPart);
				 partService.bringToTop(keyListPart);
			 }
			 
			
		}
		RedisKeyListPart listPart =  (RedisKeyListPart) keyListPart.getObject();
		listPart.setDbElement(element);
		List<KeysElement>rootList = listPart.getRootList();
		rootList.clear();
		TableViewer tableViewer = listPart.getTableViewer();
		
		Set<DataNode> cnodes = service2.listContainerKeys(element.getServerId(),element.getDbId(), null, false);
		 
		for (DataNode node : cnodes) {
			System.out.println(node.toString());
			KeysElement keysElement = new KeysElement(node);
			keysElement.setDbElement(element);
			rootList.add(keysElement);
		}
		tableViewer.setInput(rootList);
//		tableViewer.refresh();
	}
	
}
