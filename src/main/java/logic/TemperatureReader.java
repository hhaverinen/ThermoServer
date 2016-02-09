package logic;

import java.io.BufferedReader;
import java.io.FileReader;

import pojo.Temperature;

/**
 * @author Henri Haverinen
 * @version 8.2.2016
 *
 *          Reads temperature from the sensor
 */
public class TemperatureReader {

	public static Temperature readTemperature() {
		String fileName = "/sys/bus/w1/devices/28-00000581de01/w1_slave";
		double temperature = -999;
		try (BufferedReader br = new BufferedReader(new FileReader(fileName));) {

			String temp = "-999";
			if (br.readLine().contains("YES")) {
				temp = br.readLine().split("t=")[1];
			}

			// ugly way to get precision point to right place, only works if
			// temperature starts with two number (in other words atleast 10.0)
			temp = temp.substring(0, 2) + "." + temp.substring(2, temp.length());
			
			temperature = Double.parseDouble(temp);

		} catch (Exception e) {
			// continue
		}

		return new Temperature("Current temperature", temperature);
	}

}
