package com.yab.multitenantERP.services;

import com.yab.multitenantERP.entity.Branch;
import com.yab.multitenantERP.repositories.BranchRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BranchService {
    BranchRepository branchRepository;

    BranchService(BranchRepository branchRepository){
        this.branchRepository = branchRepository;
    }

    public Branch registerBranch(Branch Branch){
        return branchRepository.save(Branch);
    }

    public List<Branch> getAllBranches(){
        return branchRepository.findAll();
    }

    public Optional<Branch> getBranchById(Long id){
        return branchRepository.findById(id);
    }

    public Branch updateBranch(Long id, Branch branch){
        branchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Branch not found"));
        branch.setId(id);
        return branchRepository.save(branch);
    }

}
