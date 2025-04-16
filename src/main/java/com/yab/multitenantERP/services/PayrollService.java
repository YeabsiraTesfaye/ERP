package com.yab.multitenantERP.services;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.yab.multitenantERP.dtos.AllowanceBreakdownDTO;
import com.yab.multitenantERP.dtos.EmployeeFetchDTO;
import com.yab.multitenantERP.dtos.EmployeePayrollDTO;
import com.yab.multitenantERP.entity.*;
import com.yab.multitenantERP.enums.DeductedFromTypes;
import com.yab.multitenantERP.enums.SalaryType;
import com.yab.multitenantERP.enums.Status;
import com.yab.multitenantERP.repositories.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class PayrollService {
    PayrollDateRangeRepository payrollDateRangeRepository;
    PayrollRepository payrollRepository;
    EmployeePayrollRepository employeePayrollRepository;
    EmployeeService employeeService;
    EmployeeRepository employeeRepository;
    IncomeTaxRepository incomeTaxRepository;
    PensionTaxService pensionTaxService;
    PayrollService(PayrollDateRangeRepository payrollDateRangeRepository,
                   PayrollRepository payrollRepository,
                   EmployeeService employeeService,
                   EmployeePayrollRepository employeePayrollRepository,
                   EmployeeRepository employeeRepository,
                   IncomeTaxRepository incomeTaxRepository,
                   PensionTaxService pensionTaxService){
        this.employeeService = employeeService;
        this.payrollRepository = payrollRepository;
        this.payrollDateRangeRepository = payrollDateRangeRepository;
        this.employeePayrollRepository = employeePayrollRepository;
        this.employeeRepository = employeeRepository;
        this.incomeTaxRepository = incomeTaxRepository;
        this.pensionTaxService = pensionTaxService;
    }

    public List<EmployeePayrollDTO> generatePayroll(LocalDate startDate, LocalDate endDate){

        ArrayList<Status> statuses = new ArrayList<>();
        statuses.add(Status.ACTIVE);
        statuses.add(Status.VACATION_LEAVE);
        statuses.add(Status.MATERNITY_LEAVE);
        statuses.add(Status.PATERNITY_LEAVE);
        List<Employee> activeEmployees = employeeRepository.findAll();

        List<EmployeePayrollDTO> employeePayrollDTOS = new ArrayList<>();
        for(Employee e : activeEmployees){

            EmployeePayrollDTO employeePayrollDTO = new EmployeePayrollDTO();
            employeePayrollDTO.setEmployeeId(e.getId());
            employeePayrollDTO.setFullName(e.getFirstName()+" "+e.getMiddleName()+" "+e.getLastName());
            employeePayrollDTO.setFromDate(startDate);
            employeePayrollDTO.setToDate(endDate);
            employeePayrollDTO.setPreparedDate(LocalDateTime.now().toLocalDate());
            Salary salary = new Salary();
            if(e.getSalaryType() == SalaryType.BY_POSITION){
                salary.setAmount(e.getPosition().getSalary().getAmount());
                salary.setPosition(e.getPosition());
                salary.setEmployee(e);
                salary.setId(e.getPosition().getSalary().getId());
                salary.setEffectiveDate(e.getPosition().getSalary().getEffectiveDate());
            }else{
                salary.setAmount(e.getSalary().getAmount());
                salary.setPosition(e.getPosition());
                salary.setEmployee(e);
                salary.setId(e.getSalary().getId());
                salary.setEffectiveDate(e.getSalary().getEffectiveDate());
            }
            employeePayrollDTO.setBasicSalary(salary.getAmount());

            List<AllowanceBreakdownDTO> allowances = calculateAllowances(e.getId());
            employeePayrollDTO.setAllowances(allowances);


            Double totalEarning = calculateTotalEarning(salary.getAmount(), allowances);
            employeePayrollDTO.setTotalEarning(totalEarning);

            Double taxableIncome = calculateTaxableIncome(salary.getAmount(), allowances);
            employeePayrollDTO.setTaxableIncome(taxableIncome);

            Double incomeTax = calculateIncomeTax(taxableIncome);
            employeePayrollDTO.setIncomeTax(incomeTax);

            Double pensionTaxFromEmployee = calculatePensionTaxFromEmployee(salary.getAmount());
            employeePayrollDTO.setPensionTaxFromEmployee(pensionTaxFromEmployee);

            Double pensionTaxFromCompany = calculatePensionTaxFromCompany(salary.getAmount());
            employeePayrollDTO.setPensionTaxFromEmployer(pensionTaxFromCompany);

            List<Deduction> deductions = new ArrayList<>();
            for(Deduction d: e.getDeductions()){
                if(d.isStatus()){
                    deductions.add(d);
                }
            }
//            List<Deduction> deductions = e.getDeductions();
            employeePayrollDTO.setPayrollOtherDeductionPerEmployee(deductions);
            Double totalDeduction = calculateDeduction(deductions, incomeTax, pensionTaxFromEmployee);
            employeePayrollDTO.setTotalDeduction(totalDeduction);

            employeePayrollDTO.setNetSalary(calculateNetPay(salary.getAmount(), incomeTax, pensionTaxFromEmployee, allowances));

            employeePayrollDTOS.add(employeePayrollDTO);
        }
    return employeePayrollDTOS;
    }

    private Double calculateDeduction(List<Deduction> deductions, Double incomeTax, Double pensionTax) {
        Double result = incomeTax + pensionTax;
        for(Deduction d : deductions){
            result += d.getAmount();
        }
        return result;
    }

    public Double calculateTotalEarning(Double total, List<AllowanceBreakdownDTO> allowanceBreakdownDTOS) {
        double result = total;
        for(AllowanceBreakdownDTO allowanceBreakdownDTO : allowanceBreakdownDTOS){
            result += allowanceBreakdownDTO.getTotalAmount().doubleValue();
        }
        return result;
    }

    public Double calculateTaxableIncome(Double total, List<AllowanceBreakdownDTO> allowanceBreakdownDTOS) {
        double result = total;
        for(AllowanceBreakdownDTO allowanceBreakdownDTO : allowanceBreakdownDTOS){
            result += allowanceBreakdownDTO.getTaxableAmount().doubleValue();
        }
        return result;
    }

    public Double calculateIncomeTax(Double taxableIncome){
        IncomeTax incomeTax = incomeTaxRepository.findTaxBracket(taxableIncome);
        Double rate = incomeTax.getRate();
        Double deduction = incomeTax.getDeduction();
        return taxableIncome * (rate/100) + deduction;
    }
    public Double calculatePensionTaxFromEmployee(Double total){
        PensionTax pensionTax = pensionTaxService.getPensionTax();
        BigDecimal employee = pensionTax.getEmployeeContributionRate();
        return employee.doubleValue() * total/100;
    }
    public Double calculatePensionTaxFromCompany(Double total){
        PensionTax pensionTax = pensionTaxService.getPensionTax();
        BigDecimal company = pensionTax.getCompanyContributionRate();
        return company.doubleValue() * total/100;
    }
    public List<AllowanceBreakdownDTO> calculateAllowances(Long id) {
        List<AllowanceBreakdownDTO> allowances = new ArrayList<>();
        EmployeeFetchDTO employee = employeeService.getEmployeeById(id);

        for (EmployeeAllowance ea : employee.getEmployeeAllowances()) {
            BigDecimal amount = ea.getAmount();
            BigDecimal taxable = BigDecimal.ZERO;

            if (ea.getAllowance().getIsTaxable() != null && ea.getAllowance().getIsTaxable()) {
                long taxableFrom = ea.getAllowance().getTaxableAmount() != null ? ea.getAllowance().getTaxableAmount() : 0L;
                taxable = amount.subtract(BigDecimal.valueOf(taxableFrom));
                if (taxable.compareTo(BigDecimal.ZERO) < 0) {
                    taxable = BigDecimal.ZERO;
                }
            }

            allowances.add(new AllowanceBreakdownDTO(
                    ea.getAllowance().getAllowanceType(),
                    amount,
                    taxable
            ));
        }

        return allowances;
    }



    Double calculateNetPay(Double salary, Double incomeTax, Double pensionTax, List<AllowanceBreakdownDTO> allowanceBreakdownDTOS){
        Double result = salary;
        for(AllowanceBreakdownDTO allowanceBreakdownDTO : allowanceBreakdownDTOS){
            result = result + allowanceBreakdownDTO.getTotalAmount().doubleValue();
        }
        return result - incomeTax - pensionTax;

    }


}
