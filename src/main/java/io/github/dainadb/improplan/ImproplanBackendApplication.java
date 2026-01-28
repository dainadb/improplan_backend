package io.github.dainadb.improplan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // Habilitamos la ejecución de tareas programadas
public class ImproplanBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImproplanBackendApplication.class, args);
	}

}
