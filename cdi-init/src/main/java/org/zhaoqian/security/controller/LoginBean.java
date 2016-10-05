package org.zhaoqian.security.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Model;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.zhaoqian.security.model.User;
import org.zhaoqian.security.qualifier.Authenticated;
import org.zhaoqian.security.shiro.repository.JpaRealmRepository;
import org.zhaoqian.util.JSFUtils;

/**
 * @author ZhaoQian
 */
@Model
public class LoginBean implements Serializable
{

	private static final long serialVersionUID = 6511123006314680155L;

	@Inject
	private JpaRealmRepository jpaRealmRepository;

	@Inject
	private Logger log;

	@Inject
	@Authenticated
	private Event<User> loginEvent;

	private String username;

	private String password;

	private final boolean rememberMe = true;

	private String captcha;

	private final String successUrl = "/";

	public void login() throws IOException
	{
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

		Subject subject = SecurityUtils.getSubject();
		log.info("subject.isRemembered():{}", subject.isRemembered());
		// if (subject.isAuthenticated() || subject.isRemembered())
		// {
		// // WebUtils.redirectToSavedRequest(request, response, successUrl);
		// }
		{
			Session shiroSession = subject.getSession();
			Object kaptchaCode = shiroSession.getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);

			if (kaptchaCode != null && !StringUtils.equalsIgnoreCase(this.captcha, kaptchaCode.toString()))
			{
				JSFUtils.printPageErrorMessage("验证码错误:请重新输入验证码。");
				return;
			}
		}

		UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe, request.getRemoteHost());
		log.info("username:{}", username);
		log.info("password:{}", password);
		log.info("request.getRemoteHost():{}", request.getRemoteHost());
		try
		{
			subject.login(token);
			User user = jpaRealmRepository.findUserByName(username);
			user.setLastLogin(new Date());
			user = jpaRealmRepository.mergeUser(user);
			loginEvent.fire(user);
			WebUtils.redirectToSavedRequest(request, response, successUrl);
		} catch (UnknownAccountException uae)
		{
			JSFUtils.printPageErrorMessage("没有此用户:请重新输入账户。");
			log.info("Unknown User!");
		} catch (IncorrectCredentialsException ice)
		{
			JSFUtils.printPageErrorMessage("密码错误:请重新输入密码。");
			log.info("Incorrect Password!");
		} catch (LockedAccountException lae)
		{
			JSFUtils.printPageErrorMessage("用户锁定:请联系管理员。");
			log.info("User Locked!");
		} catch (AuthenticationException ae)
		{
			JSFUtils.printPageErrorMessage("认证失败:请核对后重新输入。");
			log.info("Authentication Failed!");
		}
	}

	public String logout() throws IOException
	{
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		return "/faces/home.jsf?faces-redirect=true";
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getCaptcha()
	{
		return captcha;
	}

	public void setCaptcha(String captcha)
	{
		this.captcha = captcha;
	}

	public boolean isRememberMe()
	{
		return rememberMe;
	}

}
