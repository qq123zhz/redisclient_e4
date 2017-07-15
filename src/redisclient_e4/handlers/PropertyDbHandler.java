package redisclient_e4.handlers;

import java.util.Set;

import javax.inject.Named;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import com.cxy.redisclient.domain.Node;
import com.cxy.redisclient.domain.NodeType;
import com.cxy.redisclient.integration.I18nFile;

import redisclient_e4.models.DbElement;
/**
 * @author Administrator
 *
 */
public class PropertyDbHandler extends AbstractRedisHandler {
	
	MPart activePart;
	
	@Execute
	public void execute(Shell shell,@Active MPart activePart,
			@Optional @Named(IServiceConstants.ACTIVE_SELECTION) DbElement element){
		super.execute(shell);
		this.activePart = activePart;
		
		String str = null;
		String container = null;
		NodeType type = element.getType();
		

		if (type == NodeType.DATABASE) {
			str = i18nFile.getText(I18nFile.TYPE) + ":\t" + i18nFile.getText(I18nFile.DATABASE) + "\n"
					+ i18nFile.getText(I18nFile.LOCATION) + ":\t" + getLocation(element) + "\n"
					+ i18nFile.getText(I18nFile.KEY) + ":\t";
			container = "";
		} 

		Set<Node> nodes = service2.listContainerAllKeys(element.getServerId(), element.getDbId(), container);
		str += nodes.size();

		String properties;

		if (type == NodeType.DATABASE)
			properties = getLocation(element) + " " + i18nFile.getText(I18nFile.DBPROPERTIES);
		else
			properties = getLocation(element) + " " + i18nFile.getText(I18nFile.CONTAINERPROPERTIES);
		MessageDialog.openInformation(shell, properties, str);
	}
	
	/**
	 * @param info
	 * @return
	 */
	private String getLocation(DbElement info) {
		return info.getServer().getName() + ":" + DB_PREFIX + info.getDbId() + ": ";
	}
	
}
