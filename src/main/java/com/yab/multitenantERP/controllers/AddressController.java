package com.yab.multitenantERP.controllers;

import com.yab.multitenantERP.entity.Address;
import com.yab.multitenantERP.services.AddressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/address")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping("/{employeeId}")
    public ResponseEntity<Address> registerAddress(
            @RequestBody Address address,
            @PathVariable Long employeeId
    ) {
        try {
            Address newAddress = addressService.updateCurrentAddress(address, employeeId);
            return ResponseEntity.ok(newAddress);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Address> getAddress(@PathVariable Long id) {
        Optional<Address> address = addressService.getAddressById(id);
        if(address.isPresent()){
            return ResponseEntity.ok(addressService.getAddressById(id).get());
        }
        return ResponseEntity.badRequest().body(null);
    }
    @GetMapping("/employee/{id}")
    public List<Address> getAddressByEmployeeId(@PathVariable Long employeeId) {

        return addressService.getAddressByEmployeeId(employeeId);

    }
}
