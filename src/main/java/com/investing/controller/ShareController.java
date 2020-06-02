package com.investing.controller;

import com.investing.model.ExchangeToolType;
import com.investing.service.ExchangeToolService;
import com.investing.service.ShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "shares", produces = MediaType.APPLICATION_JSON_VALUE)
public class ShareController {

    private ShareService shareService;
    private ExchangeToolService exchangeToolService;

    @GetMapping(value = "/list")
    public ResponseEntity getShares() {
        return ResponseEntity.ok().body(shareService.getShares());
    }

    @GetMapping(value = "/{code}")
    public ResponseEntity getShare(@PathVariable("code") String code) {
        return ResponseEntity.ok().body(shareService.getShareDetails(code));
    }

    @GetMapping(value = "period-data/{code}")
    public ResponseEntity getPeriodData(@RequestParam(value = "period") String period, @PathVariable("code") String code) {
        return ResponseEntity.ok().body(exchangeToolService.getPeriodData(code, period, ExchangeToolType.SHARE));
    }

    @GetMapping(value = "growth-leaders")
    public ResponseEntity getGrowthLeaders() {
        return ResponseEntity.ok().body(shareService.getSharesGrowthLeaders());
    }

    @Autowired
    public void setShareService(ShareService shareService) {
        this.shareService = shareService;
    }

    @Autowired
    public void setExchangeToolService(ExchangeToolService exchangeToolService) {
        this.exchangeToolService = exchangeToolService;
    }
}
