package de.dtonal.moneykeeper.dao;

import java.util.function.BiFunction;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import de.dtonal.moneykeeper.db.HibernateUtil;

public abstract class AbstractDao<E> {
	public E saveOrUpdate(E entity) {
		return doInSession((ent, s) -> {
			s.saveOrUpdate(entity);
			return entity;
		});
	}

	protected E findById(Class<E> entityClass, Long id) {
		return doInSession((ent, s) -> s.load(entityClass, id));
	}

	protected E findById(Class<E> entityClass, Long id, Session session) {
		return session.load(entityClass, id);
	}

	protected E doInSession(BiFunction<E, Session, E> doInSession) {
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		E entity = null;
		try (Session session = sessionFactory.openSession()) {
			session.beginTransaction();
			entity = doInSession.apply(entity, session);
			session.getTransaction().commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return entity;
	}

}
