package com.upgrader.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;


import org.springframework.data.mongodb.core.query.Query;

@Service
public class DeleteCollection {

    @Autowired
    GridFsOperations gridFsOperations;

    public void before() {
        gridFsOperations.delete(new Query());

    }


}
