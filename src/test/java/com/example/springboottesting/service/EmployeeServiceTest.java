package com.example.springboottesting.service;

import com.example.springboottesting.exception.ResourceNotFoundException;
import com.example.springboottesting.model.Employee;
import com.example.springboottesting.repository.EmployeeRepository;
import com.example.springboottesting.service.Impl.EmployeeServiceImp;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImp employeeService;

    private Employee employee;
    private Employee employee1;



    @BeforeEach
    public void setup() {
        employee = Employee.builder()
                .id(1L)
                .firstName("tuan")
                .lastName("nguyen")
                .email("nguyentuan2686@gmail.com").build();

        employee1 = Employee.builder()
                .id(1L)
                .firstName("tuan1")
                .lastName("nguyen1")
                .email("nguyentuan@gmail.com").build();

        //Same way use @Mock or @InjectMock
//        employeeRepository = Mockito.mock(EmployeeRepository.class);
//        employeeService = new EmployeeServiceImp(employeeRepository);
    }

    @Test
    public void givenEmployeeObject_whenSaveEmoplyee_thenReturnEmployeeObject() {
        //given - precondition or setup


        BDDMockito.given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.empty());
        BDDMockito.given(employeeRepository.save(employee))
                .willReturn(employee);

        //when - action or the behaviour that we are going test
        Employee actualEmployee = employeeService.saveEmployee(employee);


        //then - verify the output

        Assertions.assertThat(actualEmployee).isEqualTo(employee);
    }

    @Test
    public void givenEmployeeObject_whenSaveEmoplyee_thenReturnException() {
        //given - precondition or setup
        BDDMockito.given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.of(employee));

        //when - action or the behaviour that we are going test
        Throwable throwable = assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.saveEmployee(employee);
        });

        String expect = "Employee already exist with given email: " + employee.getEmail();

        //then - verify the output

        Assertions.assertThat(throwable.getMessage()).isEqualTo(expect);
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    public void givenEmployeeList_whenGetAllEmployee_thenReturnEmployeeList() {
        //given - precondition or setup


        BDDMockito.given(employeeRepository.findAll())
                .willReturn(List.of(employee,employee1));

        //when - action or the behaviour that we are going test
        List<Employee> employeeList = employeeRepository.findAll();

        //then - verify the output
        Assertions.assertThat(employeeList).containsAll(List.of(employee,employee1));
        Assertions.assertThat(employeeList.size()).isEqualTo(2);
    }

    @Test
    public void givenId_whenGetEmployee_thenReturnEmployee() {
        //given - precondition or setup

        BDDMockito.given(employeeRepository.findById(employee.getId()))
                .willReturn(Optional.ofNullable(employee));

        //when - action or the behaviour that we are going test
        Employee actualEmployee =  employeeRepository.findById(employee.getId()).get();

        //then - verify the output
        Assertions.assertThat(actualEmployee).isEqualTo(employee);
    }

    @Test
    public void givenId_whenDeleteEmployee_thenReturnUpdatedEmployee() {
        //given - precondition or setup

        BDDMockito.willDoNothing().given(employeeRepository).deleteById(1L);

        //when - action or the behaviour that we are going test
        employeeRepository.deleteById(employee.getId());

        //then - verify the output
        Mockito.verify(employeeRepository,times(1)).deleteById(1L);
    }
}