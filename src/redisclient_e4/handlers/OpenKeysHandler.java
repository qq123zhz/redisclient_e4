package redisclient_e4.handlers;

import javax.inject.Inject;

import org.eclipse.core.commands.ParameterValueConversionException;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.swt.widgets.Shell;

import com.cxy.redisclient.domain.NodeType;

import redisclient_e4.handlers.parm.MyParameterConverter;
import redisclient_e4.models.KeysElement;
import redisclient_e4.parts.RedisHashKeyDetailPart;
import redisclient_e4.parts.RedisListKeyDetailPart;
import redisclient_e4.parts.RedisSetKeyDetailPart;
import redisclient_e4.parts.RedisSortSetKeyDetailPart;
import redisclient_e4.parts.RedisStringKeyDetailPart;
/**
 * @author Administrator
 *
 */
public class OpenKeysHandler extends AbstractRedisHandler {
	
	 public static final String CMDID = "redisclient_e4.command.openkey";
	
	 MPart activePart;
	 @Inject
	private MyParameterConverter converter;	 

	
	@Execute
	public void execute(Shell shell,@Active MPart activePart,@Optional ParameterizedCommand command  ){
		super.execute(shell);
		this.activePart = activePart;
		Object objid = command.getParameterMap().get("KeysElement");
		if(objid==null)return;
		KeysElement element = null;
		try {
			converter.setElement(KeysElement.class);
			element = (KeysElement) converter.convertToObject(objid.toString());
		} catch (ParameterValueConversionException e) {
			e.printStackTrace();
		}
		NodeType nodeType = element.getType();
		if(nodeType==NodeType.STRING){
			String keyDetailPartId = "redisclient_e4.partdescriptor.keys.string";
			MPart keyDetailPart =  openPart(keyDetailPartId, element);
			 RedisStringKeyDetailPart listPart =  (RedisStringKeyDetailPart) keyDetailPart.getObject();
			 listPart.setElement(element);
		}
		else if(nodeType==NodeType.LIST){
			String keyDetailPartId = "redisclient_e4.partdescriptor.keys.list";
			MPart keyDetailPart = openPart(keyDetailPartId, element);
			 RedisListKeyDetailPart listPart =  (RedisListKeyDetailPart) keyDetailPart.getObject();
			 listPart.setElement(element);
			
		}else if(nodeType==NodeType.SET){
			String keyDetailPartId = "redisclient_e4.partdescriptor.keys.set";
			MPart keyDetailPart = openPart(keyDetailPartId, element);
			 RedisSetKeyDetailPart listPart =  (RedisSetKeyDetailPart) keyDetailPart.getObject();
			 listPart.setElement(element);
			
		}
		else if(nodeType==NodeType.SORTEDSET){
			String keyDetailPartId = "redisclient_e4.partdescriptor.keys.sortedset";
			MPart keyDetailPart = openPart(keyDetailPartId, element);
			 RedisSortSetKeyDetailPart listPart =  (RedisSortSetKeyDetailPart) keyDetailPart.getObject();
			 listPart.setElement(element);
			
		}
		else if(nodeType==NodeType.HASH){
			String keyDetailPartId = "redisclient_e4.partdescriptor.keys.hash";
			MPart keyDetailPart = openPart(keyDetailPartId, element);
			 RedisHashKeyDetailPart listPart =  (RedisHashKeyDetailPart) keyDetailPart.getObject();
			 listPart.setElement(element);
			
		}
		
	}
	
}
