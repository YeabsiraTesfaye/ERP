package com.yab.multitenantERP.dtos;

import com.yab.multitenantERP.enums.AttendanceSession;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendancePolicyRequest {
    private AttendanceSession session;
    private LocalTime lateAfter;
}
