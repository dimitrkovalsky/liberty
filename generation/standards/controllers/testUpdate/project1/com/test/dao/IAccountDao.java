package com.test.dao;

import com.test.model.Account;
import com.test.errors.DaoException;
import java.lang.Integer;

interface IAccountDao {

	public void insert(Account entity) throws DaoException;

	public void find(Account entity) throws DaoException;

	public void findAll() throws DaoException;

	public void findByInternalId(Integer internalId) throws DaoException;

	public void update(Account entity) throws DaoException;

	public void delete(Account entity) throws DaoException;
}