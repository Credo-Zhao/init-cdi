package org.zhaoqian.security.model.enums;

public enum Permission
{
	USER("user:*", "用户管理模块所有权限"), 
	USER_VIEW("user:view", "用户管理模块查看权限"), 
	ROLE("role:*","权限管理模块所有权限"), 
	ROLE_VIEW("role:view", "权限管理模块查看权限");

	private String expression;

	private String description;

	private Permission(String expression, String description)
	{
		this.expression = expression;
		this.description = description;
	}

	public String getExpression()
	{
		return this.expression;
	}

	public String getDescription()
	{
		return this.description;
	}
}
