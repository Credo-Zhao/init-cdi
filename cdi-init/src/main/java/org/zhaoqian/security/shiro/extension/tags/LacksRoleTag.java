
package org.zhaoqian.security.shiro.extension.tags;

import javax.faces.view.facelets.TagConfig;

/**
 * @author Deluan Quintao
 */
public class LacksRoleTag extends PermissionTagHandler
{

	// TODO - complete JavaDoc

	public LacksRoleTag(TagConfig config)
	{

		super(config);
	}

	@Override
	protected boolean showTagBody(String roleName)
	{

		boolean hasRole = getSubject() != null && getSubject().hasRole(roleName);
		return !hasRole;
	}

}