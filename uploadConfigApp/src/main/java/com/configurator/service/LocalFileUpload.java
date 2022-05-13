package com.configurator.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class LocalFileUpload {
    @Value("${configFilepath}")
    private String propertyPath;
    private static final Logger logger = LogManager.getLogger(LocalFileUpload.class);
    public void localuploadFile(MultipartFile file) {
        try {
            File f = new File(file.getOriginalFilename());
            Path newFilePath = Paths.get(propertyPath + f);
    //        Files.createFile(newFilePath);
            file.transferTo(newFilePath);
        } catch (IllegalStateException e) {
            logger.info("IllegalStateException occured" + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            logger.info("IOException occured" + e.getMessage());
            e.printStackTrace();
        }
    }
}
