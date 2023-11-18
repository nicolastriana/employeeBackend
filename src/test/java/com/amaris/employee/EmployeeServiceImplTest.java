package com.amaris.employee;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import com.amaris.employee.model.Employee;
import com.amaris.employee.service.impl.EmployeeServiceImpl;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class EmployeeServiceImplTest {
	
	@Autowired
	private EmployeeServiceImpl employeeServiceImpl;	
	
	@MockBean
    private RestTemplate restTemplate;
	
	private MockRestServiceServer mockServer;
	
	@Test
    public void getEmployeeByIdTest() throws Exception {
		
		mockServer = MockRestServiceServer.createServer(restTemplate);

        String mockedJsonResponse = "{ \"data\": [{ \"id\": 1, \"employee_name\": \"Tiger Nixon\", \"employee_salary\": 320800, \"employee_age\": 61, \"profile_image\": \"\" }] }";
        mockServer.expect(requestTo("https://dummy.restapiexample.com/api/v1/employee/1"))
                  .andRespond(withSuccess(mockedJsonResponse, MediaType.APPLICATION_JSON));

        Employee result = employeeServiceImpl.getEmployeeById(1);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);

    }

}
