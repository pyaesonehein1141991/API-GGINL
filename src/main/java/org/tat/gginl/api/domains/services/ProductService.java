package org.tat.gginl.api.domains.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tat.gginl.api.domains.Product;
import org.tat.gginl.api.domains.repository.ProductRepository;
import org.tat.gginl.api.exception.DAOException;
import org.tat.gginl.api.exception.ErrorCode;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository productRepository;
	
	
	public List<Product> findAll(){
		return productRepository.findAll();
	}
	
	public List<Object[]> findAllNativeObject(){
		return productRepository.findAllNativeObject();
	}
	
	public List<Object> findAllColumnName(){
		return productRepository.findAllColumnName();
	}
	
	
	@Transactional
	public Optional<Product>  findById(String id) throws DAOException {
		if(!StringUtils.isBlank(id)) {
		if(productRepository.findById(id).isEmpty()) {
			throw new DAOException(ErrorCode.SYSTEM_ERROR_RESOURCE_NOT_FOUND, id + " not found in Product");
		}else {
			return productRepository.findById(id);
		 }
		} else {
		 return Optional.empty();
	 }
	
		}

}
