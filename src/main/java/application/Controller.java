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

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import logic.CharLCD;
import logic.TemperatureReader;
import pojo.Temperature;

/**
 * @author Henri Haverinen
 * @version 28.2.2016
 *
 * Controller class for RESTful service
 */
@RestController
public class Controller {

	@RequestMapping("/temperature")
	public Temperature temperature() {
		return TemperatureReader.readTemperature();
	}
	
	@RequestMapping("/setBacklight")
	public String setBacklight(@RequestParam(value="backlightOn") boolean backlightOn) {
		CharLCD.setBacklight(backlightOn);
		return "Backlight is " + ((backlightOn) ? "on" : "off");
	}
	
	@RequestMapping("/writeMessage")
	public void writeMessage(@RequestParam(value="message") String message){
		CharLCD.clear();
		CharLCD.message(message);
	}
	
	@RequestMapping("/clearAll")
	public void clearAll(){
		CharLCD.clear();
		CharLCD.blink(false);
		CharLCD.showCursor(false);
	}

}
