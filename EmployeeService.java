package com.tougher.app.v1.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.NullHandling;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.tougher.app.v1.dto.EmployeeDTO;
import com.tougher.app.v1.dto.criteria.EmployeeSearchCriteriaDTO;
import com.tougher.app.v1.dto.criteria.EmployeeSearchResultDTO;
import com.tougher.app.v1.dto.criteria.PageableDTO;
import com.tougher.app.v1.dto.criteria.PageableDTO.SortDTO.OrderDTO;
import com.tougher.app.v1.model.Employee;
import com.tougher.app.v1.repo.EmployeeRepository;
import com.tougher.app.v1.repo.HobbyRepository;
import com.tougher.app.v1.repo.OccupationRepository;

@Service
public class EmployeeService {
	@Autowired
	private EmployeeRepository employeeRepo;
	@Autowired
	private OccupationRepository occupationRepo;
	@Autowired
	private HobbyRepository hobbyRepo;
	@Autowired
	private ModelMapper modelMapper;

	public EmployeeDTO save(EmployeeDTO empDTO) {
		Employee emp = convertToEntity(empDTO);
		emp = employeeRepo.save(emp);
		return convertToDto(emp);
	}

	public EmployeeDTO findById(Long id) {
		Employee employee = employeeRepo.findById(id).get();
		return convertToDto(employee);
	}

	public EmployeeSearchResultDTO findByCriteria(EmployeeSearchCriteriaDTO criteria) {
		Sort sort = Sort.unsorted();
		if (criteria.getPageable() != null && criteria.getPageable().getSort() != null) {
			sort = convertToEntity(criteria.getPageable().getSort());
		}
		PageRequest pageable = PageRequest.of(0, 20);
		if (criteria.getPageable() != null && criteria.getPageable().getOffset() != null
				&& criteria.getPageable().getPageSize() != null) {
			pageable = PageRequest.of(criteria.getPageable().getOffset(), criteria.getPageable().getPageSize(), sort);
		}
		Page<Employee> page = employeeRepo.findByCriteria(criteria, pageable);

		EmployeeSearchResultDTO result = new EmployeeSearchResultDTO();
		result.setEmployeeList(page.getContent().stream().map(this::convertToDto).collect(Collectors.toList()));
		return result;
	}

	private EmployeeDTO convertToDto(Employee employee) {
		return modelMapper.map(employee, EmployeeDTO.class);
	}

	private Sort convertToEntity(PageableDTO.SortDTO dto) {
		List<OrderDTO> dtoList = dto.getOrderList();
		List<Order> orderList = new ArrayList<Order>();
		for (OrderDTO orderDTO : dtoList) {
			Order order = new Order(Direction.valueOf(orderDTO.getDirection().name()), orderDTO.getProperty(),
					NullHandling.valueOf(orderDTO.getNullHandling().name()));
			orderList.add(order);
		}
		return Sort.by(orderList);
	}

	private Employee convertToEntity(EmployeeDTO employeeDTO) {
		Employee emp = modelMapper.map(employeeDTO, Employee.class);
		if (emp.getOccupation() != null && emp.getOccupation().getId() != null) {
			emp.setOccupation(occupationRepo.getOne(emp.getOccupation().getId()));
		}
		if (emp.getHobbies() != null) {
			for (int i = 0; i < emp.getHobbies().size(); i++) {
				emp.getHobbies().set(i, hobbyRepo.getOne(emp.getHobbies().get(i).getId()));
			}
		}
		if (emp.getPersonalDetail() != null && emp.getPersonalDetail().getId() == null) {
			emp.getPersonalDetail().setEmployee(emp);
		}
		if (emp.getProfessionalDetail() != null && emp.getProfessionalDetail().getId() == null) {
			emp.getProfessionalDetail().setEmployee(emp);
		}
		if (emp.getAddresses() != null) {
			emp.getAddresses().stream().forEach(address -> address.setEmployee(emp));
		}
		return emp;
	}

}
