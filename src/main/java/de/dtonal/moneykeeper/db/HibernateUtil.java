package de.dtonal.moneykeeper.db;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	private static SessionFactory sessionFactory;

	public static String configName = "hibernate.cfg.xml";

	private HibernateUtil() {
	}

	public static synchronized SessionFactory getSessionFactory() {
		if (sessionFactory == null) {
			sessionFactory = new Configuration().configure(configName).buildSessionFactory();
		}
		return sessionFactory;
	}
}
