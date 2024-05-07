package com.example.backend;

import com.example.backend.entity.User;
import com.example.backend.enums.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.example.backend.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Date;

import static com.fasterxml.jackson.databind.type.LogicalType.DateTime;

@SpringBootApplication
public class HrManagementApplication implements CommandLineRunner{
	@Autowired
	private UserRepository repository;
	public static void main(String[] args) {
		SpringApplication.run(HrManagementApplication.class, args);
	}
	@Override
	public void run(String... args) throws Exception {
		ExampleMatcher modelMatcher = ExampleMatcher.matching()
				.withIgnorePaths("id")
				.withMatcher("username", ExampleMatcher.GenericPropertyMatchers.ignoreCase())
				.withMatcher("role", ExampleMatcher.GenericPropertyMatchers.ignoreCase())
				.withMatcher("firstName", ExampleMatcher.GenericPropertyMatchers.ignoreCase())
				.withMatcher("lastName", ExampleMatcher.GenericPropertyMatchers.ignoreCase())
				.withMatcher("DaysOff", ExampleMatcher.GenericPropertyMatchers.ignoreCase());

		User manager = User.builder()
				.role(UserRole.MANAGER)
				.username("manager")
				.firstName("Eliada")
				.lastName("Manager")
				.daysOff(0)
				.build();

		Example<User> example = Example.of(manager, modelMatcher);
		if (!repository.exists(example)) {
			manager.setCreatedBy("System");
			manager.setCreatedAt(LocalDateTime.now());
			manager.setPassword(new BCryptPasswordEncoder().encode("manager"));
			repository.save(manager);
		}
	}
}
