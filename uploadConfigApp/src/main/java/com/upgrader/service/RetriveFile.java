package com.upgrader.service;

import com.mongodb.client.gridfs.model.GridFSFile;
import com.upgrader.controller.FileUploadController;
import com.upgrader.model.LoadFile;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
@Service
public class RetriveFile {

    @Autowired
    private GridFsOperations operations;

    @Autowired
    private GridFsTemplate template;

    private static final Logger logger = LogManager.getLogger(RetriveFile.class);

    public LoadFile downloadFile() throws IOException {

        //search file
        GridFSFile gridFSFile = template.findOne( new Query());


        //convert uri to byteArray
        //save data to LoadFile class
        LoadFile loadFile = new LoadFile();

        if (gridFSFile != null && gridFSFile.getMetadata() != null) {
            loadFile.setFilename( gridFSFile.getFilename() );

            loadFile.setFileType( gridFSFile.getMetadata().get("_contentType").toString() );

            loadFile.setFileSize( gridFSFile.getMetadata().get("fileSize").toString() );

            loadFile.setFile( IOUtils.toByteArray(operations.getResource(gridFSFile).getInputStream()) );
        }
        logger.info("Data Collected");
        return loadFile;
    }

}