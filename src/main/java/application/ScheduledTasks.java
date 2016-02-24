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

package application;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import logic.CharLCD;
import logic.TemperatureReader;
import pojo.Temperature;

/**
 * @author Henri Haverinen
 * @version 24.2.2016
 *
 * Class for managing scheduled tasks
 */
@Component
public class ScheduledTasks {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM HH:mm:ss");
	
	/**
	 * updates data shown on the display
	 */
	@Scheduled(fixedRate = 60000)
	public void updateTemperature(){
		CharLCD lcd = new CharLCD();
		lcd.clear();
		Temperature temp = TemperatureReader.readTemperature();
		String text = dateFormat.format(new Date()) + "\n" + "Temp: " + Double.toString(temp.getTemperature());
		lcd.message(text);
	}
}
