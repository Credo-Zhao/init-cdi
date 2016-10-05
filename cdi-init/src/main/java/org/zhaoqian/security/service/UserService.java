package org.zhaoqian.security.service;

import java.util.HashSet;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.zhaoqian.security.model.Role;
import org.zhaoqian.security.model.User;
import org.zhaoqian.security.model.User_;
import org.zhaoqian.security.repository.RoleRepository;
import org.zhaoqian.security.repository.UserRepository;

/**
 * @author ZhaoQian
 */
@RequestScoped
public class UserService
{
	@Inject
	UserRepository userRepository;

	@Inject
	RoleRepository roleRepository;

	public List<User> queryAllUser()
	{
		return this.userRepository.queryAllEntity(User.class);
	}

	public List<Role> queryAllRole()
	{
		return this.roleRepository.queryAllEntity(Role.class);
	}

	public User queryUserById(Long userId)
	{
		return this.userRepository.queryEntityById(User.class, userId);
	}

	public boolean deleteUserById(Long userId)
	{
		return this.userRepository.deleteEntityById(User.class, userId, User_.id.getName());
	}

	public User saveOrUpdateUser(User user)
	{
		return this.userRepository.merge(user);
	}

	public User saveSelectedRoleIdsListInUser(User user, List<String> selectedRoleNames, List<Role> systemRoleList)
	{
		HashSet<Role> roleSet = new HashSet<Role>();
		for (String roleName : selectedRoleNames)
		{
			for (Role role : systemRoleList)
			{
				if (StringUtils.equalsIgnoreCase(roleName, role.getName()))
				{
					roleSet.add(role);
					continue;
				}
			}
		}
		user.setRoles(roleSet);
		return user;
	}
}
