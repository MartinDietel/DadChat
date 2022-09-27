package com.dadapp.seniorproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;


@SpringBootApplication
public class DadappApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication app = new SpringApplication(DadappApplication.class);
		app.addListeners(new ApplicationPidFileWriter("dadapp.pid"));
		app.run(args);
	}


}


