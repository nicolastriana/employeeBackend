package com.amaris.employee.service;

import java.util.List;

import com.amaris.employee.model.Employee;

public interface IEmployeeService {
	
	List<Employee> getEmployees() throws Exception;
	
	Employee getEmployeeById(Integer id) throws Exception;

}
