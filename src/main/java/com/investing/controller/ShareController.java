package com.investing.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ShareController {

    @GetMapping(value = "/actual-shares")
    public String getSharesList() {
        return "Hello";
    }
}
