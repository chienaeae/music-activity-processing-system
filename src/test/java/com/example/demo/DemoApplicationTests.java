package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.demo.config.TestDatabaseConfig;

@SpringBootTest
@ActiveProfiles("test")
class DemoApplicationTests extends TestDatabaseConfig {

	@Test
	void contextLoads() {
	}

}
