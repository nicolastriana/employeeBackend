package com.amaris.employee.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amaris.employee.dto.EmployeeResponseDto;
import com.amaris.employee.model.Employee;
import com.amaris.employee.service.IEmployeeService;

@RestController
@RequestMapping("/api")
public class EmployeeController {
	
	@Autowired
	private IEmployeeService employeeService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@GetMapping("/employees") 
	public ResponseEntity<List<EmployeeResponseDto>> getEmployees() throws Exception { 
		List<EmployeeResponseDto> employees = employeeService.getEmployees().stream()
												.map(e -> modelMapper.map(e, EmployeeResponseDto.class))
												.collect(Collectors.toList());
		return new ResponseEntity<>(employees, HttpStatus.OK); 
	};
	
	@GetMapping("/employee/{id}")
	public ResponseEntity<EmployeeResponseDto> getEmployee(@PathVariable Integer id) throws Exception { 
		
		EmployeeResponseDto employee;
		Employee obj = employeeService.getEmployeeById(id);
		
		if(obj == null) {
			return null;
		} else {
			employee = modelMapper.map(obj, EmployeeResponseDto.class);
		}
		
		return new ResponseEntity<>(employee, HttpStatus.OK); 
	};

}
