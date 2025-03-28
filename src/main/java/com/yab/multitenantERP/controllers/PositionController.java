package com.yab.multitenantERP.controllers;

import java.util.List;
import java.util.Optional;

import com.yab.multitenantERP.entity.Position;
import com.yab.multitenantERP.services.PositionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/position")
class PositionController {

    private final PositionService positionService;

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
    public Optional<Position> getPositionById(@PathVariable Long id) {
        return positionService.getPositionById(id);
    }

    @PutMapping("/{id}")
    public Position updatePosition(@PathVariable Long id, @RequestBody Position position) {
        return positionService.updatePosition(id, position);
    }

}