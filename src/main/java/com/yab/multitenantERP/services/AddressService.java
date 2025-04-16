package com.yab.multitenantERP.services;

import com.yab.multitenantERP.entity.Address;
import com.yab.multitenantERP.entity.AddressHistory;
import com.yab.multitenantERP.entity.Branch;
import com.yab.multitenantERP.entity.Employee;
import com.yab.multitenantERP.repositories.AddressHistoryRepository;
import com.yab.multitenantERP.repositories.AddressRepository;
import com.yab.multitenantERP.repositories.BranchRepository;
import com.yab.multitenantERP.repositories.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {
    AddressRepository addressRepository;
    EmployeeRepository employeeRepository;
    AddressHistoryRepository addressHistoryRepository;

    AddressService(AddressRepository addressRepository,
                   EmployeeRepository employeeRepository,
                   AddressHistoryRepository addressHistoryRepository
    ){
        this.addressRepository = addressRepository;
        this.employeeRepository = employeeRepository;
        this.addressHistoryRepository = addressHistoryRepository;
    }

    public Address updateCurrentAddress( Address newAddress,Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (employee.getAddress() != null) {
            Address oldAddress = employee.getAddress();
            oldAddress.setEmployee(employee);
            oldAddress.setIsPrimary(false);
            employee.getAddressHistory().add(oldAddress);
        }

        // Set new address as current
        newAddress.setEmployee(employee);
        employee.setAddress(newAddress);
        return employeeRepository.save(employee).getAddress();
    }

    public List<Address> getAllAddresses(){
        return addressRepository.findAll();
    }

    public Optional<Address> getAddressById(Long id){
        return addressRepository.findById(id);
    }

    public List<Address> getAddressByEmployeeId(Long id){
        return addressRepository.findByEmployeeId(id);
    }

    public Address updateAddress(Long id, Address address){
        addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found"));
        address.setId(id);
        return addressRepository.save(address);
    }

}
