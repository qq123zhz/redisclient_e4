package redisclient_e4.parts;

import javax.inject.Inject;

import org.eclipse.swt.widgets.Shell;

public abstract class AbstractKeyDetailPart extends AbstractPart {
	
	@Inject
	protected Shell shell;
	
	protected  int id ;
	protected  int db ;
	protected  String  key ;

}
