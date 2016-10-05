
package org.zhaoqian.security.shiro.extension.tags;

import javax.faces.view.facelets.TagConfig;

/**
 * @author Deluan Quintao
 */
public class HasRoleTag extends PermissionTagHandler {

	// TODO - complete JavaDoc

	public HasRoleTag(TagConfig config) {

		super(config);
	}

	@Override
	protected boolean showTagBody(String roleName) {

		return getSubject() != null && getSubject().hasRole(roleName);
	}

}