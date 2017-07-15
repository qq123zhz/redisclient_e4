package redisclient_e4.handlers;

import java.io.File;
import java.io.IOException;

import javax.inject.Named;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import com.cxy.redisclient.integration.I18nFile;
import com.cxy.redisclient.service.ExportService;

import redisclient_e4.models.DbElement;
/**
 * @author Administrator
 *
 */
public class ExportDbHandler extends AbstractRedisHandler {
	
	 MPart activePart;
	
	@Execute
	public void execute(Shell shell,@Active MPart activePart,
			@Optional @Named(IServiceConstants.ACTIVE_SELECTION) DbElement cinfo){
		super.execute(shell);
		this.activePart = activePart;
		 
		FileDialog dialog = new FileDialog(shell, SWT.SAVE);
		dialog.setText(i18nFile.getText(I18nFile.EXPORTREDIS));
		String[] filterExt = { "*.*" };
		dialog.setFilterExtensions(filterExt);
		String file = dialog.open();
		if (file != null) {
			File exportFile = new File(file);

			boolean ok = false;
			boolean exist = exportFile.exists();
			if (exist)
				ok = MessageDialog.openConfirm(shell, i18nFile.getText(I18nFile.FILEEXIST),
						i18nFile.getText(I18nFile.FILEREPLACE));
			if (!exist || ok) {
				 
				exportOne(cinfo, file);
			}
		}
	}
	
	/**
	 * @param cinfo
	 * @param file
	 * @param item
	 */
	private void exportOne(DbElement cinfo, String file) {

		ExportService service = new ExportService(file, cinfo.getServerId(), cinfo.getDbId(), null);
		try {
			service.export();
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	 
}
