package com.test.common;

import com.test.dao.AccountDao;
import com.test.dao.IAccountDao;
import java.lang.String;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

class DaoFactory {
	private static String PERSISTENT_UNIT = "PostgresUnit";
	private static EntityManager em = null;

	static {
		em = Persistence.createEntityManagerFactory(PERSISTENT_UNIT).createEntityManager();
	}

	public static IAccountDao getAccountDao(){
		return new AccountDao(em);
	}
}