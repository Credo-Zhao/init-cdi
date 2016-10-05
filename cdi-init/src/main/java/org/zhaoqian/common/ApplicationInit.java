/**
 * Copyright (c) 2007 Beijing Shiji Kunlun Software Co., Ltd. All Rights Reserved.
 * $Id$
 */

package org.zhaoqian.common;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletContext;

import org.apache.deltaspike.core.api.lifecycle.Destroyed;
import org.apache.deltaspike.core.api.lifecycle.Initialized;
import org.slf4j.Logger;
import org.zhaoqian.security.model.Role;
import org.zhaoqian.security.model.User;
import org.zhaoqian.security.model.enums.Permission;
import org.zhaoqian.security.shiro.PasswordHelper;

import com.lowagie.text.FontFactory;

/**
 * @author ZhaoQian
 */
@Stateless
public class ApplicationInit
{

	@Inject
	private Logger log;

	@PersistenceContext
	private EntityManager em;

	public void onCreate(@Observes @Initialized ServletContext context)
	{
		log.info("Initialized ServletContext: " + context.getServletContextName());
		log.info("iText 2.1.7 register system's all font.");
		FontFactory.registerDirectories();

		String jpql = "SELECT COUNT(u) FROM User u";
		Long result = (Long) this.em.createQuery(jpql).getResultList().get(0);
		log.info("User表共有{}条数据", result);
		if (result == 0)
		{
			List<String> adminPermissionList = new ArrayList<String>();
			for (Permission p : Permission.values())
			{
				adminPermissionList.add(p.getExpression());
			}
			Role adminRole = new Role();
			adminRole.setName("administrator");
			adminRole.setDescription("administrator role");
			adminRole.setPermissions(adminPermissionList);
			adminRole = this.em.merge(adminRole);

			User user = new User();
			user.setName("zhaoqianjava@foxmail.com");
			user.setPassword("123");
			user.setRealName("ZhaoQian");
			user = PasswordHelper.generateUserPasswordHashAndSalt(user);
			Set<Role> roles = new HashSet<Role>();
			roles.add(adminRole);
			user.setRoles(roles);

			this.em.persist(user);
			log.info("add user and roles over.");
		}
	}

	public void onDestroy(@Observes @Destroyed ServletContext context)
	{

		log.info("Destroyed ServletContext: " + context.getServletContextName());
	}
}
