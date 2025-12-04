package com.dymmy.projects.employees;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Component;

import com.dymmy.projects.employees.model.Employee;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;

@SpringBootApplication

public class EmployeesApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeesApplication.class, args);
	}
	

}

@Component
@RequiredArgsConstructor
class DynamoDbInitializer {

    private final DynamoDbEnhancedClient enhancedClient;

    @PostConstruct
    public void createTableIfNotExists() {
        DynamoDbTable<Employee> table = enhancedClient.table("Employees", TableSchema.fromBean(Employee.class));
        try {
            table.describeTable(); // check if exists
            System.out.println("Employees table exists!");
        } catch (ResourceNotFoundException e) {
            table.createTable();
            System.out.println("Employees table created!");
        }
    }
}

/*@Configuration
class KafkaProducerConfig {

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}*/

