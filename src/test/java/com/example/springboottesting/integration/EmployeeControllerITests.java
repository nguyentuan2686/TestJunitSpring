package com.example.springboottesting.integration;

import com.example.springboottesting.model.Employee;
import com.example.springboottesting.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment =  SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerITests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup(){
        employeeRepository.deleteAll();
    }

    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {

        //given - precondition or setup
        Employee employee = Employee.builder()
                .id(1L)
                .firstName("Tuan")
                .lastName("Nguyen")
                .email("nguyentuan2686@gmail.com")
                .build();
        //when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        //then - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(employee.getEmail())));
    }

    @Test
    public void givenListOfEmployee_whenGetAllEmployee_thenReturnListEmployee() throws Exception {
        //given - precondition or setup
        List<Employee> employeeList = new ArrayList<>();
        Employee employee1 = Employee.builder()
                .firstName("A")
                .lastName("b")
                .email("c@gmail.com")
                .build();

        Employee employee = Employee.builder()
                .id(1L)
                .firstName("Tuan")
                .lastName("Nguyen")
                .email("nguyentuan2686@gmail.com")
                .build();
        employeeList.add(employee);
        employeeList.add(employee1);
        employeeRepository.saveAll(employeeList);

        //when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employee"));

        //then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(employeeList.size())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].lastName", CoreMatchers.is(employee1.getLastName())));
    }
}
