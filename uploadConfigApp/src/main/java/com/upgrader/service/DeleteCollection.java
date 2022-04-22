package com.upgrader.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;


import org.springframework.data.mongodb.core.query.Query;

@Service
public class DeleteCollection {

    @Autowired
    GridFsOperations gridFsOperations;

    private static final Logger logger = LogManager.getLogger(DeleteCollection.class);

    public void before() {
        gridFsOperations.delete(new Query());
        logger.info("Deleted Successfully");

    }


}
