package com.dinoterra.dinosaur;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class DinosaurApplicationTests {

	@Test
	void contextLoads() {
	}

}
