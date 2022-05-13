package com.configurator.controller;

import com.configurator.model.LoadFile;
import com.configurator.service.LocalFileUpload;
import com.configurator.service.RetriveFile;
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

import com.configurator.service.UploadConfigService;

import java.io.IOException;

@RestController
@RequestMapping("fileconfig")
public class UploadConfigController {
    private static final Logger logger = LogManager.getLogger(UploadConfigController.class);

    @Autowired
    UploadConfigService fileUploadService;

    @Autowired
    RetriveFile retriveFile;

    @Autowired
    LocalFileUpload localFileUpload;


    private static final String UPLOAD_SERVICE = "uploadService";

    @PostMapping(path = "/upload")
    @CircuitBreaker(name = UPLOAD_SERVICE, fallbackMethod = "uploadServiceFallback")
    public ResponseEntity<Object> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        fileUploadService.before();
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

    @PostMapping("/localupld")
    public ResponseEntity<Object> localupload(@RequestParam("file")MultipartFile file){
        localFileUpload.localuploadFile(file);
        return new ResponseEntity<Object>("The File Uploaded in the Current Directory", HttpStatus.OK);
    }

}