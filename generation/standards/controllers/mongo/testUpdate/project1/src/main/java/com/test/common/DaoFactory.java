package com.test.common;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;
import com.test.dao.AccountDao;
import com.test.dao.IAccountDao;
import com.test.dao.ILocationDao;
import com.test.dao.LocationDao;
import java.lang.Integer;
import java.lang.String;
import java.net.UnknownHostException;

class DaoFactory {
	private static String DATABASE_URL = "localhost";
	private static Integer DATABASE_PORT = 27017;
	private static String DATABASE_NAME = "com.liberty-database";
	private static Datastore datastore = null;

	static {
		try {
			Mongo mongo = new Mongo(DATABASE_URL, DATABASE_PORT);
			Morphia morphia = new Morphia();
			datastore = morphia.createDatastore(mongo, DATABASE_NAME);
		} catch(UnknownHostException e){
			System.err.println(e.getMessage());
		}
	}

	public static ILocationDao getLocationDao(){
		return new LocationDao(datastore);
	}

	public static IAccountDao getAccountDao(){
		return new AccountDao(datastore);
	}
}