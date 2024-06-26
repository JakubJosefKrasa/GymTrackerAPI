package com.kuba.GymTrackerAPI;

import com.kuba.GymTrackerAPI.role.Role;
import com.kuba.GymTrackerAPI.role.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GymTrackerApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(GymTrackerApiApplication.class, args);
	}

	@Bean
	public CommandLineRunner runner(RoleRepository roleRepository) {
		return args -> {
			if (roleRepository.findByName("USER").isEmpty()) {
				roleRepository.save(Role.builder().name("USER").build());
			}
		};
	}
}
