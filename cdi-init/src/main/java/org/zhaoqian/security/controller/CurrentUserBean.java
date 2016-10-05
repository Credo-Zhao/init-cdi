/**
 * <p>Copyright (c) 2015 ZhaoQian.All Rights Reserved.</p>
 * @author <a href="zhaoqianjava@foxmail.com">ZhaoQian</a>
 */
package org.zhaoqian.security.controller;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.zhaoqian.security.model.User;
import org.zhaoqian.security.qualifier.Authenticated;

/**
 * @author ZhaoQian
 */
@Named
@SessionScoped
public class CurrentUserBean implements Serializable
{
	private static final long serialVersionUID = -5278593311950833432L;
	public static final String ANONYMOUS = "匿名";

	@Produces
	@Authenticated
	@Named("currentUser")
	public User getCurrentUser()
	{
		return currentUser;
	}

	@Produces
	@Authenticated
	@Named("currentUserName")
	public String getCurrentUserName()
	{
		return currentUser != null ? currentUser.getRealName() : ANONYMOUS;
	}

	private User currentUser;

	public void onLogin(@Observes @Authenticated User user, HttpServletRequest request)
	{
		setCurrentUser(user);
	}

	public void setCurrentUser(User currentUser)
	{
		this.currentUser = currentUser;
	}
}
