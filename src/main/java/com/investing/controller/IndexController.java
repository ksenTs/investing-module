package com.investing.controller;

import com.investing.model.ExchangeTool;
import com.investing.model.ExchangeToolType;
import com.investing.service.ExchangeToolService;
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
@RequestMapping(path = "indexes", produces = MediaType.APPLICATION_JSON_VALUE)
public class IndexController {

    private ExchangeToolService exchangeToolService;

    @Autowired
    public void setExchangeToolService(ExchangeToolService exchangeToolService) {
        this.exchangeToolService = exchangeToolService;
    }

    @GetMapping(value = "list")
    public ResponseEntity getIndexes() {
        return ResponseEntity.ok().body(exchangeToolService.getIndexes());
    }

    @GetMapping(value = "/{code}")
    public ResponseEntity getIndex(@PathVariable("code") String code) {
        return ResponseEntity.ok().body(exchangeToolService.getIndexDetails(code));
    }

    @GetMapping(value = "period-data/{code}")
    public ResponseEntity getPeriodData(@RequestParam(value = "period") String period, @PathVariable("code") String code) {
        return ResponseEntity.ok().body(exchangeToolService.getPeriodData(code, period, ExchangeToolType.INDEX));
    }
}
