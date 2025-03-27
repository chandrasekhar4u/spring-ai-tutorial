package com.itsallbinary.tutorial.ai.spring_ai_app.tutorial.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConditionalOnProperty(name = "tutorial.type", havingValue = "springai")
@ComponentScan(basePackages = "com.itsallbinary.tutorial.ai.spring_ai_app.tutorial.springai")
@PropertySource("classpath:application-springai.properties")
public class SpringAiConfig {

}
