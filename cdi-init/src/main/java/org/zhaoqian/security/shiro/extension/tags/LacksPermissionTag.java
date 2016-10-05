
package org.zhaoqian.security.shiro.extension.tags;

import javax.faces.view.facelets.TagConfig;

/**
 * @author Deluan Quintao
 */
public class LacksPermissionTag extends PermissionTagHandler
{

	// TODO - complete JavaDoc

	public LacksPermissionTag(TagConfig config)
	{

		super(config);
	}

	@Override
	protected boolean showTagBody(String p)
	{

		return !isPermitted(p);
	}

}
