package com.dinoterra.expedition;

import org.springframework.boot.SpringApplication;

public class TestExpeditionApplication {

	public static void main(String[] args) {
		SpringApplication.from(ExpeditionApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
