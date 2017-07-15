package redisclient_e4.handlers;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.model.application.ui.basic.MStackElement;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

import com.cxy.redisclient.integration.I18nFile;
import com.cxy.redisclient.service.HashService;
import com.cxy.redisclient.service.ListService;
import com.cxy.redisclient.service.NodeService;
import com.cxy.redisclient.service.ServerService;
import com.cxy.redisclient.service.SetService;
import com.cxy.redisclient.service.ZSetService;

import redisclient_e4.models.DbElement;
import redisclient_e4.models.Element;
import redisclient_e4.models.KeysElement;

public class AbstractRedisHandler {

	protected static Image redisImage;
	protected static Image dbImage;
	protected static Image containerImage;
	protected static Image strImage;
	protected static Image strGrayImage;
	protected static Image setImage;
	protected static Image setGrayImage;
	protected static Image listImage;
	protected static Image listGrayImage;
	protected static Image zsetImage;
	protected static Image zsetGrayImage;
	protected static Image hashImage;
	protected static Image hashGrayImage;
	protected static Image questionImage;
	protected static Image codeImage;
	protected static Image iconImage;
	protected static Image refreshImage;
	protected static Image upImage;
	protected static Image rightImage;
	protected static Image leftImage;
	@Optional @Inject protected  Shell shell;
	
	public static final I18nFile i18nFile = new I18nFile();
	
	@Inject  protected ServerService service1;
	@Inject  protected NodeService service2;
	@Inject  protected  ListService service4 ;
	@Inject  protected  SetService service5 ;
	@Inject  protected  ZSetService service6 ;
	@Inject  protected  HashService service7 ;
	
	@Inject protected EPartService partService;
	@Inject protected EModelService modelService;
	@Inject protected MApplication app;
	@Inject protected ECommandService commandService;
	@Inject protected EHandlerService handlerService;
	@Inject IEventBroker broker;
	
	protected MPart activePart;
	
	private String partStackId = "redisclient_e4.partstack.detail";
	
	protected static final String DB_PREFIX = "DB";
	
	/*protected static ServerService service1 = new ServerService();
	protected static NodeService service2 = new NodeService();
	protected static FavoriteService service3 = new FavoriteService();
	protected static ListService service4 = new ListService();
	protected static SetService service5 = new SetService();
	protected static ZSetService service6 = new ZSetService();
	protected static HashService service7 = new HashService();*/

	@Execute
	public void execute(Shell shell) {
		this.shell = shell;
		initImage(shell);
	}

	/**
	 * @param shell
	 */
	public static void initImage(Shell shell) {
		
		redisImage = new Image(shell.getDisplay(), AbstractRedisHandler.class.getResourceAsStream("/icons/redis.png"));
		dbImage = new Image(shell.getDisplay(), AbstractRedisHandler.class.getResourceAsStream("/icons/db.png"));

		containerImage = new Image(shell.getDisplay(), AbstractRedisHandler.class.getResourceAsStream("/icons/container.png"));

		strImage = new Image(shell.getDisplay(), AbstractRedisHandler.class.getResourceAsStream("/icons/string.png"));
		strGrayImage = new Image(shell.getDisplay(), strImage, SWT.IMAGE_GRAY);

		setImage = new Image(shell.getDisplay(), AbstractRedisHandler.class.getResourceAsStream("/icons/set.png"));
		setGrayImage = new Image(shell.getDisplay(), setImage, SWT.IMAGE_GRAY);

		listImage = new Image(shell.getDisplay(), AbstractRedisHandler.class.getResourceAsStream("/icons/list.png"));
		listGrayImage = new Image(shell.getDisplay(), listImage, SWT.IMAGE_GRAY);

		zsetImage = new Image(shell.getDisplay(), AbstractRedisHandler.class.getResourceAsStream("/icons/zset.png"));
		zsetGrayImage = new Image(shell.getDisplay(), zsetImage, SWT.IMAGE_GRAY);

		hashImage = new Image(shell.getDisplay(), AbstractRedisHandler.class.getResourceAsStream("/icons/hash.png"));
		hashGrayImage = new Image(shell.getDisplay(), hashImage, SWT.IMAGE_GRAY);

		leftImage = new Image(shell.getDisplay(), AbstractRedisHandler.class.getResourceAsStream("/icons/left.png"));
		rightImage = new Image(shell.getDisplay(), AbstractRedisHandler.class.getResourceAsStream("/icons/right.png"));
		upImage = new Image(shell.getDisplay(), AbstractRedisHandler.class.getResourceAsStream("/icons/up.png"));
		refreshImage = new Image(shell.getDisplay(), AbstractRedisHandler.class.getResourceAsStream("/icons/refresh.png"));

		iconImage = new Image(shell.getDisplay(), AbstractRedisHandler.class.getResourceAsStream("/icons/icon.png"));

//		codeImage = new Image(shell.getDisplay(), AbstractRedisHandler.class.getResourceAsStream("/icons/code.png"));

		questionImage = new Image(shell.getDisplay(), AbstractRedisHandler.class.getResourceAsStream("/icons/question.png"));
	}
	
	/**
	 * @param path
	 * @return 
	 */
	private Image createImage(String path) {
		ImageDescriptor desc = ImageDescriptor.createFromFile(AbstractRedisHandler.class, path);
		Image image = desc.createImage();
		return image;
	}
	
	 
	/**
	 * @param keyDetailPartId
	 * @param element
	 * @return 
	 */
	public MPart openPart(String keyDetailPartId,Element element) {
		MPart keyDetailPart = partService.createPart(keyDetailPartId);
		
		MPartStack partStack = (MPartStack) modelService.find(partStackId , app);
		String label = element.getName();
		if(element instanceof KeysElement){
			KeysElement keysElement = (KeysElement) element;
			label = keysElement.getDbElement().getServer().getName()+" "+keysElement.getDbElement().getName()+" "+keysElement.getName();
		}else if(element instanceof DbElement){
			DbElement dbElement = (DbElement) element;
			label = dbElement.getServer().getName()+" "+dbElement.getName();
		}
		
		if(partStack!=null){
			 List<MStackElement> stackElements = partStack.getChildren();
			 if(stackElements.isEmpty()){
				 partStack.getChildren().add(keyDetailPart);
				 keyDetailPart.setLabel(label);
				 partService.activate(keyDetailPart);
				 partService.bringToTop(keyDetailPart);
				 return keyDetailPart;
			 }
			 boolean flag = false;
			 for (MStackElement mStackElement : stackElements) {
				 boolean f = mStackElement instanceof MPart;
				 if(!f)continue;
				 MPart mPart = (MPart) mStackElement;
				 if(mPart.getLabel().equals(label)){
					 flag = true;
					 keyDetailPart = mPart;
					
					 partService.activate(mPart);
					 partService.bringToTop(mPart);continue;
				 }
				 
			}
			 if(!flag){
				 
				 partStack.getChildren().add(keyDetailPart);
				 keyDetailPart.setLabel(label);
				 partService.activate(keyDetailPart);
				 partService.bringToTop(keyDetailPart);
			 }
		}
		return keyDetailPart;
	}
	
	public void openError(String msg) {
		 MessageDialog.openError(shell, "error", msg);
	}

}
