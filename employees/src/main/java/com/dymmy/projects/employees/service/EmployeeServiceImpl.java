package com.dymmy.projects.employees.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.dymmy.projects.employees.model.Employee;
import com.dymmy.projects.employees.repositories.EmployeeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class EmployeeServiceImpl implements EmployeeService {

	private final EmployeeRepository repository;
	//private final KafkaTemplate<String, String> kafkaTemplate;

	@Override
	public Employee create(Employee emp) {

		if (emp.getEmployeeId() == null || emp.getEmployeeId().isEmpty()) {
			emp.setEmployeeId(new StringBuilder("EMP_").append(1000 + (int)(Math.random() * 9000)).toString());
		}

		Employee savedEmployee = repository.save(emp);

		// Publish event to Kafka
		// producer.publishEmployeeEvent(savedEmployee);
		//kafkaTemplate.send("my-topic", emp.toString());

		return savedEmployee;
	}

	@Override
	public Employee update(Employee emp) {

		// Check if employee exists
		Employee existing = this.findById(emp);

		if (null == existing) {
			this.create(emp);
			emp = findById(emp);
			//kafkaTemplate.send("my-topic", emp.toString());
			return emp;
		}

		// Update fields (only the ones you want to allow)
		existing.setName(emp.getName());
		existing.setDepartment(emp.getDepartment());
		existing.setEmail(emp.getEmail());
		existing.setPhone(emp.getPhone());

		// Save updated employee
		Employee updated = repository.save(existing);

		// Publish update event
		//kafkaTemplate.send("my-topic", emp.toString());

		return updated;
	}

	@Override
	public Employee findById(Employee emp) {

		Employee fetchedEmp = repository.findById(emp);

		return fetchedEmp;
	}

}
