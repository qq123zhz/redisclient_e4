package redisclient_e4.handlers.parm;

import org.eclipse.core.commands.AbstractParameterValueConverter;
import org.eclipse.core.commands.ParameterValueConversionException;
import org.eclipse.e4.core.di.annotations.Creatable;

import com.google.gson.Gson;

import redisclient_e4.models.DbElement;
import redisclient_e4.models.Element;

/**
 * @author Administrator
 *
 */
@Creatable
public class MyParameterConverter extends AbstractParameterValueConverter {
	Gson gson = new Gson();
	private Class element;
	
	public void setElement(Class element) {
		this.element = element;
	}
	public MyParameterConverter() {
		
	}

	@Override
	public String convertToString(Object parameterValue) throws ParameterValueConversionException {
		return parameterValue.toString();
	}

	/**
	 * This will always create a new object. Just keep that in mind if you're
	 * trying to work with the objects.
	 */
	@Override
	public Object convertToObject(String parameterValue) throws ParameterValueConversionException {
		return gson.fromJson(parameterValue, element);
	}
}