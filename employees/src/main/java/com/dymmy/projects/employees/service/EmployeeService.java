package com.dymmy.projects.employees.service;

import com.dymmy.projects.employees.model.Employee;

public interface EmployeeService {
	
	Employee create(Employee emp);
	
	Employee update(Employee emp);

	Employee findById(Employee emp);
}
