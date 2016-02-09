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
	private final String date;

	public Temperature(String description, double temperature, String date) {
		this.description = description;
		this.temperature = temperature;
		this.date = date;
	}
	
	public String getDescription() {
		return description;
	}

	public double getTemperature() {
		return temperature;
	}

	public String getDate() {
		return date;
	}
	
}
