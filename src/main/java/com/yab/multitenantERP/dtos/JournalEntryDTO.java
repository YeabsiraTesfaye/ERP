package com.yab.multitenantERP.dtos;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class JournalEntryDTO {
    private Long id;
    private String reference;
    private String description;
    private LocalDate date;
    private List<JournalEntryLineDTO> lines;
}