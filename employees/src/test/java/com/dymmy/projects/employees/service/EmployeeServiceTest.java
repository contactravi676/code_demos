package com.dymmy.projects.employees.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import com.dymmy.projects.employees.model.Employee;
import com.dymmy.projects.employees.repositories.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

	@Mock
	private EmployeeRepository employeeRepository;

	@Mock
	private KafkaTemplate<String, String> kafkaTemplate;

	@InjectMocks
	private EmployeeServiceImpl employeeService;

	@Test
	void testGetById_Success() {
		Employee emp = new Employee();
		emp.setEmployeeId("EMP_2329");
		emp.setName("Ravi");

		Mockito.when(employeeRepository.findById(emp)).thenReturn(emp);

		Employee result = employeeService.findById(emp);

		assertNotNull(result);
		assertEquals("Ravi", result.getName());
		assertEquals("EMP_2329", result.getEmployeeId());
	}

	@Test
	void testFindById_NotFound() {
		// given
		Employee request = new Employee();
		request.setEmployeeId("EMP_3000");

		Mockito.when(employeeRepository.findById(request)).thenReturn(null);

		// when
		Employee result = employeeService.findById(request);

		// then
		assertNull(result);
	}

	@Test
	void testCreate_GeneratesId_SavesAndPublishes() {
		// given
		Employee input = new Employee();
		input.setName("John Doe"); // employeeId is null intentionally

		Employee saved = new Employee();
		saved.setEmployeeId("EMP_1234");
		saved.setName("John Doe");

		// Mock repository save to return saved object
		Mockito.when(employeeRepository.save(Mockito.any(Employee.class))).thenReturn(saved);

		// when
		Employee result = employeeService.create(input);

		// then
		// 1. ID should be generated
		assertNotNull(input.getEmployeeId());
		assertTrue(input.getEmployeeId().startsWith("EMP_"));

		// 2. Repository save should be called
		Mockito.verify(employeeRepository, Mockito.times(1)).save(Mockito.any(Employee.class));

		// 3. Kafka publish should be called
		Mockito.verify(kafkaTemplate, Mockito.times(1)).send(Mockito.eq("my-topic"), Mockito.anyString());

		// 4. Returned value should be repository's output
		assertEquals("EMP_1234", result.getEmployeeId());
		assertEquals("John Doe", result.getName());
	}

	@Test
	void testUpate_IfExisting() {
		// given
		Employee input = new Employee();
		input.setName("John Doe1");
		input.setEmployeeId("EMP_1234");

		Employee saved = new Employee();
		saved.setEmployeeId("EMP_1234");
		saved.setName("John Doe1");

		// Mock repository save to return saved object
		Mockito.when(employeeRepository.save(Mockito.any(Employee.class))).thenReturn(saved);

		Mockito.when(employeeRepository.findById(input)).thenReturn(input);

		// when
		Employee result = employeeService.update(input);

		// 2. Repository save should be called
		Mockito.verify(employeeRepository, Mockito.times(1)).save(Mockito.any(Employee.class));

		// 3. Kafka publish should be called
		Mockito.verify(kafkaTemplate, Mockito.times(1)).send(Mockito.eq("my-topic"), Mockito.anyString());

		// 4. Returned value should be repository's output
		assertEquals("EMP_1234", result.getEmployeeId());
		assertEquals("John Doe1", result.getName());
	}

	@Test
	void testUpate_IfNotExisting() {
		// given
		Employee input = new Employee();
		input.setName("John Doe1");
		input.setEmployeeId("EMP_1234");

		Employee saved = new Employee();
		saved.setEmployeeId("EMP_1234");
		saved.setName("John Doe1");

		Mockito.when(employeeRepository.findById(input)).thenReturn(null).thenReturn(saved);

		Mockito.when(employeeRepository.save(Mockito.any(Employee.class))).thenReturn(saved);

		// when
		Employee result = employeeService.update(input);

		// then

		// 2. Service create should be called
		Mockito.verify(employeeRepository, Mockito.times(1)).save(Mockito.any(Employee.class));

		// 3. Kafka publish should be called
		Mockito.verify(kafkaTemplate, Mockito.times(1)).send(Mockito.eq("my-topic"), Mockito.anyString());

		// 4. Returned value should be repository's output
		assertEquals("EMP_1234", result.getEmployeeId());
		assertEquals("John Doe1", result.getName());
	}

}
