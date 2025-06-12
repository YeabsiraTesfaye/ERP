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

    public Address updateCurrentAddress( Address address,Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (employee.getAddress() != null) {
            Address oldAddress = employee.getAddress();
            AddressHistory addressHistory = new AddressHistory();
            addressHistory.setAddressLine1(address.getAddressLine1());
            addressHistory.setAddressLine2(address.getAddressLine2());
            addressHistory.setCity(address.getCity());
            addressHistory.setStateOrProvince(address.getStateOrProvince());
            addressHistory.setPostalCode(address.getPostalCode());
            addressHistory.setCountry(address.getCountry());
            addressHistory.setAddressType(address.getAddressType());
            addressHistory.setSubCity(address.getSubCity());
            addressHistory.setWoreda(address.getWoreda());
            addressHistory.setKebele(address.getKebele());
            addressHistory.setEffectiveDate(address.getEffectiveDate());
            addressHistory.setEmployee(employee);


            addressHistoryRepository.save(addressHistory);
            addressRepository.delete(oldAddress);
        }

        // Set new address as current
        address.setEmployee(employee);
        employee.setAddress(address);
        return addressRepository.save(address);
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
