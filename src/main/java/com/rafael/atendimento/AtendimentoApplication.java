package com.rafael.atendimento;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.rafael.atendimento.entity.User;
import com.rafael.atendimento.enums.TypeAccess;
import com.rafael.atendimento.repository.UserRepository;

@SpringBootApplication
public class AtendimentoApplication {

	public static void main(String[] args) {
		SpringApplication.run(AtendimentoApplication.class, args);
	}
	
	@Bean
	CommandLineRunner initDatabase(UserRepository userRepository) {
		return args -> {
			userRepository.deleteAll();

			for (int i = 0; i < 2; i++) {

				User u = new User();
				
				u.setName("Matheus");
				u.setEmail("matheus"+i+"@gmail.com");
				u.setTypeAccess(TypeAccess.PROFESSOR);
				u.setPassword("$2a$10$aq7HHnTFjjjyVEzLSDdln.u3TM3cOljBJLbavmN5gh9bD39otl/Ry");
				
				userRepository.save(u);
			}
		};
	}

}
