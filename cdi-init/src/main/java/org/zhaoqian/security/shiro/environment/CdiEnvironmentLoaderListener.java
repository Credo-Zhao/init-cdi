package org.zhaoqian.security.shiro.environment;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.apache.shiro.authc.pam.AllSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.authz.permission.WildcardPermissionResolver;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.web.env.DefaultWebEnvironment;
import org.apache.shiro.web.env.EnvironmentLoaderListener;
import org.apache.shiro.web.env.WebEnvironment;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.slf4j.Logger;
import org.zhaoqian.security.shiro.credential.CustomCredentialsMatcher;
import org.zhaoqian.security.shiro.realm.JpaRealm;

/**
 * @author ZhaoQian
 */
public class CdiEnvironmentLoaderListener extends EnvironmentLoaderListener
{
	@Inject
	private JpaRealm jpaRealm;

	@Inject
	private Logger log;

	@Override
	protected WebEnvironment createEnvironment(ServletContext pServletContext)
	{
		log.info("Init shiro environment,Integrated into the CDI container....");
		WebEnvironment environment = super.createEnvironment(pServletContext);

		log.info("Init shiro SecurityManager....");
		// 设置SecurityManager
		DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) environment.getSecurityManager();

		log.info("Init shiro SecurityManager's authenticator....");
		// 设置securityManager的authenticator
		ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
		authenticator.setAuthenticationStrategy(new AllSuccessfulStrategy());
		securityManager.setAuthenticator(authenticator);

		// 设置securityManager的authorizer
		ModularRealmAuthorizer authorizer = new ModularRealmAuthorizer();
		authorizer.setPermissionResolver(new WildcardPermissionResolver());
		securityManager.setAuthorizer(authorizer);

		// 设置securityManager CacheManager，缓存。
		EhCacheManager cacheManager = new EhCacheManager();
		cacheManager.setCacheManagerConfigFile("classpath:ehcache.xml");
		securityManager.setCacheManager(cacheManager);

		// 设置securityManager RememberMeManager cookie
		SimpleCookie rememberMeCookie = new SimpleCookie();
		rememberMeCookie.setName("loginCookie");
		rememberMeCookie.setHttpOnly(true);
		rememberMeCookie.setMaxAge(2592000);// 30 days
		CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
		cookieRememberMeManager.setCookie(rememberMeCookie);
		securityManager.setRememberMeManager(cookieRememberMeManager);

		// 设置securityManager realm
		// 设置CredentialsMatcher参数,realm使用。
		CustomCredentialsMatcher customCredentialsMatcher = new CustomCredentialsMatcher();
		customCredentialsMatcher.setHashAlgorithmName("SHA-512");
		customCredentialsMatcher.setHashIterations(99);
		customCredentialsMatcher.setStoredCredentialsHexEncoded(true);
		jpaRealm.setCredentialsMatcher(customCredentialsMatcher);
		jpaRealm.setCachingEnabled(true);
		jpaRealm.setAuthenticationCachingEnabled(true);
		jpaRealm.setAuthenticationCacheName("authenticationCache");// 指定在ehcahe.xml中的名字
		jpaRealm.setAuthorizationCachingEnabled(true);
		jpaRealm.setAuthorizationCacheName("authorizationCache");// 指定在ehcahe.xml中的名字
		securityManager.setRealm(jpaRealm);

		// securityManager的会话管理器未做配置。
		((DefaultWebEnvironment) environment).setSecurityManager(securityManager);

		log.info("Init shiro environment successful....");
		return environment;
	}
}
