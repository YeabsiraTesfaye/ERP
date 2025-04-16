package com.yab.multitenantERP.controllers;

import com.yab.multitenantERP.dtos.AttendancePolicyRequest;
import com.yab.multitenantERP.entity.AttendancePolicy;
import com.yab.multitenantERP.enums.AttendanceSession;
import com.yab.multitenantERP.services.AttendancePolicyService;
import com.yab.multitenantERP.services.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/attendance-session")
@RequiredArgsConstructor
public class AttendanceSessionController {

    private final AttendancePolicyService service;

//    @GetMapping()
//    public List<AttendanceSession> getAllowedSessions() {
//        return service.getAllowedSessions();
//    }
//
//    @PostMapping()
//    public void setAllowedSessions(@RequestBody List<AttendanceSession> sessions) {
//        service.setAllowedSessions(sessions);
//    }

    @PostMapping
    public void setPolicy(@RequestBody List<AttendancePolicyRequest> requests) {
        service.saveAll(requests);
    }

    @GetMapping
    public List<AttendancePolicy> getPolicies() {
        return service.getAll();
    }
}
