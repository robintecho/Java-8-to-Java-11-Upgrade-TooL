package com.upgrader.upgraderApplication.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class SearchFilesService implements UtilityService {
	
	/**
	 * To find all files
	 * 
	 * @param path
	 * @param fName
	 * @param str
	 * @return List
	 */
	List<String> find(String path, String fName, List<String> str) {

		if (fName.indexOf("*") > -1) {
			str = findAllExtensionMatchingFiles(path, fName, str);
		} else {
			str = findFiles(path, fName, str);
		}
		return str;
	}

	/**
	 * To find all pom.xml files within the project
	 * 
	 * @param path
	 * @param fName
	 * @param str
	 * @return List
	 */
	List<String> findFiles(String path, String fName, List<String> str) {
		File f = new File(path);

		if (fName.equalsIgnoreCase(f.getName())) {
			str.add(f.getAbsolutePath());
		}
		if (f.isDirectory()) {
			for (String aChild : f.list()) {
				findFiles(path + File.separator + aChild, fName, str);
			}
		}
		return str;
	}

	/**
	 * To find all files in directory
	 * 
	 * @param allFileList
	 * @param path
	 * @return List
	 */
	List<String> findAllFilesInDir(List<String> allFileList, String path) {
		File f = new File(path);
		if (f.isDirectory()) {
			for (String aChild : f.list()) {
				findAllFilesInDir(allFileList, path + File.separator + aChild);
			}
		} else {
			allFileList.add(f.getAbsolutePath());
		}
		return allFileList;
	}

	/**
	 * To filter all extension matching files in directory
	 * 
	 * @param path
	 * @param extension
	 * @param str
	 * @return List
	 */
	List<String> findAllExtensionMatchingFiles(String path, String extension, List<String> str) {
		List<String> allFilesInDir = findAllFilesInDir(new ArrayList<String>(), path);
		List<String> allFilesFilteredByExtention = allFilesInDir.stream()
				.filter(eachFile -> filterFileExtention(eachFile, extension)).collect(Collectors.toList());
		return allFilesFilteredByExtention;
	}
}