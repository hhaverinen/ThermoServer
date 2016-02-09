package pojo;

/**
 * @author Henri Haverinen
 * @version 8.2.2016
 * 
 * POJO for temperature
 *
 */
public class Temperature {

	private final String description;
	
	private final double temperature;

	public Temperature(String description, double temperature) {
		this.description = description;
		this.temperature = temperature;
	}
	
	public String getDescription() {
		return description;
	}

	public double getTemperature() {
		return temperature;
	}
	
}
