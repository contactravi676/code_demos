package com.dymmy.projects.employees.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;
import lombok.Data;

@Data
@DynamoDbBean
public class Employee {

    private String employeeId;
    private String name;
    private String email;
    private String department;
    private String phone;

    @DynamoDbPartitionKey
    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
