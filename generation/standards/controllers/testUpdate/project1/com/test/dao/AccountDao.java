package com.test.dao;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.dao.BasicDAO;
import com.test.errors.DaoException;
import com.test.model.Account;
import java.lang.Exception;
import java.lang.Integer;

class AccountDao extends BasicDAO<Account, Integer> implements IAccountDao {
	public void AccountDao(Datastore datastore){
		super(datastore);
	}

	public void insert(Account entity) throws DaoException {
		try {
			super.save(entity);
		} catch(Exception e){
			throw new DaoException(e);
		}
	}

	public void find(Account entity) throws DaoException {
		try {
			return super.findOne("_id", entity.getInternalId());
		} catch(Exception e){
			throw new DaoException(e);
		}
	}

	public void findAll() throws DaoException {
		try {
			getCollection().find(Account.class).asList();
		} catch(Exception e){
			throw new DaoException(e);
		}
	}

	public void findByInternalId(Integer internalId) throws DaoException {
		try {
			return super.findOne("_id", internalId);
		} catch(Exception e){
			throw new DaoException(e);
		}
	}

	public void update(Account entity) throws DaoException {
		try {
			super.save(entity);
		} catch(Exception e){
			throw new DaoException(e);
		}
	}

	public void delete(Account entity) throws DaoException {
		try {
			getCollection().remove(new BasicDBObject().append("internalId", entity.getInternalId()));
		} catch(Exception e){
			throw new DaoException(e);
		}
	}
}