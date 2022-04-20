package com.upgrader.upgraderApplication.service;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public interface UtilityService {

	/**
	 * To get the extension by string handling
	 * @param filename
	 * @return String
	 */
	default String getExtensionByStringHandling(String filename) {
		return Arrays.asList(Optional.ofNullable(filename).orElseGet(() -> "")).stream().filter(f -> f.contains("."))
				.map(f -> f.substring(filename.lastIndexOf(".") + 1)).collect(Collectors.joining());
	}

	/**
	 * To get the file name by string handling
	 * @param filename
	 * @return String
	 */
	// D:\Suntec\aa\mavenproject\src\main\java\com\simpleproject\mavenproject\AddNumber.java
	default String getFileNameByStringHandling(String filename) {
		return Arrays.asList(Optional.ofNullable(filename).orElseGet(() -> "")).stream().filter(f -> f.contains("."))
				.map(f -> f.substring(filename.lastIndexOf("\\") + 1)).collect(Collectors.joining());
	}

	/**
	 * To filter file extension
	 * @param filePath
	 * @param extension
	 * @return boolean
	 */
	default boolean filterFileExtention(String filePath, String extension) {
		return getExtensionByStringHandling(getFileNameByStringHandling(filePath))
				.equals(getExtensionByStringHandling(extension));
	}
}