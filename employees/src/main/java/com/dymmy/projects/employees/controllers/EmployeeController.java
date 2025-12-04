package com.dymmy.projects.employees.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dymmy.projects.employees.model.Employee;
import com.dymmy.projects.employees.service.EmployeeService;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

	private final EmployeeService service;

	public EmployeeController(EmployeeService service) {
		this.service = service;
	}

	@PostMapping("/create")
	public ResponseEntity<Employee> create(@RequestBody Employee emp) {
		System.out.println("******************** Employee create 1");
		return ResponseEntity.ok(service.create(emp));
	}

	@PutMapping("/update")
	public ResponseEntity<Employee> update(@RequestBody Employee emp) {
		return ResponseEntity.ok(service.update(emp));

	}
}
