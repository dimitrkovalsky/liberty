package com.test.dao;

import com.test.errors.DaoException;
import com.test.model.Location;
import java.lang.Exception;
import java.lang.Integer;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;

class LocationDao implements ILocationDao {
	private EntityManager entityManager = null;

	public void LocationDao(EntityManager em){
		this.entityManager = em;
	}

	public void insert(Location entity) throws DaoException {
		try {
			entityManager.getTransaction().begin();
			entityManager.persist(entity);
			entityManager.getTransaction().commit();
		} catch(Exception e){
			throw new DaoException(e);
		}
	}

	public Location find(Location entity) throws DaoException {
		try {
			return entityManager.find(Location.class, entity.getAccountId());
		} catch(Exception e){
			throw new DaoException(e);
		}
	}

	public List<Location> findAll() throws DaoException {
		try {
			CriteriaQuery<Location> criteria = entityManager.getCriteriaBuilder().createQuery(Location.class);
			criteria.select(criteria.from(Location.class));
			return entityManager.createQuery(criteria).getResultList();
		} catch(Exception e){
			throw new DaoException(e);
		}
	}

	public Location findByAccountId(Integer accountId) throws DaoException {
		try {
			return entityManager.find(Location.class, accountId);
		} catch(Exception e){
			throw new DaoException(e);
		}
	}

	public void update(Location entity) throws DaoException {
		try {
			entityManager.getTransaction().begin();
			entityManager.merge(entity);
			entityManager.getTransaction().commit();
		} catch(Exception e){
			throw new DaoException(e);
		}
	}

	public void delete(Location entity) throws DaoException {
		try {
			entityManager.getTransaction().begin();
			entityManager.remove(entity);
			entityManager.getTransaction().commit();
		} catch(Exception e){
			throw new DaoException(e);
		}
	}
}