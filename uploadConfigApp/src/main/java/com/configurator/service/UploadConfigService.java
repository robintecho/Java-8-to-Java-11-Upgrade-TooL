package com.configurator.service;

import java.io.IOException;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadConfigService {
	private static final Logger logger = LogManager.getLogger(UploadConfigService.class);
	@Autowired
	private GridFsTemplate template;


	public String uploadFile(MultipartFile upload) throws IOException {
		//define additional metadata
		DBObject metadata = new BasicDBObject();
		metadata.put("fileSize", upload.getSize());

		//store in database which returns the objectID
		Object fileID = template.store(upload.getInputStream(), upload.getOriginalFilename(), upload.getContentType(), metadata);

		//return as a string
		logger.info("Upload Successfully");
		return fileID.toString();
	}

	@Autowired
	GridFsOperations gridFsOperations;

    @DeleteMapping("/delete")
	public void before() {
		gridFsOperations.delete(new Query());
		logger.info("Deleted Successfully");

	}
}
