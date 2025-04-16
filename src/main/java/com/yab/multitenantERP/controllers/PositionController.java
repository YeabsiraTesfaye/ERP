package com.yab.multitenantERP.controllers;

import java.util.List;

import com.yab.multitenantERP.dtos.PositionFetchDTO;
import com.yab.multitenantERP.entity.Position;
import com.yab.multitenantERP.services.PositionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/position")
class PositionController {

    private final PositionService positionService;
    private static final Logger logger = LoggerFactory.getLogger(PositionController.class);

    PositionController(PositionService positionService) {
        this.positionService = positionService;
    }
    @PostMapping
    public Position createPosition(@RequestHeader("X-Company-Schema") String companyName, @RequestBody Position position) {
        return positionService.registerPosition(position);
    }

    @GetMapping
    public List<Position> getAllPositions() {
        return positionService.getAllPositions();
    }


    @GetMapping("/getPositionByDepartmentId/{id}")
    public List<Position> getPositionsByDepartment(@PathVariable Long id) {
        return positionService.getPositionsByDepartment(id);
    }

    @GetMapping("/{id}")
    public PositionFetchDTO getPositionById(@PathVariable Long id) {
        return positionService.fetchPositionAllowanceDTO(id);
    }

    @PutMapping("/{id}")
    public Position updatePosition(@PathVariable Long id, @RequestBody Position position) {
        return positionService.updatePosition(id, position);
    }

}