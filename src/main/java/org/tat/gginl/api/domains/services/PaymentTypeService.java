package org.tat.gginl.api.domains.services;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tat.gginl.api.domains.PaymentType;
import org.tat.gginl.api.domains.repository.PaymentTypeRepository;
import org.tat.gginl.api.exception.DAOException;
import org.tat.gginl.api.exception.ErrorCode;

@Service
public class PaymentTypeService {

	@Autowired
	private PaymentTypeRepository paymentTypeRepository;

	public List<PaymentType> findAll() {
		return paymentTypeRepository.findAll();
	}

	public List<Object[]> findAllNativeObject() {
		return paymentTypeRepository.findAllNativeObject();
	}

	public List<Object> findAllColumnName() {
		return paymentTypeRepository.findAllColumnName();
	}

	@Transactional
	public Optional<PaymentType> findById(String id) throws DAOException {
		if (!StringUtils.isBlank(id)) {
			if (paymentTypeRepository.findById(id).isPresent()) {
				return paymentTypeRepository.findById(id);
			}
			else {
				throw new DAOException(ErrorCode.SYSTEM_ERROR_RESOURCE_NOT_FOUND, id + " not found in PamentType");
			}
		}
		else {
			return Optional.empty();
		}

	}

}
