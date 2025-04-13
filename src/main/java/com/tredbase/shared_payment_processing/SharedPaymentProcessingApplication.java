package com.tredbase.shared_payment_processing;

import com.tredbase.shared_payment_processing.services.DataInitializationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SharedPaymentProcessingApplication  implements CommandLineRunner {
	private final DataInitializationService dataInitializationService;

    public SharedPaymentProcessingApplication(DataInitializationService dataInitializationService) {
        this.dataInitializationService = dataInitializationService;
    }

    public static void main(String[] args) {
		SpringApplication.run(SharedPaymentProcessingApplication.class, args);
	}

	@Override
	public void run(String... args) {
		dataInitializationService.initializeDatabase();
	}
}
