/*
 * Copyright (c) 2016 Henri Haverinen
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 */

package logic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDateTime;
import pojo.Temperature;

/**
 * @author Henri Haverinen
 * @version 8.2.2016
 *
 *          Reads temperature from the DS18B20 temperature sensor
 */
public class TemperatureReader {
	
	/**
	 * reads the temperature and returns a new Temperature pojo
	 * @return temperature pojo
	 */
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

		return new Temperature("Current temperature", temperature, LocalDateTime.now().toString());
	}

}
