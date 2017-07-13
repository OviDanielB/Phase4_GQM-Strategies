package it.uniroma2.isssr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class GqmPhase4Application extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(GqmPhase4Application.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(GqmPhase4Application.class, args);
	}
}
