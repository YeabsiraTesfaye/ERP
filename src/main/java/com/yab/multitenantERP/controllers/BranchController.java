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

    private final BranchService BranchService;

    BranchController(BranchService BranchService) {
        this.BranchService = BranchService;
    }
    @PostMapping
    public Branch createBranch(@RequestHeader("X-Company-Schema") String companyName, @RequestBody Branch Branch) {

        return BranchService.registerBranch(Branch);
    }

    @GetMapping
    public List<Branch> getAllBranchs(@RequestHeader("X-Company-Schema") String companyName) {
        return BranchService.getAllBranches();
    }

    @GetMapping("/{id}")
    public Optional<Branch> getBranchById(@PathVariable Long id) {
        return BranchService.getBranchById(id);
    }

    @PutMapping("/{id}")
    public Branch updateBranch(@PathVariable Long id, @RequestBody Branch Branch) {
        return BranchService.updateBranch(id, Branch);
    }
}