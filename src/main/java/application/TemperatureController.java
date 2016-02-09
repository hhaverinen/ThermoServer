package application;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import logic.TemperatureReader;
import pojo.Temperature;

/**
 * @author Henri Haverinen
 * @version 8.2.2016
 *
 * Controller class for RESTful service
 */
@RestController
public class TemperatureController {

	@RequestMapping("/temperature")
	public Temperature temperature() {
		return TemperatureReader.readTemperature();
	}
}