package redisclient_e4.handlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Named;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import com.cxy.redisclient.domain.Server;
import com.cxy.redisclient.integration.I18nFile;
import com.cxy.redisclient.presentation.server.AddServerDialog;
import com.cxy.redisclient.service.ImportService;

import redisclient_e4.models.DbElement;
import redisclient_e4.models.RedisTreeNode;
import redisclient_e4.parts.RedisServerListPart;
/**
 * @author Administrator
 *
 */
public class ImportKey2DbHandler extends AbstractRedisHandler {
	
	MPart activePart;
	
	@Execute
	public void execute(Shell shell,@Active MPart activePart,
			@Optional @Named(IServiceConstants.ACTIVE_SELECTION) DbElement cinfo){
		super.execute(shell);
		this.activePart = activePart;
		
		
		
		FileDialog dialog = new FileDialog(shell, SWT.OPEN);
		dialog.setText(i18nFile.getText(I18nFile.IMPORTREDIS));
		String[] filterExt = { "*.*" };
		dialog.setFilterExtensions(filterExt);
		String file = dialog.open();
		if (file != null) {
			ImportService service = new ImportService(file, cinfo.getServerId(), cinfo.getDbId());
			try {
				service.importFile();
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage());
			}

		}
	}
	
}
