package com.itsallbinary.tutorial.ai.spring_ai_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages =
//		"com.itsallbinary.tutorial.ai.spring_ai_app.experiments"
		"com.itsallbinary.tutorial"
)
public class SpringAiAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringAiAppApplication.class, args);
	}

}
