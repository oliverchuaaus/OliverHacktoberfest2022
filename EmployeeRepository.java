package com.tougher.app.v1.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tougher.app.v1.model.Employee;
import com.tougher.app.v1.model.enums.Gender;
import com.tougher.app.v1.repo.impl.EmployeeRepositoryInf;

public interface EmployeeRepository
		extends JpaRepository<Employee, Long>, EmployeeRepositoryInf, JpaSpecificationExecutor<Employee> {

	@Query("SELECT e FROM Employee e WHERE e.gender = com.tougher.app.v1.model.enums.Gender.MALE")
	List<Employee> findAllMaleEmployees();

	@Query("SELECT e FROM Employee e WHERE e.gender IN :genders")
	List<Employee> findByGenders(List<Gender> genders);

	@Query("SELECT e FROM Employee e WHERE e.birthday BETWEEN :fromDate AND :toDate")
	List<Employee> findByBirthday(LocalDate fromDate, LocalDate toDate);

	// Using NamedQuery. NamedQuery should be named Entity.methodName
	List<Employee> findFirstName(@Param("name") String firstName);

	// Using specification
	static Specification<Employee> start() {
		return (emp, cq, cb) -> cb.conjunction();
	}

	static Specification<Employee> hasFirstNameMatching(String name) {
		return (emp, cq, cb) -> cb.like(cb.lower(emp.get("firstName")), "%" + name.toLowerCase() + "%");
	}

	static Specification<Employee> hasLastNameMatching(String name) {
		return (emp, cq, cb) -> cb.like(cb.lower(emp.get("lastName")), "%" + name.toLowerCase() + "%");
	}

	static Specification<Employee> hasGenders(List<Gender> genders) {
		return (emp, cq, cb) -> emp.get("gender").in(genders);
	}

	static Specification<Employee> hasGender(Gender gender) {
		return (emp, cq, cb) -> cb.equal(emp.get("gender"), gender);
	}

	static Specification<Employee> hasOccupation(Long occupationCode) {
		return (emp, cq, cb) -> cb.equal(emp.get("occupation").get("id"), occupationCode);
	}

	static Specification<Employee> hasHobby(List<Long> hobbyList) {
		return (emp, cq, cb) -> emp.join("hobbies").get("id").in(hobbyList);
	}
}
