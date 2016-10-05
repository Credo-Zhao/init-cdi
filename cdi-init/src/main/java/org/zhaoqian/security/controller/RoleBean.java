package org.zhaoqian.security.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.primefaces.context.RequestContext;
import org.primefaces.model.DualListModel;
import org.slf4j.Logger;
import org.zhaoqian.common.qualifier.ViewModel;
import org.zhaoqian.security.model.Role;
import org.zhaoqian.security.model.enums.Permission;
import org.zhaoqian.security.service.RoleService;
import org.zhaoqian.security.shiro.realm.JpaRealm;
import org.zhaoqian.util.EHCacheShowDataUtils;
import org.zhaoqian.util.JSFUtils;

/**
 * @author ZhaoQian
 */
@ViewModel
public class RoleBean implements Serializable
{
	private static final long serialVersionUID = 6422028996213680640L;

	@Inject
	private RoleService roleService;

	@Inject
	private Logger log;

	@Inject
	private Instance<JpaRealm> jpaRealm;

	private Role role;
	private List<Role> roleList;
	private DualListModel<String> permissionPickListSource;

	@PostConstruct
	private void init()
	{
		this.roleList = roleService.queryAllRole();
	}

	public void queryRoleById(Long roleId)
	{
		this.role = this.roleService.queryRoleById(roleId);
	}

	public void updateEvent(Long roleId)
	{
		log.info("roleId:{}", roleId);
		this.role = this.roleService.queryRoleById(roleId);

		List<String> pfPickListTarget = role.getPermissions();

		List<String> pfPickListSource = new ArrayList<String>();
		for (Permission permission : Permission.values())
		{
			if (!pfPickListTarget.contains(permission.getExpression()))
			{
				pfPickListSource.add(permission.getExpression());
			}
		}

		permissionPickListSource = new DualListModel<>(pfPickListSource, pfPickListTarget);
	}

	@Inject
	EHCacheShowDataUtils ehCache;

	public void updateRole()
	{
		ehCache.showEhcache();
		if (this.roleService.checkRoleNameIsExist(role.getName(), role.getId()))
		{
			JSFUtils.printPageErrorMessage("权限名已存在:" + role.getName() + "权限名称已经存在.");
			return;
		}
		this.role.getPermissions().clear();
		role.setPermissions(this.permissionPickListSource.getTarget());
		this.role = this.roleService.saveOrUpdateRole(role);
		jpaRealm.get().clearAllCachedAuthorizationInfo();
		JSFUtils.printPageInfoMessage("Update Successful:" + role.getName() + "has been updated.");
		RequestContext.getCurrentInstance().addCallbackParam("update", true);
		this.roleList = roleService.queryAllRole();
		ehCache.showEhcache();
	}

	public void createEvent()
	{
		this.role = new Role();
		List<String> pfPickListSource = this.roleService.obtainPermissionStringList();
		List<String> pfPickListTarget = new ArrayList<>();
		permissionPickListSource = new DualListModel<>(pfPickListSource, pfPickListTarget);
	}

	public void createRole()
	{
		if (this.roleService.checkRoleNameIsExist(role.getName(), 10000000L))
		{
			JSFUtils.printPageErrorMessage("权限名已存在:" + role.getName() + "权限名称已经存在.");
			return;
		}
		this.role.getPermissions().clear();
		this.role.setPermissions(this.permissionPickListSource.getTarget());
		this.role = this.roleService.saveOrUpdateRole(role);
		JSFUtils.printPageInfoMessage("Save Successful:" + role.getName() + "has been saved.");
		RequestContext requestContext = RequestContext.getCurrentInstance();
		requestContext.addCallbackParam("create", true);
		this.roleList = roleService.queryAllRole();
	}

	public void deleteRoleById(Long roleId)
	{
		try
		{
			Role role = this.roleService.queryRoleById(roleId);
			this.roleService.remove(role);
			jpaRealm.get().clearAllCachedAuthorizationInfo();
			JSFUtils.printPageInfoMessage("Delete Successful.");
			this.roleList = roleService.queryAllRole();
		} catch (Exception ex)
		{
			JSFUtils.printPageErrorMessage(ex.getMessage());
		}
	}

	public Role getRole()
	{
		return role;
	}

	public void setRole(Role role)
	{
		this.role = role;
	}

	public List<Role> getRoleList()
	{
		return roleList;
	}

	public void setRoleList(List<Role> roleList)
	{
		this.roleList = roleList;
	}

	public DualListModel<String> getPermissionPickListSource()
	{
		return permissionPickListSource;
	}

	public void setPermissionPickListSource(DualListModel<String> permissionPickListSource)
	{
		this.permissionPickListSource = permissionPickListSource;
	}

}
