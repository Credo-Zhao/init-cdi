package org.zhaoqian.security.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DualListModel;
import org.zhaoqian.common.qualifier.ViewModel;
import org.zhaoqian.security.model.Role;
import org.zhaoqian.security.model.User;
import org.zhaoqian.security.service.UserService;
import org.zhaoqian.security.shiro.PasswordHelper;
import org.zhaoqian.security.shiro.realm.JpaRealm;
import org.zhaoqian.util.JSFUtils;

/**
 * @author ZhaoQian
 */
@ViewModel
public class UserBean implements Serializable
{
	private static final long serialVersionUID = -8300757506636436269L;

	@Inject
	private UserService userService;

	@Inject
	private Instance<JpaRealm> jpaRealm;

	private List<User> userList = new ArrayList<>();
	private List<Role> systemRoleList = new ArrayList<>();
	private User user = new User();
	private DualListModel<String> rolePickListSource;

	@PostConstruct
	private void init()
	{
		this.userList = this.userService.queryAllUser();
		this.systemRoleList = this.userService.queryAllRole();
	}

	public void updateEvent(Long userId)
	{
		this.user = this.userService.queryUserById(userId);

		List<String> pfPickListTarget = new ArrayList<String>();
		Set<Role> roles = this.user.getRoles();
		for (Role role : roles)
		{
			pfPickListTarget.add(role.getName());
		}
		List<String> pfPickListSource = new ArrayList<String>();
		for (Role role : systemRoleList)
		{
			if (!pfPickListTarget.contains(role.getName()))
			{
				pfPickListSource.add(role.getName());
			}
		}
		rolePickListSource = new DualListModel<>(pfPickListSource, pfPickListTarget);
	}

	public void updateUser()
	{
		List<String> selectedRoleNames = rolePickListSource.getTarget();
		user = this.userService.saveSelectedRoleIdsListInUser(user, selectedRoleNames, systemRoleList);
		this.user = this.userService.saveOrUpdateUser(user);
		jpaRealm.get().clearAllCachedAuthenticationInfo();
		JSFUtils.printPageInfoMessage("Update Successful" + user.getName() + "has been updated.");
		RequestContext.getCurrentInstance().addCallbackParam("update", true);
		this.userList = userService.queryAllUser();
	}

	public void createEvent()
	{
		this.user = new User();

		List<String> pfPickListTarget = new ArrayList<String>();
		List<String> pfPickListSource = new ArrayList<String>();
		for (Role role : systemRoleList)
		{
			pfPickListSource.add(role.getName());
		}
		rolePickListSource = new DualListModel<>(pfPickListSource, pfPickListTarget);
	}

	public void createUser()
	{
		if (!StringUtils.equalsIgnoreCase(user.getPassword(), user.getSecondInputPassword()))
		{
			JSFUtils.printPageErrorMessage("错误：您两次输入的密码不一致，请重新输入。");
			return;
		}
		List<String> selectedRoleNames = rolePickListSource.getTarget();
		user = this.userService.saveSelectedRoleIdsListInUser(user, selectedRoleNames, systemRoleList);
		user = PasswordHelper.generateUserPasswordHashAndSalt(user);
		this.user = this.userService.saveOrUpdateUser(user);
		JSFUtils.printPageInfoMessage("Save Successful:" + user.getName() + "has been saved.");
		RequestContext.getCurrentInstance().addCallbackParam("create", true);
		this.userList = userService.queryAllUser();
	}

	public void changePasswordEvent(Long userId)
	{
		this.user = this.userService.queryUserById(userId);
	}

	public void changePassword()
	{
		if (!StringUtils.equalsIgnoreCase(user.getPassword(), user.getSecondInputPassword()))
		{
			JSFUtils.printPageErrorMessage("错误:您两次输入的密码不一致，请重新输入。");
			return;
		}
		user = PasswordHelper.generateUserPasswordHashAndSalt(user);
		this.user = this.userService.saveOrUpdateUser(user);
		jpaRealm.get().clearAllCachedAuthenticationInfo();
		JSFUtils.printPageInfoMessage("Password Change Successful:" + user.getName() + "password has been changed.");
		RequestContext.getCurrentInstance().addCallbackParam("changePwd", true);
	}

	public List<User> getUserList()
	{
		return userList;
	}

	public void setUserList(List<User> userList)
	{
		this.userList = userList;
	}

	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}

	public DualListModel<String> getRolePickListSource()
	{
		return rolePickListSource;
	}

	public void setRolePickListSource(DualListModel<String> rolePickListSource)
	{
		this.rolePickListSource = rolePickListSource;
	}

	public List<Role> getSystemRoleList()
	{
		return systemRoleList;
	}

	public void setSystemRoleList(List<Role> systemRoleList)
	{
		this.systemRoleList = systemRoleList;
	}

}
