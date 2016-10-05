package org.zhaoqian.common.exception.handler.jsf.handler;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.apache.deltaspike.core.api.exception.control.ExceptionHandler;
import org.apache.deltaspike.core.api.exception.control.Handles;
import org.apache.deltaspike.core.api.exception.control.event.ExceptionEvent;
import org.slf4j.Logger;
import org.zhaoqian.common.exception.handler.jsf.qualifier.FacesRequest;

/**
 * This exception handler uses {@link FacesMessage} to display the exception
 * message on JSF
 * 
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 */
@ExceptionHandler
public class FacesExceptionHandler
{
	@Inject
	Logger logger;

	void showFacesMessage(@Handles @FacesRequest ExceptionEvent<Throwable> evt, FacesContext facesContext)
	{

		String errorMessage = getRootErrorMessage(evt.getException());
		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, evt.getException().getMessage()));

		evt.handledAndContinue();
	}

	void handleViewExpiredException(@Handles @FacesRequest ExceptionEvent<ViewExpiredException> evt, FacesContext facesContext)
	{

		try
		{
			HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
			String url = facesContext.getExternalContext().getRequestContextPath() + "/";
			response.sendRedirect(url);
			facesContext.renderResponse();
			evt.handled();
		} catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}

	private String getRootErrorMessage(Throwable throwable)
	{

		// Default to general error message that registration failed.
		String errorMessage = "Operation failed. See server log for more information";
		if (throwable == null)
		{
			// This shouldn't happen, but return the default messages
			return errorMessage;
		}

		// Start with the exception and recurse to find the root cause
		Throwable t = throwable;
		while (t != null)
		{
			// Get the message from the Throwable class instance
			errorMessage = t.getLocalizedMessage();
			t = t.getCause();
		}
		// This is the root cause message
		return errorMessage;
	}

}
