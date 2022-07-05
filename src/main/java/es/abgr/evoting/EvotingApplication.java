package es.abgr.evoting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "es.abgr.evoting")
public class EvotingApplication {

	public static void main(String[] args) {
		SpringApplication.run(EvotingApplication.class, args);
	}

}
