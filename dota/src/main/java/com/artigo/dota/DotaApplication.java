package com.artigo.dota;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.artigo.dota")
public class DotaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DotaApplication.class, args);
	}

}
