package com.bavostepbros.leap.domain.service.informationservice;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bavostepbros.leap.domain.model.Information;
import com.bavostepbros.leap.persistence.InformationDAL;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class InformationServiceImpl implements InformationService {
	
	@Autowired
	private InformationDAL informationDAL;
	
	@Override
	public Information save(String informationName, String informationDescription) {
		Information information = new Information(informationName, informationDescription);
		return informationDAL.save(information);
	}

	@Override
	public Information get(Integer informationId) {
		Optional<Information> information = informationDAL.findById(informationId);
		information.orElseThrow(() -> new NullPointerException("Information does not exist."));
		return information.get();
	}

	@Override
	public Information update(Integer informationId, String informationName, String informationDescription) {
		Information information = new Information(informationId, informationName, informationDescription);
		return informationDAL.save(information);
	}

	@Override
	public void delete(Integer informationId) {
		informationDAL.deleteById(informationId);
	}

	@Override
	public Information getInformationByName(String informationName) {
		Optional<Information> information = informationDAL.findByInformationName(informationName);
		information.orElseThrow(() -> new NullPointerException("Information does not exist."));
		return information.get();
	}

	@Override
	public List<Information> getAll() {
		return informationDAL.findAll();
	}

}
