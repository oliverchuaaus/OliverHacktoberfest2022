package com.tougher.app.v1.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tougher.app.v1.dto.EmployeeDTO;
import com.tougher.app.v1.dto.criteria.EmployeeSearchCriteriaDTO;
import com.tougher.app.v1.dto.criteria.EmployeeSearchResultDTO;
import com.tougher.app.v1.service.EmployeeService;

@RestController
@RequestMapping("employees")
public class MyApplicationController {

	@Autowired
	private EmployeeService employeeService;

	@PostMapping
	public EmployeeDTO save(@Valid @RequestBody EmployeeDTO employeeDTO) {
		return employeeService.save(employeeDTO);
	}

	@PostMapping("/search")
	public EmployeeSearchResultDTO findByCriteria(@RequestBody EmployeeSearchCriteriaDTO criteria) {
		return employeeService.findByCriteria(criteria);
	}

	@GetMapping("/{id}")
	public EmployeeDTO findById(@PathVariable Long id) {
		return employeeService.findById(id);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return errors;
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ConstraintViolationException.class)
	public Map<String, String> handleValidationExceptions(ConstraintViolationException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getConstraintViolations().forEach((error) -> {
			String fieldName = error.getPropertyPath().toString();
			String errorMessage = error.getMessage();
			errors.put(fieldName, errorMessage);
		});
		return errors;
	}
}
