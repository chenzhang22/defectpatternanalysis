/**
 * 
 */
package cn.edu.fudan.se.code.io.utils;

import java.util.Date;

/**
 * @author Lotay
 *
 */
@SuppressWarnings("deprecation")
public class LoggerUtils {
	public static void main(String[] args) {
		LoggerUtils.println("tests message");
	}

	public static void println(Object message) {
		System.out.println("[" + new Date().toLocaleString() + "]:"
				+ message.toString());
	}

	public static void print(Object message) {
		System.out.println("[" + new Date().toLocaleString() + "]:"
				+ message.toString());
	}

	public static void println(int value) {
		println(value + "");
	}

	public static void print(int value) {
		print(value + "");
	}

	public static void println(float value) {
		println(value + "");
	}

	public static void print(float value) {
		print(value + "");
	}

	public static void println(double value) {
		println(value + "");
	}

	public static void print(double value) {
		print(value + "");
	}

	public static void println(boolean value) {
		println(value + "");
	}

	public static void print(boolean value) {
		print(value + "");
	}

	public static void println(char value) {
		println(value + "");
	}

	public static void print(char value) {
		print(value + "");
	}

	public static void println(byte value) {
		println(value + "");
	}

	public static void print(byte value) {
		print(value + "");
	}

	public static void println(long value) {
		println(value + "");
	}

	public static void print(long value) {
		print(value + "");
	}

	public static void println(short value) {
		println(value + "");
	}

	public static void print(short value) {
		print(value + "");
	}
}
