package org.tat.gginl.api.domains.services;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.asm.internal.Relationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tat.gginl.api.domains.Organization;
import org.tat.gginl.api.domains.Province;
import org.tat.gginl.api.domains.RelationShip;
import org.tat.gginl.api.domains.repository.RelationshipRepository;
import org.tat.gginl.api.exception.DAOException;
import org.tat.gginl.api.exception.ErrorCode;

@Service
public class RelationshipService {
	
	@Autowired
	private RelationshipRepository relationshipRepository;
	

	public List<Object[]> findAllNativeObject(){
		return relationshipRepository.findAllNativeObject();
	}
	
	public List<Object> findAllColumnName(){
		return relationshipRepository.findAllColumnName();
	}
	
	@Transactional
	public Optional<RelationShip>  findById(String id) throws DAOException {
		if(!StringUtils.isBlank(id)) {
		if(relationshipRepository.findById(id).isEmpty()) {
			throw new DAOException(ErrorCode.SYSTEM_ERROR_RESOURCE_NOT_FOUND, id + " not found in relationship");
		}else {
			return relationshipRepository.findById(id);
		 }
		} else {
		 return Optional.empty();
	 }
	
		}


}
