package vn.hoidanit.jobhunter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.service.CompanyService;

@Controller
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/companies")
    public ResponseEntity<Company> createNewCompany(@Valid @RequestBody Company postManCompany) {
        Company ericCompany = this.companyService.handleCreateCompany(postManCompany);
        return ResponseEntity.status(HttpStatus.CREATED).body(ericCompany);
    }
}
