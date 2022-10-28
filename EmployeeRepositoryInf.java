package com.tougher.app.v1.repo.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tougher.app.v1.dto.criteria.EmployeeSearchCriteriaDTO;
import com.tougher.app.v1.model.Employee;

public interface EmployeeRepositoryInf {
	public Page<Employee> findByCriteria(EmployeeSearchCriteriaDTO dto, Pageable pageable);
}
