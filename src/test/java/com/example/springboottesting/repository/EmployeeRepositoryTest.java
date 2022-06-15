package com.example.springboottesting.repository;

import com.example.springboottesting.model.Employee;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @DisplayName("JUnit test for save employee operation")
    @Test
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {

        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Tuan")
                .lastName("Nguyen")
                .email("nguyentuan2686@gmail.com")
                .build();

        //when - action or the behaviour that we are going test
        Employee savedEmployee = employeeRepository.save(employee);


        //then - verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
        assertThat(savedEmployee.getFirstName()).isEqualTo("Tuan");
    }

    @Test
    public void givenEmployeeList_whenFindAll_thenEmployeeList() {
        //given - precondition or setup
        Employee employee1 = Employee.builder()
                .firstName("Tuan")
                .lastName("Nguyen")
                .email("nguyentuan2686@gmail.com")
                .build();

        Employee employee2 = Employee.builder()
                .firstName("Van")
                .lastName("A")
                .email("a22@gmail.com")
                .build();

        employeeRepository.save(employee1);
        employeeRepository.save(employee2);

        //when - action or the behaviour that we are going test
        List<Employee> employeeList = employeeRepository.findAll();

        //then - verify the output

        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
        assertThat(employeeList.get(0).getId()).isEqualTo(1L);
        assertThat(employeeList.get(1).getId()).isEqualTo(2L);
        assertThat(employeeList.get(0).getFirstName()).isEqualTo("Tuan");
        assertThat(employeeList.get(1).getFirstName()).isEqualTo("Van");
    }

    @Test
    public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject() {
        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Tuan")
                .lastName("Nguyen")
                .email("nguyentuan2686@gmail.com")
                .build();

        employeeRepository.save(employee);
        //when - action or the behaviour that we are going test
        Employee getEmployee = employeeRepository.findById(employee.getId()).get();

        //then - verify the output
        assertThat(getEmployee).isNotNull();
    }

    @Test
    public void givenEmployeeObject_whenFindByEmail_thenReturnEmployeeObject() {
        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Tuan")
                .lastName("Nguyen")
                .email("nguyentuan2686@gmail.com")
                .build();

        employeeRepository.save(employee);
        //when - action or the behaviour that we are going test
        Employee getEmployee = employeeRepository.findByEmail(employee.getEmail()).get();
        String expect = getEmployee.getEmail();

        //then - verify the output
        assertThat(getEmployee).isNotNull();
        assertThat(expect).isEqualTo("nguyentuan2686@gmail.com");
    }

    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployeeObject() {
        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Tuan")
                .lastName("Nguyen")
                .email("nguyentuan2686@gmail.com")
                .build();

        employeeRepository.save(employee);
        //when - action or the behaviour that we are going test
        Employee getEmployee = employeeRepository.findById(employee.getId()).get();
        getEmployee.setEmail("nguyentuan@gmail.com");

        //then - verify the output
        assertThat(getEmployee).isNotNull();
        assertThat(getEmployee.getEmail()).isEqualTo("nguyentuan@gmail.com");
    }

    @Test
    public void givenEmployeeObject_whenDeleteEmployee_thenRemoveEmployeeObject() {
        //given - precondition or setup
        Employee employee1 = Employee.builder()
                .firstName("Tuan")
                .lastName("Nguyen")
                .email("nguyentuan2686@gmail.com")
                .build();

        Employee employee2 = Employee.builder()
                .firstName("Tuan")
                .lastName("Nguyen")
                .email("nguyentuan2686@gmail.com")
                .build();

        employeeRepository.save(employee1);
        employeeRepository.save(employee2);


        employeeRepository.deleteById(employee1.getId());
        //when - action or the behaviour that we are going test

        List<Employee> employeeList = employeeRepository.findAll();
        //then - verify the output
        assertThat(employeeList).doesNotContain(employee1);
        assertThat(employeeList).contains(employee2);
    }
}