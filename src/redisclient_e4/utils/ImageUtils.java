package redisclient_e4.utils;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;



/**
 * @author Administrator
 *
 */
public class ImageUtils {
	private static Map<String, Image> imgRegistry = new HashMap<String, Image>();
	
	private static Map<String, ImageDescriptor> idRegistry = new HashMap<String, ImageDescriptor>();
	/**
	 * @param imgPath
	 * @return
	 */
	public static Image createImage(Class<?> clazz,String imgPath){
		if(imgRegistry.containsKey(imgPath)){
			return imgRegistry.get(imgPath);
		}
		Image image =  createImageDescriptor(clazz,imgPath).createImage();
//		Image image =  new Image(clazz.getResourceAsStream("/"+imgPath));
		imgRegistry.put(imgPath, image);
		return image;
		
	}
	/**
	 * @param imgPath
	 * @return
	 */
	public static ImageDescriptor createImageDescriptor(Class<?> clazz,String imgPath){
		
		if (idRegistry.containsKey(imgPath))
			return idRegistry.get(imgPath);
//		ImageDescriptor descriptor =  AbstractUIPlugin.imageDescriptorFromPlugin(CashConst.PLUGIN_ID,
//				imgPath);
		if (idRegistry.get(imgPath) == null) {
//			Bundle bundle = FrameworkUtil.getBundle(clazz);
//			URL url = FileLocator.find(bundle, new Path(imgPath), null);
			ImageDescriptor imageDescr = ImageDescriptor.createFromFile(clazz, "/"+imgPath);
			idRegistry.put(imgPath, imageDescr);
			return imageDescr;
		}
		return null;
		 
		
	}
	
	/**
	 * 
	 */
	public static void dispose(){
//		imgRegistry.clear();
		
//		idRegistry.clear();
		
	}
	
}
