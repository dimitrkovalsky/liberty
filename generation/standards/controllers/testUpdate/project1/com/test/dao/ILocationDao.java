package com.test.dao;

import com.test.model.Location;
import com.test.errors.DaoException;
import java.lang.Integer;

interface ILocationDao {

	public void insert(Location entity) throws DaoException;

	public void find(Location entity) throws DaoException;

	public void findAll() throws DaoException;

	public void findByAccountId(Integer accountId) throws DaoException;

	public void update(Location entity) throws DaoException;

	public void delete(Location entity) throws DaoException;
}