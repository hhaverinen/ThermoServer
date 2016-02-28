/*
 * Copyright (c) 2016 Henri Haverinen
 * Pretty literal port of Adafruit Industries's CharLCD.py to Java, using Pi4J library
 * Some custom changes made also.
 *
 * Copyright to original CharLCD.py code is owned by Adafruit Industries / Tony DiCola
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

import com.pi4j.io.gpio.*;

/**
 * @author Henri Haverinen
 * @version 24.2.2016
 * Class to represent and interact with an HD44780 character LCD display.
 */
public final class CharLCD {
	// Commands
	static final int LCD_CLEARDISPLAY        = 0x01;
	static final int LCD_RETURNHOME          = 0x02;
	static final int LCD_ENTRYMODESET        = 0x04;
	static final int LCD_DISPLAYCONTROL      = 0x08;
	static final int LCD_CURSORSHIFT         = 0x10;
	static final int LCD_FUNCTIONSET         = 0x20;
	static final int LCD_SETDDRAMADDR        = 0x80;
	
	// Entry flags
	static final int LCD_ENTRYLEFT           = 0x02;
	static final int LCD_ENTRYSHIFTINCREMENT = 0x01;
	static final int LCD_ENTRYSHIFTDECREMENT = 0x00;
	
	// Control flags
	static final int LCD_DISPLAYON           = 0x04;
	static final int LCD_CURSORON            = 0x02;
	static final int LCD_CURSOROFF           = 0x00;
	static final int LCD_BLINKON             = 0x01;
	static final int LCD_BLINKOFF            = 0x00;
	
	// Move flags
	static final int LCD_DISPLAYMOVE         = 0x08;
	static final int LCD_MOVERIGHT           = 0x04;
	static final int LCD_MOVELEFT            = 0x00;
	
	// Function set flags
	static final int LCD_4BITMODE            = 0x00;
	static final int LCD_2LINE               = 0x08;
	static final int LCD_1LINE               = 0x00;
	static final int LCD_5x8DOTS             = 0x00;
	
	// Offset for up to 4 rows.
	static final int[] LCD_ROW_OFFSETS         = {0x00, 0x40, 0x14, 0x54};
	
	// Char LCD plate GPIO numbers.
	// not used, just to remind basic setup
	static final int LCD_PLATE_RS            = 2;
	static final int LCD_PLATE_EN            = 3;
	static final int LCD_PLATE_D4            = 6;
	static final int LCD_PLATE_D5            = 5;
	static final int LCD_PLATE_D6            = 4;
	static final int LCD_PLATE_D7            = 1;
	static final int LCD_PLATE_RED           = 11;

	// Gpio controller and pins
	private static final GpioController gpio = GpioFactory.getInstance();;
	private static final GpioPinDigitalOutput rsPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02,PinState.LOW);
	private static final GpioPinDigitalOutput enPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03,PinState.LOW);
	private static final GpioPinDigitalOutput d4Pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06,PinState.LOW);
	private static final GpioPinDigitalOutput d5Pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05,PinState.LOW);
	private static final GpioPinDigitalOutput d6Pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04,PinState.LOW);
	private static final GpioPinDigitalOutput d7Pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01,PinState.LOW);
	private static final GpioPinDigitalOutput backlightPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00,PinState.LOW);
	
	private static int displayControl = LCD_DISPLAYON | LCD_CURSOROFF | LCD_BLINKOFF;
	private static int displayFunction = LCD_4BITMODE | LCD_1LINE | LCD_2LINE | LCD_5x8DOTS;
	private static int displayMode = LCD_ENTRYLEFT | LCD_ENTRYSHIFTDECREMENT;
	
	// size of lcd-display (16x2)
	private static final int columns = 16;
	private static final int lines = 2;
	
	/**
	 * initialize display
	 */
	private CharLCD(){
		write8(0x33,false);
		write8(0x32,false);
		write8(LCD_DISPLAYCONTROL | displayControl, false);
		write8(LCD_FUNCTIONSET | displayFunction, false);
		write8(LCD_ENTRYMODESET | displayMode, false);
		clear();
	}
	
	/**
	 * sets pin state
	 * @param pin which pin's state will be changed
	 * @param state true to set state high, false to set state low
	 */
	private static void setPinState(GpioPinDigitalOutput pin, boolean state) {
		if (state) {
			pin.high();
		} else {
			pin.low();
		}
	}
	
	/**
	 * set cursor position to zero
	 */
	public static void home() {
		try {
			write8(LCD_RETURNHOME, false);
			Thread.sleep(3);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * clears the display
	 */
	public static void clear(){
		try {
			write8(LCD_CLEARDISPLAY, false);
			Thread.sleep(3);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * set cursor to given place
	 * @param col number of column
	 * @param row number of row
	 */
	public static void setCursor(int col, int row) {
		if (row > lines) row = lines - 1;
		write8(LCD_SETDDRAMADDR | (col + LCD_ROW_OFFSETS[row]), false);
	}

	/**
	 * enables or disables the display
	 * @param enable true enables, false disables
	 */
	public static void enableDisplay(boolean enable) {
		if (enable) CharLCD.displayControl |= LCD_DISPLAYON;
		else CharLCD.displayControl &= ~LCD_DISPLAYON;
		write8(LCD_DISPLAYCONTROL | CharLCD.displayControl, false);
	}

	/**
	 * shows or hides the cursor
	 * @param show true shows, false hides
	 */
	public static void showCursor(boolean show) {
		if (show) CharLCD.displayControl |= LCD_CURSORON;
		else CharLCD.displayControl &= ~LCD_CURSORON;
		write8(LCD_DISPLAYCONTROL | CharLCD.displayControl, false);
	}
	
	/**
	 * blinks cursor or stops the blinking
	 * @param blink true blinks, false stops
	 */
	public static void blink(boolean blink) {
		if (blink) CharLCD.displayControl |= LCD_BLINKON;
		else CharLCD.displayControl &= ~LCD_BLINKON;
		write8(LCD_DISPLAYCONTROL | CharLCD.displayControl, false);
	}
	
	/**
	 * moves cursor to left one position
	 */
	public static void moveLeft() {
		write8(LCD_CURSORSHIFT | LCD_DISPLAYMOVE | LCD_MOVELEFT, false);
	}
	
	/**
	 * moves cursor to right one position
	 */
	public static void moveRight() {
		write8(LCD_CURSORSHIFT | LCD_DISPLAYMOVE | LCD_MOVERIGHT, false);
	}

	/**
	 * sets text direction from left to right (default)
	 */
	public static void setLeftToRight() {
		CharLCD.displayMode |= LCD_ENTRYLEFT;
		write8(LCD_ENTRYMODESET | CharLCD.displayMode, false);
	}

	/**
	 * sets text direction from right to left
	 */
	public static void setRightToLeft() {
		CharLCD.displayMode &= ~LCD_ENTRYLEFT;
		write8(LCD_ENTRYMODESET | CharLCD.displayMode, false);
	}
	
	/**
	 * 'Right justify' or 'left justify' text
	 * @param autoScroll true to right justify, false to left justify
	 */
	public static void autoScroll(boolean autoScroll) {
		if (autoScroll) CharLCD.displayMode |= LCD_ENTRYSHIFTINCREMENT;
		else CharLCD.displayMode &= ~LCD_ENTRYSHIFTINCREMENT;
		write8(LCD_ENTRYMODESET | CharLCD.displayMode, false);
	}

	/**
	 * writes a message to display, can contain newlines
	 * @param text message to display
	 */
	public static void message(String text){
		int line = 0;
		for (char c : text.toCharArray()){
			if (c == '\n') {
				line++;
				int col = (CharLCD.displayMode & LCD_ENTRYLEFT) > 0 ? 0 : CharLCD.columns - 1;
				setCursor(col,line);
			} else {
				write8(c, true);
			}
		}
	}

	/**
	 * sets backlight on or off
	 * @param backlight true to set backlight on, false off
	 */
	public static void setBacklight(boolean backlight) {
		setPinState(backlightPin, !backlight);
	}
	
	/**
	 * writes 8-bit value in character or data mode.
	 * @param value value from 0-255
	 * @param charMode true if character data, false otherwise
	 */
	public static void write8(int value, boolean charMode) {
		try {
		Thread.sleep(1);
		
		setPinState(rsPin, charMode);
		
		// write upper 4 bits
		setPinState(d4Pin, ((value >> 4) & 1) > 0);
		setPinState(d5Pin, ((value >> 5) & 1) > 0);
		setPinState(d6Pin, ((value >> 6) & 1) > 0);
		setPinState(d7Pin, ((value >> 7) & 1) > 0);
		pulseEnable();
		
		// write lower 4 bits
		setPinState(d4Pin, (value & 1) > 0);
		setPinState(d5Pin, ((value >> 1) & 1) > 0);
		setPinState(d6Pin, ((value >> 2) & 1) > 0);
		setPinState(d7Pin, ((value >> 3) & 1) > 0);
		pulseEnable();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * pulses the clocl enable line off, on, off to send command
	 */
	private static void pulseEnable() {
		try {
			setPinState(enPin, false);
			Thread.sleep(1);
			setPinState(enPin, true);
			Thread.sleep(1);
			setPinState(enPin, false);
			Thread.sleep(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}        