package com.cgi.cgi_test;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class CgiTestApplication implements CommandLineRunner {



	public static void main(String[] args) {
		SpringApplication.run(CgiTestApplication.class, args);
	}

	@Override
	//It will be used to perform initialization, it will be invoked at application startup
	public void run(String... args) throws Exception {
		// it will be used for initialization
	}

}
