package wemake;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BookingApplication {
	protected static Logger log = LoggerFactory.getLogger(BookingApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(BookingApplication.class, args);
	}
}
