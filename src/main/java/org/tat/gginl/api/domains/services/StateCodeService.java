package org.tat.gginl.api.domains.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tat.gginl.api.domains.StateCode;
import org.tat.gginl.api.domains.TownshipCode;
import org.tat.gginl.api.domains.repository.StateCodeRepository;
import org.tat.gginl.api.exception.DAOException;
import org.tat.gginl.api.exception.ErrorCode;

@Service
public class StateCodeService {

	@Autowired
	private StateCodeRepository stateCodeRepository;
	
	@Transactional
	public Optional<StateCode>  finById(String id) throws DAOException {
		if(id !=null) {
		if(stateCodeRepository.findById(id).isEmpty()) {
			throw new DAOException(ErrorCode.SYSTEM_ERROR_RESOURCE_NOT_FOUND, id + " not found in StateCode");
		}else {
			return stateCodeRepository.findById(id);
		 }
		} else {
		 return Optional.empty();
	 }
	
		}
	
	@Transactional
	public Optional<StateCode> findByCodeNo(String townshipcodeno) {
		if(townshipcodeno !=null) {
			if(stateCodeRepository.findByCodeNo(townshipcodeno).isEmpty()) {
				throw new DAOException(ErrorCode.SYSTEM_ERROR_RESOURCE_NOT_FOUND, townshipcodeno + " not found in StateCode");
			}else {
				return stateCodeRepository.findByCodeNo(townshipcodeno);
			 }
			} else {
			 return Optional.empty();
		 }
	}
	
}
