package com.upgrader.upgrade_application.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileUploadService {
	private static final Logger logger = LogManager.getLogger(FileUploadService.class);
	@Autowired
	private GridFsTemplate template;


	public String uploadFile(MultipartFile upload) throws IOException {
		//define additional metadata
		DBObject metadata = new BasicDBObject();
		metadata.put("fileSize", upload.getSize());

		//store in database which returns the objectID
		Object fileID = template.store(upload.getInputStream(), upload.getOriginalFilename(), upload.getContentType(), metadata);

		//return as a string
		return fileID.toString();
	}
}
