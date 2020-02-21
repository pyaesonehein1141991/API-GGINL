package org.tat.gginl.api.domains.services;

import java.util.List;
import java.util.Optional;

import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tat.gginl.api.domains.Branch;
import org.tat.gginl.api.domains.repository.BranchRepository;
import org.tat.gginl.api.exception.DAOException;
import org.tat.gginl.api.exception.SystemException;

@Service
public class BranchService {
	
	@Autowired
	private BranchRepository repository;
	
	public List<Branch> findAll(){
		return repository.findAll();
	}
	
	public List<Object[]> findAllNativeObject(){
		return repository.findAllNativeObject();
	}
	
	public List<Object> findAllColumnName(){
		return repository.findAllColumnName();
	}
	
	
	@Transactional
	public Optional<Branch>  finById(String id) throws DAOException {
		if(repository.findById(id).isEmpty()) {
			throw new DAOException("dddddd", id + " not found in Branch");
		}else {
			return repository.findById(id);
		}
}
	
}
