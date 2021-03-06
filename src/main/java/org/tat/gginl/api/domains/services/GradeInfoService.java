package org.tat.gginl.api.domains.services;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tat.gginl.api.domains.GradeInfo;
import org.tat.gginl.api.domains.repository.GradeInfoRepository;
import org.tat.gginl.api.exception.DAOException;
import org.tat.gginl.api.exception.ErrorCode;

@Service
public class GradeInfoService {

	@Autowired
	private GradeInfoRepository gradeInfoRepository;

	public List<GradeInfo> findAll() {
		return gradeInfoRepository.findAll();
	}

	public List<Object[]> findAllNativeObject() {
		return gradeInfoRepository.findAllNativeObject();
	}

	public List<Object> findAllColumnName() {
		return gradeInfoRepository.findAllColumnName();
	}

	@Transactional
	public Optional<GradeInfo> findById(String id) throws DAOException {
		if (!StringUtils.isBlank(id)) {
			if (gradeInfoRepository.findById(id).isPresent()) {
				return gradeInfoRepository.findById(id);
			}
			else {
				throw new DAOException(ErrorCode.SYSTEM_ERROR_RESOURCE_NOT_FOUND, id + " not found in GradeInfo");
			}
		}
		else {
			return Optional.empty();
		}

	}

}
