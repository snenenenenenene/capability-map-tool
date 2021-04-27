package com.bavostepbros.leap.database;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import com.bavostepbros.leap.model.Status;

public class StatusDAL {	
	private static SessionFactory factory;
	
	public StatusDAL() {
		try {
            factory = new Configuration().configure().buildSessionFactory();
        } catch (Exception e) {
            System.out.println("Could not connect to database.");
        }
	}
	
	public Status saveStatus(Status status) {
		Session session = factory.openSession();
		Transaction transaction = session.beginTransaction();
		Integer id = (Integer) session.save(status);
		transaction.commit();
        session.close();
        status.setStatusId(id);
        return status;
	}
	
	public Status getStatusById(Integer id) {
		Session session = factory.openSession();
		Query<Status> query = session.createQuery("SELECT s FROM Status s WHERE statusId = '"+ id +"'", Status.class);
		Status status = query.getSingleResult();
		return status;
	}
	
	public void updateStatus(Integer statusId, Integer validityPeriod) {
		Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        Status status = getStatusById(statusId);
        status.setValidityPeriod(validityPeriod);
        session.update(status);
        transaction.commit();
        session.close();
	}
	
	public void deleteEnvironment(Integer statusId) {
		Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        Status status = getStatusById(statusId);
        session.delete(status);
        transaction.commit();
        session.close();
	}
	
	public void close() {
        factory.close();
    }

}
