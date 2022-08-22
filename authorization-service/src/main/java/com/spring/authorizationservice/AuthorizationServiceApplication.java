package com.spring.authorizationservice;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.spring.authorizationservice.entity.User;
import com.spring.authorizationservice.repository.UserRepository;


@SpringBootApplication
@CrossOrigin(origins="*")
public class AuthorizationServiceApplication {
	
	@Autowired
	UserRepository repository;
	
	@Autowired
	PasswordEncoder encoder;
	
	@PostConstruct
    public void initUsers() {
        List<User> users = Stream.of(
                new User(111, "tester", encoder.encode("test"), "USER"),
                new User(112, "developer", encoder.encode("dev"), "USER"),
                new User(113, "devops", encoder.encode("ops"),"USER")
        ).collect(Collectors.toList());
        repository.saveAll(users);
    }


	public static void main(String[] args) {
		SpringApplication.run(AuthorizationServiceApplication.class, args);
	}
	
	
}
