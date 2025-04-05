package com.dinoterra.dinosaur;

import org.springframework.boot.SpringApplication;

public class TestDinosaurApplication {

	public static void main(String[] args) {
		SpringApplication.from(DinosaurApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
