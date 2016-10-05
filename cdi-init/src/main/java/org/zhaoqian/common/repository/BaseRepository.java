package org.zhaoqian.common.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;

public abstract class BaseRepository<T> implements Serializable
{
	private static final long serialVersionUID = 5345573195673186480L;

	@Inject
	protected Logger logger;

	@Inject
	protected EntityManager em;

	public List<T> queryAllEntity(Class<T> entityClass)
	{
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
		Root<T> entityRoot = criteriaQuery.from(entityClass);
		criteriaQuery.select(entityRoot);
		TypedQuery<T> typedQuery = em.createQuery(criteriaQuery);
		return typedQuery.getResultList();
	}

	@SuppressWarnings("hiding")
	public <T> T queryEntityById(Class<T> entityClass, final Serializable id)
	{
		return em.find(entityClass, id);
	}

	public T merge(T t)
	{
		T result = em.merge(t);
		em.flush();
		return result;
	}

	public void persist(T t)
	{
		em.persist(t);
		em.flush();
	}

	public void delete(T t)
	{
		em.remove(em.merge(t));
	}

	public boolean deleteEntityById(Class<T> entityClass, final Serializable id, String primaryKeyName)
	{
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaDelete<T> criteriaDelete = criteriaBuilder.createCriteriaDelete(entityClass);

		// 构建Form子句
		Root<T> root = criteriaDelete.from(entityClass);
		Predicate predicate = criteriaBuilder.equal(root.get(primaryKeyName), id);
		criteriaDelete.where(predicate);

		Query query = em.createQuery(criteriaDelete);
		int rowCount = query.executeUpdate();
		return rowCount > 0;
	}

}
