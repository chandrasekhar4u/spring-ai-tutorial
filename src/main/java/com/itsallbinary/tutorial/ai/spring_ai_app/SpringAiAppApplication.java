package com.itsallbinary.tutorial.ai.spring_ai_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
//		"com.itsallbinary.tutorial.ai.spring_ai_app.experiments",
		"com.itsallbinary.tutorial.ai.spring_ai_app.tutorial.config"
//		"com.itsallbinary.tutorial.ai.spring_ai_app.tutorial.springai"
//		"com.itsallbinary.tutorial.ai.spring_ai_app.tutorial.langchain4j"
		}
)
public class SpringAiAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringAiAppApplication.class, args);
	}

}
