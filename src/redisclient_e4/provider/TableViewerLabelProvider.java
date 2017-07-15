package redisclient_e4.provider;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.cxy.redisclient.domain.NodeType;

import redisclient_e4.models.DbElement;
import redisclient_e4.models.KeysElement;
import redisclient_e4.utils.ImageUtils;

/**
 * @author Administrator
 *
 */
public class TableViewerLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		if(element instanceof DbElement){
			DbElement dataElement = (DbElement) element; 
			 if(columnIndex==0){
				 return ImageUtils.createImage(getClass(), "icons/resource_obj.gif");
			 }
		 }
		else if(element instanceof KeysElement){
			KeysElement dataElement = (KeysElement) element; 
			if(columnIndex==0){
				if(dataElement.getType()==NodeType.STRING)
					return ImageUtils.createImage(getClass(), "icons/static_co.gif");
				else if(dataElement.getType()==NodeType.LIST)
					return ImageUtils.createImage(getClass(), "icons/doc_co.gif");
				else if(dataElement.getType()==NodeType.SET)
					return ImageUtils.createImage(getClass(), "icons/aboutEredLabAction.gif");
				else if(dataElement.getType()==NodeType.SORTEDSET)
					return ImageUtils.createImage(getClass(), "icons/alphab_sort_co.gif");
				else if(dataElement.getType()==NodeType.HASH)
					return ImageUtils.createImage(getClass(), "icons/class_hi.gif");
			 }
		}
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		if(element instanceof DbElement){
			DbElement dataElement = (DbElement) element; 
			 return getDataElementText(dataElement,columnIndex);
		 }
		else if(element instanceof KeysElement){
			KeysElement dataElement = (KeysElement) element; 
			return getkeysElementText(dataElement,columnIndex);
		}
		return null;
	}

	/**
	 * @param dataElement
	 * @param columnIndex
	 * @return
	 */
	private String getkeysElementText(KeysElement dataElement, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return dataElement.getName();
		case 1:
			return dataElement.getType().name();
		case 2:
			return dataElement.getSize()+"";

		default:
			return "";
		}
		
	}

	/**
	 * @param dataElement
	 * @param columnIndex
	 * @return 
	 */
	private String getDataElementText(DbElement dataElement, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return dataElement.getName();
		case 1:
			return dataElement.getType().name();
		case 2:
			return dataElement.getSize()+"";

		default:
			return "";
		}
		
	}

}
