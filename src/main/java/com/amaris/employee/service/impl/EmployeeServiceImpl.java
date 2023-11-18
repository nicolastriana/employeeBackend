package com.amaris.employee.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.amaris.employee.model.Employee;
import com.amaris.employee.repository.IEmployeeRepository;
import com.amaris.employee.service.IEmployeeService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class EmployeeServiceImpl implements IEmployeeService {

	@Autowired
	private IEmployeeRepository employeeRepo;
	
	private final RestTemplate restTemplate;
	
	private String employeesApiUrl = "https://dummy.restapiexample.com/api/v1/employees";
	
	@Autowired
    public EmployeeServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    };

	@Override
	public List<Employee> getEmployees() throws Exception {

		try {
			
			List<Employee> employees = getEmployeesFromApi();
			
            employees.forEach(e -> e.setEmployee_anual_salary(e.getEmployee_salary() * 12));
            
            saveData(employees);
            
            return employees;
            
		} catch (HttpClientErrorException.TooManyRequests e) {

			List<Employee> employees = employeeRepo.findAll();
			
			return employees;
		}
		
	}

	@Override
	public Employee getEmployeeById(Integer id) throws Exception {
        
        try {
            
            List<Employee> employees = getEmployeesFromApi();
    		
            employees.forEach(e -> e.setEmployee_anual_salary(e.getEmployee_salary() * 12));
            
            saveData(employees);
            
            Employee employee = employeeRepo.findById(id).orElse(null);
            
            return employee;
			
		} catch (HttpClientErrorException.TooManyRequests e) {

			Employee employee = employeeRepo.findById(id).orElse(null);
            
            return employee;
			
		}
		
	}

	// This method save the employees info in a temporal bd to avoid the problem with the time between each API call
	public void saveData(List<Employee> employees) {
		
		employeeRepo.deleteAll();
		
		employees.forEach(e -> {
			Employee employee = new Employee();
			
			employee.setId(e.getId());
			employee.setEmployee_name(e.getEmployee_name());
			employee.setEmployee_salary(e.getEmployee_salary());
			employee.setEmployee_anual_salary(e.getEmployee_anual_salary());
			employee.setEmployee_age(e.getEmployee_age());
			employee.setProfile_image(e.getProfile_image());
			
			employeeRepo.save(employee);
		});
		
	}
	
	
	// This method make the api call and return a list with the employees info
	public List<Employee> getEmployeesFromApi() throws Exception {
		
		String jsonResponse = restTemplate.getForObject(employeesApiUrl, String.class);
		ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);
        JsonNode dataNode = jsonNode.get("data");
        
        List<Employee> employees = objectMapper.convertValue(dataNode, new TypeReference<List<Employee>>() {});
		
        return employees;
        
	}

}
