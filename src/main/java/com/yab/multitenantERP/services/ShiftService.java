package com.yab.multitenantERP.services;

import com.yab.multitenantERP.entity.Employee;
import com.yab.multitenantERP.entity.Position;
import com.yab.multitenantERP.entity.Shift;
import com.yab.multitenantERP.repositories.EmployeeRepository;
import com.yab.multitenantERP.repositories.PositionRepository;
import com.yab.multitenantERP.repositories.ShiftRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ShiftService {
    ShiftRepository shiftRepository;
    EmployeeRepository employeeRepository;
    PositionRepository positionRepository;
    ShiftService(ShiftRepository shiftRepository,
                 EmployeeRepository employeeRepository,
                 PositionRepository positionRepository){
        this.shiftRepository = shiftRepository;
        this.employeeRepository = employeeRepository;
        this.positionRepository = positionRepository;
    }

    public Shift createShift(Shift shift){
        return shiftRepository.save(shift);
    }

    public Employee assignShiftToEmployee(Long employeeId, Long shiftId) {
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        if(employee.isPresent()){
            Optional<Shift> shift = shiftRepository.findById(shiftId);
            shift.ifPresent(value -> employee.get().setShift(value));
            return employeeRepository.save(employee.get());

        }
        return null;
    }

    public Position assignShiftToPosition(Long positionId, Long shiftId) {
        Optional<Position> position = positionRepository.findById(positionId);
        if(position.isPresent()){
            Optional<Shift> shift = shiftRepository.findById(shiftId);
            shift.ifPresent(value -> position.get().setShift(value));
            return positionRepository.save(position.get());

        }
        return null;
    }

    public List<Shift> getShifts() {

        return shiftRepository.findAll();
    }

    public Shift updateShift(Long shiftId, Shift shift) {
        Optional<Shift> oldShift = shiftRepository.findById(shiftId);
        if(oldShift.isPresent()){
            shift.setId(shiftId);
        }
        return shiftRepository.save(shift);
    }

    public Shift getShift(Long shiftId) {
        Optional<Shift> shift = shiftRepository.findById(shiftId);
        return shift.orElse(null);
    }

    public Shift setDefaultShift(Long shiftId) {
        Optional<Shift> shift = shiftRepository.findById(shiftId);

            List<Shift> shifts = shiftRepository.findAll();
            for (Shift s : shifts){
                s.setDefault(Objects.equals(s.getId(), shiftId));
            }
          shiftRepository.saveAll(shifts);
            return shift.get();

    }
}
