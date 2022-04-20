package com.upgrader.upgrader.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.upgrader.upgrade_application.service.FileUploadService;

@RestController
public class FileUploadController {
	private static final Logger logger = LogManager.getLogger(FileUploadController.class);

	@Autowired
	FileUploadService fileUploadService;

	private static final String UPLOAD_SERVICE= "uploadService";

	@PostMapping(path = "/FileUpload/Upload")
	@CircuitBreaker(name= UPLOAD_SERVICE, fallbackMethod = "uploadServiceFallback")
	public ResponseEntity<Object> uploadFile(@RequestParam("file") MultipartFile file) {
		fileUploadService.uploadFile(file);
		logger.info("Uploaded");
		return new ResponseEntity<Object>("The File Uploaded Successfully", HttpStatus.OK);
	}

	public ResponseEntity<String> uploadServiceFallback(Exception e){
		return new ResponseEntity<String>("Service is down",HttpStatus.OK);
	}

}
