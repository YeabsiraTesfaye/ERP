package com.yab.multitenantERP.dtos;

import com.yab.multitenantERP.entity.Employee;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagerAndSupervisorDTO {
    Long managerId;
    Long supervisorId;
}
