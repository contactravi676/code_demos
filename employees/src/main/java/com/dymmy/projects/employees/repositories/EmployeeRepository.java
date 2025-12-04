package com.dymmy.projects.employees.repositories;

import org.springframework.stereotype.Repository;

import com.dymmy.projects.employees.model.Employee;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Repository
@RequiredArgsConstructor
public class EmployeeRepository {

    private final DynamoDbEnhancedClient enhancedClient;

    private DynamoDbTable<Employee> employeeTable() {
        return enhancedClient.table("Employees", TableSchema.fromBean(Employee.class));
    }
    
    public Employee save(Employee employee) {
        employeeTable().putItem(employee);
        return employee;
    }
    
    public Employee findById(Employee emp) {
        return employeeTable().getItem(emp);
    }
}

