package com.bavostepbros.leap.database;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import com.bavostepbros.leap.model.Environment;

public class EnvironmentDAL {
	private static SessionFactory factory;

	public EnvironmentDAL() {
		try {
			factory = new Configuration().configure().buildSessionFactory();
		} catch (Exception e) {
			System.out.println("Could not connect to database.");
		}
	}

	public Environment saveEnvironment(Environment environment) {
		Session session = factory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			Integer id = (Integer) session.save(environment);
			transaction.commit();
			environment.setEnvironmentId(id);
		} catch (HibernateException e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		return environment;
	}

	public Environment getEnvironmentById(Integer id) {
		Session session = factory.openSession();
		Environment environment = null;
		try {
			Query<Environment> query = session
					.createQuery("SELECT e FROM Environment e WHERE environmentId = '" + id + "'", Environment.class);
			environment = query.getSingleResult();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return environment;
	}

	public void updateEnvironment(Integer environmentId, String environmentName) {
		Session session = factory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			Environment environment = getEnvironmentById(environmentId);
			environment.setEnvironmentName(environmentName);
			session.saveOrUpdate(environment);
			transaction.commit();
		} catch (HibernateException e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	public void deleteEnvironment(Integer environmentId) {
		Session session = factory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			Environment environment = getEnvironmentById(environmentId);
			session.delete(environment);
			transaction.commit();
		} catch (HibernateException e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	public void close() {
		factory.close();
	}
}
