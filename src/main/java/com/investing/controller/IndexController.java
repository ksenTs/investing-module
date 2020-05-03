package com.investing.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @GetMapping(value = "/indexes")
    public String getIndexes() {
        return "Index";
    }
}
