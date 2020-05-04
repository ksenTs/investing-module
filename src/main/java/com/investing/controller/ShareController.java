package com.investing.controller;

import com.investing.service.ShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin(origins = "*")
public class ShareController {

    private ShareService shareService;

    @Autowired
    public void setShareService(ShareService shareService) {
        this.shareService = shareService;
    }

    @GetMapping(value = "/actual-shares")
    public ResponseEntity getSharesList() {
        return ResponseEntity.ok().body(shareService.getShares());
    }

    @GetMapping(value = "/monthly_values")
    public ResponseEntity getMonthlyValues() {
        return ResponseEntity.ok().body(shareService.getMonthlyValues());
    }
}
