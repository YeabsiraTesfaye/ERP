package com.yab.multitenantERP.dtos;

import com.yab.multitenantERP.entity.Employee;
import com.yab.multitenantERP.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private Long tenant_id;
    private  Set<Role> roles;
    private Long employeeId;
}
