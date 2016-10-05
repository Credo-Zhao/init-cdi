
package org.zhaoqian.security.shiro.extension.tags;

import javax.faces.view.facelets.TagConfig;

import org.apache.shiro.subject.Subject;

/**
 * Displays body content if the current user has any of the roles specified.
 *
 * @author Deluan Quintao
 * @author Jeremy Haile
 */
public class HasAnyRolesTag extends PermissionTagHandler {

	// TODO - complete JavaDoc

	// Delimeter that separates role names in tag attribute
	private static final String ROLE_NAMES_DELIMETER = ",";

	public HasAnyRolesTag(TagConfig config) {

		super(config);
	}

	@Override
	protected boolean showTagBody(String roleNames) {

		boolean hasAnyRole = false;

		Subject subject = getSubject();

		if ( subject != null )
		{
			// Iterate through roles and check to see if the user has one of the roles
			for ( String role : roleNames.split(ROLE_NAMES_DELIMETER) )
			{
				if ( subject.hasRole(role.trim()) )
				{
					hasAnyRole = true;
					break;
				}
			}
		}

		return hasAnyRole;
	}

}
