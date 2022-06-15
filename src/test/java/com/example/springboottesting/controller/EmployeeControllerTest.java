package com.example.springboottesting.controller;

import com.example.springboottesting.model.Employee;
import com.example.springboottesting.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebMvcTest
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper mapper;

    private Employee employee;

    @BeforeEach
    public void setUp() {
        employee = Employee.builder()
                .id(1L)
                .firstName("Tuan")
                .lastName("Nguyen")
                .email("nguyentuan2686@gmail.com")
                .build();
    }

    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        //given - precondition or setup

        BDDMockito.given(employeeService.saveEmployee(ArgumentMatchers.any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        //when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(employee)));

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
        employeeList.add(employee);
        employeeList.add(employee1);

        BDDMockito.given(employeeService.getAllEmployee())
                .willReturn(employeeList);
        //when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employee"));

        //then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(employeeList.size())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].lastName", CoreMatchers.is(employee1.getLastName())));
    }

    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenreturnEmpolyeeObject() throws Exception {
        //given - precondition or setup
        BDDMockito.given(employeeService.getEmployeeById(employee.getId()))
                .willReturn(Optional.of(employee));

        //when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employee/{id}", employee.getId()));

        //then - verify the output

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(employee.getEmail())));
    }

    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenreturnEmpty() throws Exception {
        //given - precondition or setup
        BDDMockito.given(employeeService.getEmployeeById(employee.getId()))
                .willReturn(Optional.empty());

        //when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employee/{id}", employee.getId()));

        //then - verify the output

        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturn200() throws Exception{
        long empployeeId = 1L;
        BDDMockito.willDoNothing().given(employeeService).deleteEmployee(empployeeId);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.delete("/api/employee/{id}", empployeeId));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}