package com.interviewsim.controller;

import com.interviewsim.dto.*;
import com.interviewsim.service.DomainService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/admin/domains")
@PreAuthorize("hasRole('ADMIN')")
public class AdminDomainController {

    private final DomainService domainService;

    public AdminDomainController(DomainService domainService) { this.domainService = domainService; }

    @PostMapping
    public ResponseEntity<ApiResponse<DomainResponse>> create(@Valid @RequestBody DomainRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Domain created", domainService.createDomain(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DomainResponse>> update(@PathVariable Long id, @Valid @RequestBody DomainRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Domain updated", domainService.updateDomain(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        domainService.deleteDomain(id);
        return ResponseEntity.ok(ApiResponse.success("Domain deleted", null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DomainResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success("Domains fetched", domainService.getAllDomains()));
    }
}
