package com.ingeneo.logistica.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfigSwagger {

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI().info(new Info()
				.title("INGENEO - Logistica")
				.description("Documentación de la API 'logistica' por parte de Departamento de Desarrollo de INGENEO")
				.version("1.0")
				.contact(new Contact()
						.name("Johann Andres Agamez Ferres")
						.email("ingjohannagamez@gmail.com")
						.url("https://ingeneo.com.co/"))
				.license(new License().name("Apache 2.0")
						.url("http://www.apache.org/licenses/LICENSE-2.0.html")))
				.addServersItem(new Server()
						.url("http://localhost:8080")
						.description("Entorno de desarrollo"))
				.addServersItem(new Server()
						.url("https://api.ingeneo.com.co")
						.description("Entorno de producción"));
	}
}
