package org.zhaoqian.security.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.zhaoqian.common.qualifier.Repository;
import org.zhaoqian.common.repository.BaseRepository;
import org.zhaoqian.security.model.Role;
import org.zhaoqian.security.model.Role_;

/**
 * @author ZhaoQian
 */
@Repository
public class RoleRepository extends BaseRepository<Role>
{
	private static final long serialVersionUID = -6237936295038942317L;

	public boolean checkRoleNameIsExist(String roleName, Long roleId)
	{
		// String jpql =
		// "SELECT r FROM Role r WHERE r.name=:roleName and r.id <> :roleId";
		// int count = super.em.createQuery(jpql).setParameter("roleName",
		// roleName).setParameter("roleId", roleId).getResultList().size();

		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Role> criteriaQuery = criteriaBuilder.createQuery(Role.class);
		Root<Role> entityRoot = criteriaQuery.from(Role.class);

		List<Predicate> predicatesList = new ArrayList<Predicate>();
		Predicate roleNamePredicate = criteriaBuilder.and(criteriaBuilder.equal(entityRoot.get(Role_.name), roleName));
		predicatesList.add(roleNamePredicate);
		if (roleId != null)
		{
			Predicate roleIdPredicate = criteriaBuilder.and(criteriaBuilder.notEqual(entityRoot.get(Role_.id), roleId));
			predicatesList.add(roleIdPredicate);
		}

		criteriaQuery.select(entityRoot);
		criteriaQuery.where(predicatesList.toArray(new Predicate[predicatesList.size()]));

		TypedQuery<Role> typedQuery = em.createQuery(criteriaQuery);
		int count = typedQuery.getResultList().size();

		return count != 0;
	}
}
