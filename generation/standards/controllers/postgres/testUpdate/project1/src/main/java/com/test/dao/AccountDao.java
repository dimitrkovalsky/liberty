package com.test.dao;

import com.test.errors.DaoException;
import com.test.model.Account;
import java.lang.Exception;
import java.lang.Integer;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;

class AccountDao implements IAccountDao {
	private EntityManager entityManager = null;

	public void AccountDao(EntityManager em){
		this.entityManager = em;
	}

	public void insert(Account entity) throws DaoException {
		try {
			entityManager.getTransaction().begin();
			entityManager.persist(entity);
			entityManager.getTransaction().commit();
		} catch(Exception e){
			throw new DaoException(e);
		}
	}

	public Account find(Account entity) throws DaoException {
		try {
			return entityManager.find(Account.class, entity.getInternalId());
		} catch(Exception e){
			throw new DaoException(e);
		}
	}

	public List<Account> findAll() throws DaoException {
		try {
			CriteriaQuery<Account> criteria = entityManager.getCriteriaBuilder().createQuery(Account.class);
			criteria.select(criteria.from(Account.class));
			return entityManager.createQuery(criteria).getResultList();
		} catch(Exception e){
			throw new DaoException(e);
		}
	}

	public Account findByInternalId(Integer internalId) throws DaoException {
		try {
			return entityManager.find(Account.class, internalId);
		} catch(Exception e){
			throw new DaoException(e);
		}
	}

	public void update(Account entity) throws DaoException {
		try {
			entityManager.getTransaction().begin();
			entityManager.merge(entity);
			entityManager.getTransaction().commit();
		} catch(Exception e){
			throw new DaoException(e);
		}
	}

	public void delete(Account entity) throws DaoException {
		try {
			entityManager.getTransaction().begin();
			entityManager.remove(entity);
			entityManager.getTransaction().commit();
		} catch(Exception e){
			throw new DaoException(e);
		}
	}
}