package com.bavostepbros.leap.database;

import com.bavostepbros.leap.model.Capability;
import com.bavostepbros.leap.model.Environment;
import com.bavostepbros.leap.model.Status;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

public class CapabilityDAL {
    private static SessionFactory factory;

    public CapabilityDAL() {
        try {
            factory = new Configuration().configure().buildSessionFactory();
        } catch (Exception e) {
            System.out.println("Could not connect to database.");
        }
    }

    public Capability saveCapability(Capability capability) {
    	Session session = factory.openSession();
        Transaction transaction = null;
        Integer id = null;
    	try {
            transaction = session.beginTransaction();
            id = (Integer) session.save(capability);
            transaction.commit();
            capability.setCapabilityId(id);
    	} catch (HibernateException e) {
    		if (transaction != null) {
        		transaction.rollback();
        	}
        	e.printStackTrace();
    	} finally {
    		session.close();
    	}
        return capability;
    }

    public Capability getCapabilityById(Integer id) {
        Session session = factory.openSession();
        Capability capability = null;
        try {
        	Query<Capability> query = session.createQuery(
        			"SELECT c FROM Capability c WHERE capabilityId = '"+ id +"' ", 
        			Capability.class);
            capability = query.getSingleResult();
        } catch (HibernateException e) {
        	e.printStackTrace();
        } finally {
        	session.close();
        }
        return capability;
    }
    
    public List<Capability> getAllCapabilities() {
        Session session = factory.openSession();
        List<Capability> capabilities = null;
        try {
        	Query<Capability> query = session.createQuery(
        			"SELECT c FROM Capability c ", 
        			Capability.class);
            capabilities = query.getResultList();
        } catch (HibernateException e) {
        	e.printStackTrace();
        } finally {
        	session.close();
        }
        return capabilities;
    }
    
    public void updateCapability(Integer capabilityId, Environment environment, Status status, 
    Integer parentCapabilityId, String capabilityName, Integer level, boolean paceOfChange, 
    String targetOperatingModel, Integer resourceQuality, Integer informationQuality, Integer applicationFit) {
        Session session = factory.openSession();
        Transaction transaction = null;
        Capability capability = null;
        try {
        	transaction = session.beginTransaction();
        	capability = getCapabilityById(capabilityId);
            capability.setEnvironment(environment);
            capability.setStatus(status);
            capability.setParentCapabilityId(parentCapabilityId);
            capability.setCapabilityName(capabilityName);
            capability.setLevel(level);
            capability.setPaceOfChange(paceOfChange);
            capability.setTargetOperatingModel(targetOperatingModel);
            capability.setResourceQuality(resourceQuality);
            capability.setInformationQuality(informationQuality);
            capability.setApplicationFit(applicationFit);
            session.saveOrUpdate(capability);
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

    public void deleteCapability(Integer id) {
        Session session = factory.openSession();
        Transaction transaction = null;
        try {
        	transaction = session.beginTransaction();
        	Capability capability = getCapabilityById(id);
            session.delete(capability);
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