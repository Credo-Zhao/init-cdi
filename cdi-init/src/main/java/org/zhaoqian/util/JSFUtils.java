package org.zhaoqian.util;

import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;

public class JSFUtils
{
	public static Flash flashScope()
	{
		return FacesContext.getCurrentInstance().getExternalContext().getFlash();
	}

	public static Map<String, String> requestParamsMap()
	{
		return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
	}

	public static boolean requestIsAjaxRequest()
	{
		return FacesContext.getCurrentInstance().getPartialViewContext().isAjaxRequest();
	}

	public static void printPageErrorMessage(String summary)
	{
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, ""));
	}

	public static void printPageWarnMessage(String summary)
	{
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, summary, ""));
	}

	public static void printPageInfoMessage(String summary)
	{
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, summary, ""));
	}

	public static void printPageFatalMessage(String summary)
	{
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, summary, ""));
	}
}
