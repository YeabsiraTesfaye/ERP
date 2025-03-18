package com.yab.multitenantERP.controllers;

import java.util.List;
import java.util.Optional;

import com.yab.multitenantERP.entity.Branch;
import com.yab.multitenantERP.repositories.BranchRepository;
import com.yab.multitenantERP.services.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/branch")
class BranchController {

    private final BranchService branchService;

    BranchController(BranchService branchService) {
        this.branchService = branchService;
    }
    @PostMapping
    public Branch createBranch(@RequestHeader("X-Company-Schema") String companyName, @RequestBody Branch Branch) {
        return branchService.registerBranch(Branch);
    }

    @GetMapping
    public List<Branch> getAllBranchs() {
        return branchService.getAllBranches();
    }

    @GetMapping("/{id}")
    public Optional<Branch> getBranchById(@PathVariable Long id) {
        return branchService.getBranchById(id);
    }

    @PutMapping("/{id}")
    public Branch updateBranch(@PathVariable Long id, @RequestBody Branch Branch) {
        return branchService.updateBranch(id, Branch);
    }
}