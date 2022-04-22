package com.upgrader.controller;

import com.upgrader.model.LoadFile;
import com.upgrader.service.DeleteCollection;
import com.upgrader.service.RetriveFile;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.upgrader.service.FileUploadService;

import java.io.IOException;

@RestController
@RequestMapping("fileconfig")
public class FileUploadController {
    private static final Logger logger = LogManager.getLogger(FileUploadController.class);

    @Autowired
    FileUploadService fileUploadService;

    @Autowired
    RetriveFile retriveFile;

    @Autowired
    DeleteCollection deleteCollection;


    private static final String UPLOAD_SERVICE = "uploadService";

    @PostMapping(path = "/upload")
    @CircuitBreaker(name = UPLOAD_SERVICE, fallbackMethod = "uploadServiceFallback")
    public ResponseEntity<Object> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        deleteCollection.before();
        logger.info("Uploaded");
        return new ResponseEntity<Object>(fileUploadService.uploadFile(file), HttpStatus.OK);
    }

    public ResponseEntity<String> uploadServiceFallback(Exception e) {
        return new ResponseEntity<String>("Service is down", HttpStatus.OK);
    }

    @GetMapping("/retrive")
    public ResponseEntity<ByteArrayResource> download() throws IOException {
        LoadFile loadFile = retriveFile.downloadFile();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(loadFile.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + loadFile.getFilename() + "\"")
                .body(new ByteArrayResource(loadFile.getFile()));
    }


}