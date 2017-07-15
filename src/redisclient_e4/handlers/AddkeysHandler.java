package redisclient_e4.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.widgets.Shell;

import com.cxy.redisclient.dto.HashInfo;
import com.cxy.redisclient.dto.ListInfo;
import com.cxy.redisclient.dto.SetInfo;
import com.cxy.redisclient.dto.StringInfo;
import com.cxy.redisclient.dto.ZSetInfo;
import com.cxy.redisclient.presentation.hash.NewHashDialog;
import com.cxy.redisclient.presentation.list.NewListDialog;
import com.cxy.redisclient.presentation.set.NewSetDialog;
import com.cxy.redisclient.presentation.string.NewStringDialog;
import com.cxy.redisclient.presentation.zset.NewZSetDialog;

import redisclient_e4.models.DbElement;
import redisclient_e4.models.Element;
import redisclient_e4.models.KeysElement;
import redisclient_e4.utils.MyEventConstants;

/**
 * @author Administrator
 *
 */
public class AddkeysHandler extends AbstractRedisHandler {

	@Execute
	public void execute(Shell shell, @Active MPart activePart, @Optional @Named("key.type") String value,
			@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Element ele) {
		super.execute(shell);
		this.activePart = activePart;
		DbElement element = null;
		if(ele instanceof DbElement){
			element = (DbElement) ele;
		}else if(ele instanceof KeysElement){
			element = ((KeysElement) ele).getDbElement();
		}
		
		
		switch (value) {
		case "String":
			newString(element);
			break;
		case "List":
			newList(element);
			break;
		case "Set":
			newSet(element);
			break;
		case "SortSet":
			newSortSet(element);
			break;
		case "Hash":
			newHash(element);
			break;

		default:
			break;
		}
		broker.send(MyEventConstants.TOPIC_KEYS_REFRESH, element);
	}

	private void newHash(DbElement element) {
		int id = element.getServerId();
		String serverName = element.getServer().getName();
		int db = element.getDbId();

		NewHashDialog dialog = new NewHashDialog(shell, iconImage, id, serverName, db, "");
		HashInfo info = (HashInfo) dialog.open();
		if (info != null) {
			service7.add(id, db, info.getKey(), info.getValues(), info.getTtl());
		}

	}

	private void newSortSet(DbElement element) {
		int id = element.getServerId();
		String serverName = element.getServer().getName();
		int db = element.getDbId();

		NewZSetDialog dialog = new NewZSetDialog(shell, iconImage, id, serverName, db, "");
		ZSetInfo info = (ZSetInfo) dialog.open();
		if (info != null) {
			service6.add(id, db, info.getKey(), info.getValues(), info.getTtl());
		}

	}

	private void newSet(DbElement element) {

		int id = element.getServerId();
		String serverName = element.getServer().getName();
		int db = element.getDbId();

		NewSetDialog dialog = new NewSetDialog(shell, iconImage, id, serverName, db, "");
		SetInfo info = (SetInfo) dialog.open();
		if (info != null) {
			service5.add(id, db, info.getKey(), info.getValues(), info.getTtl());
		}

	}

	private void newList(DbElement element) {
		int id = element.getServerId();
		String serverName = element.getServer().getName();
		int db = element.getDbId();

		NewListDialog dialog = new NewListDialog(shell, iconImage, id, serverName, db, "");
		ListInfo info = (ListInfo) dialog.open();
		if (info != null) {
			service4.add(id, db, info.getKey(), info.getValues(), info.isHeadTail(), info.isExist(), info.getTtl());

		}

	}

	/**
	 * @param element
	 * 
	 */
	private void newString(DbElement element) {
		int id = element.getServerId();
		String serverName = element.getServer().getName();
		int db = element.getDbId();
		NewStringDialog dialog = new NewStringDialog(shell, iconImage, id, serverName, db, "");
		StringInfo info = (StringInfo) dialog.open();
		if (info != null) {
			service2.addString(id, db, info.getKey(), info.getValue(), info.getTtl());
		}

	}

}
