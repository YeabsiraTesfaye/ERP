package com.yab.multitenantERP.services;

import com.yab.multitenantERP.entity.AttendancePolicy;
import com.yab.multitenantERP.repositories.AttendancePolicyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttendancePolicyService {

    private final AttendancePolicyRepository policyRepository;

    AttendancePolicyService(AttendancePolicyRepository attendancePolicyRepository){
        this.policyRepository = attendancePolicyRepository;
    }

    public void saveAll(List<com.yab.multitenantERP.dtos.AttendancePolicyRequest> requests) {
        List<AttendancePolicy> policies = requests.stream().map(req -> {
            return AttendancePolicy.builder()
                    .session(req.getSession())
                    .lateAfter(req.getLateAfter())
                    .build();
        }).collect(Collectors.toList());

        policyRepository.deleteAll(); // Clear existing before adding new
        policyRepository.saveAll(policies);
    }

    public List<AttendancePolicy> getAll() {
        return policyRepository.findAll();
    }
}
