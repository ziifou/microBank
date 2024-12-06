package com.sif.microserviceApp.service.clients;

import com.sif.microserviceApp.dto.LoansDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "Loans", fallback = LoansFallBack.class)
public interface LoansFeignClient {
    @GetMapping(value="/api/fetch", consumes = "application/json")
    ResponseEntity<LoansDto> fetchLoanDetails(@RequestParam String mobileNumber);
}
