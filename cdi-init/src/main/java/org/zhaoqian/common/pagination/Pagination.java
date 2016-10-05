
package org.zhaoqian.common.pagination;

import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;

public abstract class Pagination<T> implements java.io.Serializable {

	private static final long serialVersionUID = -1314576150546939352L;

	@Inject
	protected EntityManager em;

	@Inject
	protected Logger log;

	private LazyDataModel<T> lazyDataModel;

	private DataTable dataTable;

	private T[] selectBeans;

	private T selectBean;

	private int first;

	private int rows;

	private Object sortBy;

	private String sortOrder;

	public DataTable getDataTable() {

		return dataTable;
	}

	public void setDataTable(DataTable dataTable) {

		this.dataTable = dataTable;
		onRestorePaginationState(dataTable);
	}

	public void query() {

		this.getDataTable().reset();
	}

	protected abstract Class<T> getEntityClass();

	protected abstract Predicate[] getSearchPredicates(CriteriaBuilder criteriaBuilder, CriteriaQuery<Object> criteriaQuery, Root<T> root);

	@PostConstruct
	protected void initialize() {

		this.lazyDataModel = new LazyDataModel<T>() {

			private static final long serialVersionUID = 1L;

			@Override
			public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

				List<T> list = loadData(first, pageSize, sortField, sortOrder, filters);
				onSavePaginationState(dataTable);
				return list;
			}

			@Override
			public List<T> load(int first, int pageSize, List<SortMeta> multiSortMeta, Map<String, Object> filters) {

				return super.load(first, pageSize, multiSortMeta, filters);
			}

			@Override
			public T getRowData(String rowKey) {

				return getSelectRowData(rowKey);
			}

			@Override
			public Object getRowKey(T object) {

				return getSelectRowKey(object);
			}

			@Override
			public void setRowIndex(final int rowIndex) {

				if ( rowIndex == -1 || getPageSize() == 0 )
				{
					super.setRowIndex(-1);
				} else
				{
					super.setRowIndex(rowIndex % getPageSize());
				}
			}

		};
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<T> loadData(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Object> criteriaQuery = criteriaBuilder.createQuery();
		Root<T> root = criteriaQuery.from(getEntityClass());

		Predicate[] predicates = getSearchPredicates(criteriaBuilder, criteriaQuery, root);

		criteriaQuery.select(criteriaBuilder.count(root)).where(predicates);
		TypedQuery countQuery = em.createQuery(criteriaQuery);

		lazyDataModel.setRowCount(( (Long) countQuery.getSingleResult() ).intValue());

		criteriaQuery.select(root);
		if ( sortField != null )
		{
			StringTokenizer tokenizer = new StringTokenizer(sortField, ".");
			Path<Object> path = root.get(tokenizer.nextToken());
			while ( tokenizer.hasMoreTokens() )
			{
				path = path.get(tokenizer.nextToken());
			}

			if ( sortOrder.name().equals("ASCENDING") )
			{
				criteriaQuery.orderBy(criteriaBuilder.asc(path));
			} else if ( sortOrder.name().equals("DESCENDING") )
			{
				criteriaQuery.orderBy(criteriaBuilder.desc(path));
			}
		}

		TypedQuery resultQuery = em.createQuery(criteriaQuery);
		return resultQuery.setFirstResult(first).setMaxResults(pageSize).getResultList();
	}

	protected T getSelectRowData(String rowKey) {

		throw new UnsupportedOperationException("getRowData(String rowKey) must be implemented when basic rowKey algorithm is not used.");
	}

	protected Object getSelectRowKey(T object) {

		throw new UnsupportedOperationException("getRowKey(T object) must be implemented when basic rowKey algorithm is not used.");
	}

	protected void onRestorePaginationState(DataTable dataTable) {

		dataTable.setFirst(getFirst());
		dataTable.setRows(getRows());
		dataTable.setSortBy(getSortBy());
		dataTable.setSortOrder(getSortOrder());
	};

	protected void onSavePaginationState(DataTable dataTable) {

		setFirst(dataTable.getFirst());
		setRows(dataTable.getRows());
		setSortBy(dataTable.getSortBy());
		setSortOrder(dataTable.getSortOrder());
	};

	public LazyDataModel<T> getLazyDataModel() {

		return lazyDataModel;
	}

	public void setLazyDataModel(LazyDataModel<T> lazyDataModel) {

		this.lazyDataModel = lazyDataModel;
	}

	public T[] getSelectBeans() {

		return selectBeans;
	}

	public void setSelectBeans(T[] selectBeans) {

		this.selectBeans = selectBeans;
	}

	public T getSelectBean() {

		return selectBean;
	}

	public void setSelectBean(T selectBean) {

		this.selectBean = selectBean;
	}

	public int getFirst() {

		return first;
	}

	public void setFirst(int first) {

		this.first = first;
	}

	public int getRows() {

		return rows;
	}

	public void setRows(int rows) {

		this.rows = rows;
	}

	public Object getSortBy() {

		return sortBy;
	}

	public void setSortBy(Object sortBy) {

		this.sortBy = sortBy;
	}

	public String getSortOrder() {

		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {

		this.sortOrder = sortOrder;
	}
}
