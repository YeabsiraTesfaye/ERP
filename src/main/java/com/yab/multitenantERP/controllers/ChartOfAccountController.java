package com.yab.multitenantERP.controllers;

import com.yab.multitenantERP.entity.ChartOfAccount;
import com.yab.multitenantERP.services.ChartOfAccountService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/finance")
public class ChartOfAccountController {
    private final ChartOfAccountService chartOfAccountService;
    private static final Logger log = LogManager.getLogger(ChartOfAccountController.class);
    @PostMapping("/chartOfAccount")
    public ResponseEntity<String> register(@RequestBody ChartOfAccount chartOfAccount){
        return new ResponseEntity<>(this.chartOfAccountService.createAccount(chartOfAccount), HttpStatus.OK);
    }

    @GetMapping("/chartOfAccounts")
    public ResponseEntity<List<ChartOfAccount>> getChartOfAccounts(@RequestParam(value = "page",defaultValue = "1") String page, @RequestParam(value = "size",defaultValue = "10") String size, @RequestParam(value = "column",defaultValue = "positionName") String column){
        return new ResponseEntity<>(this.chartOfAccountService.findAll(), HttpStatus.OK);
    }
}
