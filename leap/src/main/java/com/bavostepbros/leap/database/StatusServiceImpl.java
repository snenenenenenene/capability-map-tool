package com.bavostepbros.leap.database;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bavostepbros.leap.model.Status;

@Service
@Transactional
public class StatusServiceImpl implements StatusService {
	
	private final StatusDAL statusDAL;	
	
	@Autowired
	public StatusServiceImpl(StatusDAL statusDAL) {
		super();
		this.statusDAL = statusDAL;
	}

	@Override
	public void save(Status status) {
		statusDAL.save(status);
	}

	@Override
	public Status get(Integer id) {
		Status status = statusDAL.findById(id).get();
		return status;
	}

	@Override
	public void update(Status status) {
		statusDAL.save(status);
	}

	@Override
	public void delete(Status status) {
		statusDAL.delete(status);
	}

}
