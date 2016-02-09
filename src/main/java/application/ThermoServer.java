package application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Henri Haverinen
 * @version 8.2.2016
 *
 * Main class for thermoServer
 */
@SpringBootApplication
public class ThermoServer {

	/**
	 * @param args not used
	 */
	public static void main(String[] args) {
		SpringApplication.run(ThermoServer.class, args);

	}

}
