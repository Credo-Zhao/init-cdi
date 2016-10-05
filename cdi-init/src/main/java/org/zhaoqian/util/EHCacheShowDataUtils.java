/**
 * <p>Copyright (c) 2015 ZhaoQian.All Rights Reserved.</p>
 * @author <a href="zhaoqianjava@foxmail.com">ZhaoQian</a>
 */
package org.zhaoqian.util;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

import org.slf4j.Logger;

/**
 * @author ZhaoQian
 */
@RequestScoped
public class EHCacheShowDataUtils
{
	@Inject
	private Logger log;

	public void showEhcache()
	{
		log.info("=====================");
		CacheManager cacheManager = CacheManager.create();

		cacheManager = CacheManager.getInstance();
		// 获取缓存管理器中的缓存配置名称
		for (String cacheName : cacheManager.getCacheNames())
		{
			log.info("cacheName:" + cacheName);
		}

		Cache authorizationCache = cacheManager.getCache("authorizationCache");
		// 获取所有的缓存对象
		for (Object key : authorizationCache.getKeys())
		{
			log.info("authorizationCache key:" + key + "value:" + authorizationCache.get(key));
		}

		// 得到缓存中的对象数
		log.info("" + authorizationCache.getSize());
		// 得到缓存对象占用内存的大小
		log.info("" + authorizationCache.getMemoryStoreSize());
		// 得到缓存读取的命中次数
		log.info("" + authorizationCache.getStatistics().getCacheHits());
		// 得到缓存读取的错失次数
		log.info("" + authorizationCache.getStatistics().getCacheMisses());
		Cache authenticationCache = cacheManager.getCache("authenticationCache");
		// 获取所有的缓存对象
		for (Object key : authenticationCache.getKeys())
		{
			log.info("authenticationCache key:" + key + "value:" + authenticationCache.get(key));
		}
		log.info("=====================");
	}
}
