package com.yab.multitenantERP.services;

import com.yab.multitenantERP.entity.Position;
import com.yab.multitenantERP.repositories.PositionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PositionService {
    PositionRepository positionRepository;

    PositionService(PositionRepository positionRepository){
        this.positionRepository = positionRepository;
    }

    public Position registerPosition(Position Position){
        return positionRepository.save(Position);
    }

    public List<Position> getAllPositiones(){
        return positionRepository.findAll();
    }

    public Optional<Position> getPositionById(Long id){
        return positionRepository.findById(id);
    }

    public Position updatePosition(Long id, Position position){
        positionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Position not found"));
        position.setId(id);
        return positionRepository.save(position);
    }

}
