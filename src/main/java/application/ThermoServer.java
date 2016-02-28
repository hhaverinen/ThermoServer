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

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import logic.CharLCD;

/**
 * @author Henri Haverinen
 * @version 28.2.2016
 *
 * Main class for thermoServer
 */
@SpringBootApplication
@EnableScheduling
@Component
public class ThermoServer implements ApplicationListener<ContextClosedEvent>{

	/**
	 * @param args not used
	 */
	public static void main(String[] args) {
		CharLCD.setBacklight(true);
		SpringApplication.run(ThermoServer.class, args);
	}

	/**
	 * clean stuff when exiting
	 */
	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
		CharLCD.clear();
		CharLCD.showCursor(false);
		CharLCD.setBacklight(false);
		CharLCD.blink(false);
		//TODO: clean gpio pins
	}
	
	

}
