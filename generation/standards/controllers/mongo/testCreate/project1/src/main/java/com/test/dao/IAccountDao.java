package com.test.dao;

import java.util.List;
import java.lang.Integer;
import com.test.errors.DaoException;
import com.test.model.Account;

interface IAccountDao {

	public void insert(Account entity) throws DaoException;

	public Account find(Account entity) throws DaoException;

	public List<Account> findAll() throws DaoException;

	public Account findByInternalId(Integer internalId) throws DaoException;

	public void update(Account entity) throws DaoException;

	public void delete(Account entity) throws DaoException;
}