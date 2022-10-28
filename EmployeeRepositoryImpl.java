package com.tougher.app.v1.repo.impl;

import static com.tougher.app.v1.repo.EmployeeRepository.hasFirstNameMatching;
import static com.tougher.app.v1.repo.EmployeeRepository.hasGender;
import static com.tougher.app.v1.repo.EmployeeRepository.hasHobby;
import static com.tougher.app.v1.repo.EmployeeRepository.hasLastNameMatching;
import static com.tougher.app.v1.repo.EmployeeRepository.hasOccupation;
import static com.tougher.app.v1.repo.EmployeeRepository.start;
import static org.springframework.data.jpa.domain.Specification.where;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.util.ObjectUtils;

import com.tougher.app.v1.dto.criteria.EmployeeSearchCriteriaDTO;
import com.tougher.app.v1.model.Employee;
import com.tougher.app.v1.model.enums.Gender;

public class EmployeeRepositoryImpl implements EmployeeRepositoryInf {
	@Autowired
	private EntityManager em;

	@Override
	public Page<Employee> findByCriteria(EmployeeSearchCriteriaDTO criteria, Pageable pageable) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
		Root<Employee> employee = cq.from(Employee.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		Specification<Employee> spec = where(start());

		if (!ObjectUtils.isEmpty(criteria.getName())) {
			spec = spec.and(hasFirstNameMatching(criteria.getName()).or(hasLastNameMatching(criteria.getName())));
		}

		if (!ObjectUtils.isEmpty(criteria.getGender())) {
			Gender gender = Gender.valueOf(criteria.getGender());
			spec = spec.and(hasGender(gender));
		}
		if (!ObjectUtils.isEmpty(criteria.getOccupationCode())) {
			spec = spec.and(hasOccupation(criteria.getOccupationCode()));
		}
		if (!ObjectUtils.isEmpty(criteria.getHobbyList())) {
			spec = spec.and(hasHobby(criteria.getHobbyList()));
		}
		cq.where(spec.toPredicate(employee, cq, cb));

		if (pageable.getSort() != null) {
			cq.orderBy(QueryUtils.toOrders(pageable.getSort(), employee, cb));
		}

		CriteriaQuery<Long> cq2 = cb.createQuery(Long.class);
		cq2.select(cb.count(cq2.from(Employee.class)));
		cq2.where(predicates.toArray(new Predicate[] {}));
		int total = em.createQuery(cq2).getSingleResult().intValue();

		TypedQuery<Employee> tq = em.createQuery(cq);
		tq.setMaxResults(pageable.getPageSize());
		tq.setFirstResult((int) pageable.getOffset());

		// This needs to select count
		return new PageImpl<Employee>(tq.getResultList(), pageable, total);
	}

}
