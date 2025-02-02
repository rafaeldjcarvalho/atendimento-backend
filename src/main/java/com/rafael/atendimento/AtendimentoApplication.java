package com.rafael.atendimento;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.rafael.atendimento.entity.User;
import com.rafael.atendimento.enums.TypeAccess;
import com.rafael.atendimento.repository.UserRepository;

@SpringBootApplication
@EnableScheduling
public class AtendimentoApplication {

	public static void main(String[] args) {
		SpringApplication.run(AtendimentoApplication.class, args);
	}
	
	@Bean
	@Profile("test")
	CommandLineRunner initDatabase(UserRepository userRepository) {
		return args -> {
			userRepository.deleteAll();


			User u = new User();
			u.setName("Matheus");
			u.setEmail("matheus0@gmail.com");
			u.setTypeAccess(TypeAccess.PROFESSOR);
			u.setPassword("$2a$10$aq7HHnTFjjjyVEzLSDdln.u3TM3cOljBJLbavmN5gh9bD39otl/Ry");
			
			userRepository.save(u);
			
			User u2 = new User();
			u2.setName("Rafael");
			u2.setEmail("rafadejesus125@gmail.com");
			u2.setTypeAccess(TypeAccess.PROFESSOR);
			u2.setPassword("$2a$10$aq7HHnTFjjjyVEzLSDdln.u3TM3cOljBJLbavmN5gh9bD39otl/Ry");
			
			userRepository.save(u2);
			
			User u3 = new User();
			u3.setName("Felipe");
			u3.setEmail("felipe@gmail.com");
			u3.setTypeAccess(TypeAccess.ALUNO);
			u3.setPassword("$2a$10$aq7HHnTFjjjyVEzLSDdln.u3TM3cOljBJLbavmN5gh9bD39otl/Ry");
			
			userRepository.save(u3);
		};
	}

}
