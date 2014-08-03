package com.test.dao;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.dao.BasicDAO;
import com.test.errors.DaoException;
import com.test.model.Location;
import java.lang.Exception;
import java.lang.Integer;
import java.util.List;

class LocationDao extends BasicDAO<Location, Integer> implements ILocationDao {
	public void LocationDao(Datastore datastore){
		super(datastore);
	}

	public void insert(Location entity) throws DaoException {
		try {
			super.save(entity);
		} catch(Exception e){
			throw new DaoException(e);
		}
	}

	public Location find(Location entity) throws DaoException {
		try {
			return super.findOne("_id", entity.getAccountId());
		} catch(Exception e){
			throw new DaoException(e);
		}
	}

	public List<Location> findAll() throws DaoException {
		try {
			return getCollection().find(Location.class).asList();
		} catch(Exception e){
			throw new DaoException(e);
		}
	}

	public Location findByAccountId(Integer accountId) throws DaoException {
		try {
			return super.findOne("_id", accountId);
		} catch(Exception e){
			throw new DaoException(e);
		}
	}

	public void update(Location entity) throws DaoException {
		try {
			super.save(entity);
		} catch(Exception e){
			throw new DaoException(e);
		}
	}

	public void delete(Location entity) throws DaoException {
		try {
			getCollection().remove(new BasicDBObject().append("accountId", entity.getAccountId()));
		} catch(Exception e){
			throw new DaoException(e);
		}
	}
}