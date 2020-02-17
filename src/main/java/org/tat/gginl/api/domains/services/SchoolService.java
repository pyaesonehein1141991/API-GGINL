package org.tat.gginl.api.domains.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.tat.gginl.api.domains.School;
import org.tat.gginl.api.domains.repository.SchoolRepository;

public class SchoolService {
	
	@Autowired
	private SchoolRepository schoolRepository;
	
	
	public List<School> findAll(){
		return schoolRepository.findAll();
	}
	
	public List<Object[]> findAllNativeObject(){
		return schoolRepository.findAllNativeObject();
	}
	
	public List<Object> findAllColumnName(){
		return schoolRepository.findAllColumnName();
	}

}
