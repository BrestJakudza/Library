package pl.atena.library.managedBeens;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import pl.atena.library.model.User;

@SuppressWarnings({ "unchecked", "deprecation" })
@FacesConverter("userConverter")
public class UserConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if (value != null && value.trim().length() > 0) {
			User result = null;
			try {
				System.out.println("Converter start...");
				List<User> modulesList = (List<User>) context.getApplication()
						.createValueBinding("#{booksBean.allUsers}").getValue(context);
				for (User user : modulesList) {
					if (value.equals(user.getId().toString())) {
						result = user;
						break;
					}
				}
				return result;
			} catch (NumberFormatException e) {
				throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Conversion Error", "Not a valid User."));
			}
		} else {
			return null;
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null && value instanceof User) {
			return ((User) value).getId().toString();
		} else {
			return null;
		}
	}

}
