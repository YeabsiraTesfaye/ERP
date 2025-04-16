package com.yab.multitenantERP.dtos;

import com.yab.multitenantERP.entity.Address;
import com.yab.multitenantERP.enums.Gender;
import com.yab.multitenantERP.enums.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class EmployeeDTO {
    private String firstName;
    private String middleName;
    private String lastName;
    private Gender gender;
    private LocalDate birthDate;
    private String tinNumber;
    private String email;
    private String phoneNumber;
    private Long departmentId;  // Instead of a full Department object
    private Long branchId;
    private Long positionId;
    private LocalDate dateOfHire;
    private Status status;
    private String photo;
    private List<Address> addressHistory;
}
