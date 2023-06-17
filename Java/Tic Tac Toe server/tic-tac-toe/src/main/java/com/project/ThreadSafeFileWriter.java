package com.project;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ThreadSafeFileWriter {
	private static final Object lock = new Object();
	private static String filePath = System.getProperty("user.home") + File.separator + "Desktop\\GameLogs.txt";

	public static boolean saveStringToFile(String content) {
		synchronized (lock) {
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true));
				writer.write(content);
				writer.newLine();
				writer.close();
				return true;
			} catch (IOException e) {
				System.err.println("An error occurred while saving the string to file: " + e.getMessage());
				return false;
			}
		}
	}
}