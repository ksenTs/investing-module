package com.investing.controller;

import com.investing.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "indexes", produces = MediaType.APPLICATION_JSON_VALUE)
public class IndexController {

    private IndexService indexService;

    @Autowired
    public void setIndexService(IndexService indexService) {
        this.indexService = indexService;
    }

    @GetMapping(value = "list")
    public ResponseEntity getIndexes() {
        return ResponseEntity.ok().body(indexService.getIndexes());
    }

//    @GetMapping(value = "/{code}")
//    public ResponseEntity getIndex(@PathVariable("code") String code) {
//        return ResponseEntity.ok().body(indexService.getIndex(code));
//    }
}
