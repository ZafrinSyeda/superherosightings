package com.company.superherosighting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Assignment for Wiley Edge training: Creating a Full-Stack Application that allows for a company to
 * create a Superhero/ supervillain sightings web application, connected to a database of these sightings,
 * where people can add or view any superhero sightings. This class is needed to run this application
 */
@SpringBootApplication
public class SuperherosightingApplication {

	public static void main(String[] args) {
		SpringApplication.run(SuperherosightingApplication.class, args);
	}

}
