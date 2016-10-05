package org.zhaoqian.security.service;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.zhaoqian.security.model.Role;
import org.zhaoqian.security.model.Role_;
import org.zhaoqian.security.model.enums.Permission;
import org.zhaoqian.security.repository.RoleRepository;

/**
 * @author ZhaoQian
 */
@RequestScoped
public class RoleService
{
	@Inject
	private RoleRepository roleRepository;

	public List<Role> queryAllRole()
	{
		return this.roleRepository.queryAllEntity(Role.class);
	}

	public Role queryRoleById(Long roleId)
	{
		return this.roleRepository.queryEntityById(Role.class, roleId);
	}

	public boolean deleteRoleById(Long roleId)
	{
		return this.roleRepository.deleteEntityById(Role.class, roleId, Role_.id.getName());
	}

	public Role saveOrUpdateRole(Role role)
	{
		return this.roleRepository.merge(role);
	}

	public boolean checkRoleNameIsExist(String roleName, Long roleId)
	{
		return this.roleRepository.checkRoleNameIsExist(roleName, roleId);
	}

	public List<String> obtainPermissionStringList()
	{
		List<String> list = new ArrayList<String>();
		for (Permission permission : Permission.values())
		{
			list.add(permission.getExpression());
		}
		return list;
	}

	public void remove(Role role)
	{
		this.roleRepository.delete(role);
	}

}
