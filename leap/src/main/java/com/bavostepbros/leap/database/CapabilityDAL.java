package com.bavostepbros.leap.database;

import com.bavostepbros.leap.model.Capability;
import com.bavostepbros.leap.model.Environment;
import com.bavostepbros.leap.model.Status;

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
        Transaction transaction = session.beginTransaction();
        Integer id = (Integer) session.save(capability);
        transaction.commit();
        session.close();
        capability.setCapabilityId(id);
        return capability;
    }

    public Capability getCapabilityById(Integer id) {
        Session session = factory.openSession();
        Query<Capability> query = session.createQuery("SELECT c FROM Capability WHERE capabilityId = '"+ id +"' ", Capability.class);
        Capability capability = query.getSingleResult();
        return capability;
    }

    public void updateCapability(Integer capabilityId, Environment environment, Status status, 
    Integer parentCapabilityId, String capabilityName, Integer level, boolean paceOfChange, 
    String targetOperatingModel, Integer resourceQuality, Integer informationQuality, Integer applicationFit) {
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        Capability capability = getCapabilityById(capabilityId);
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
        session.update(capability);
        transaction.commit();
        session.close();
    }

    public void deleteCapability(Integer id) {
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        Capability capability = getCapabilityById(id);
        session.delete(capability);
        transaction.commit();
        session.close();
    }

    public void close() {
        factory.close();
    }
}