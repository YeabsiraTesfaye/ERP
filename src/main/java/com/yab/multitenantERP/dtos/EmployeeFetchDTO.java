package com.yab.multitenantERP.dtos;

import com.yab.multitenantERP.entity.Employee;
import com.yab.multitenantERP.entity.EmployeeAllowance;
import com.yab.multitenantERP.entity.EmployeeBenefit;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EmployeeFetchDTO {
    Employee employee;
    List<EmployeeAllowance> employeeAllowances;
    List<EmployeeBenefit> employeeBenefits;
}
