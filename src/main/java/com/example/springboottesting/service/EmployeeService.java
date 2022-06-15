package com.example.springboottesting.service;

import com.example.springboottesting.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    Employee saveEmployee(Employee employee);
    List<Employee> getAllEmployee();

    Optional<Employee> getEmployeeById(Long id);

    void deleteEmployee(Long id);
}
