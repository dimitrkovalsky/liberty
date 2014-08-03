package com.test.dao;

import com.test.model.Location;
import java.util.List;
import java.lang.Integer;
import com.test.errors.DaoException;

interface ILocationDao {

	public void insert(Location entity) throws DaoException;

	public Location find(Location entity) throws DaoException;

	public List<Location> findAll() throws DaoException;

	public Location findByAccountId(Integer accountId) throws DaoException;

	public void update(Location entity) throws DaoException;

	public void delete(Location entity) throws DaoException;
}