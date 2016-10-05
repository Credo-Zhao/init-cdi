package org.zhaoqian.common;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

public class Resources
{
	@PersistenceUnit
	private EntityManagerFactory entityManagerFactory;

	@Produces
	@RequestScoped
	public EntityManager createEntityManager()
	{
		return entityManagerFactory.createEntityManager();
	}

	public void closeEntityManager(@Disposes EntityManager entityManager)
	{
		if (entityManager.isOpen())
		{
			entityManager.close();
		}
	}

	@Produces
	public org.slf4j.Logger createLogger(InjectionPoint injectionPoint)
	{
		return org.slf4j.LoggerFactory.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
	}

	@Produces
	@RequestScoped
	public FacesContext produceFacesContext()
	{
		return FacesContext.getCurrentInstance();
	}

}
