package pl.atena.library.utils;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

@Stateless
@Local
public class Growl {

	public static void showMsg(Severity type, String caption, String message) {
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(type, caption, message));
	}
}
