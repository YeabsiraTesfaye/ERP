package com.yab.multitenantERP.services;

import com.yab.multitenantERP.entity.Document;
import com.yab.multitenantERP.entity.Employee;
import com.yab.multitenantERP.enums.EmployeeDocumentType;
import com.yab.multitenantERP.repositories.DocumentRepository;
import com.yab.multitenantERP.repositories.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentService {
    private final DocumentRepository documentRepository;
    private final EmployeeRepository employeeRepository;

    public DocumentService(DocumentRepository documentRepository, EmployeeRepository employeeRepository) {
        this.documentRepository = documentRepository;
        this.employeeRepository = employeeRepository;
    }

    // Upload or Update Employee Document
    public Document uploadDocument(Document document, Long employeeId) throws Exception {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

//        Document document = new Document();
//        document.setName(name);
//        document.setEmployeeDocumentType(type);
//        document.setFileName(fileName);
//        document.setEmployee(employee);
        document.setEmployee(employee);

        return documentRepository.save(document);
    }

    // Get all documents for an employee
    public List<Document> getDocumentsByEmployee(Long employeeId) {
        return documentRepository.findByEmployeeId(employeeId);
    }

    // Delete a document
    public void deleteDocument(Long documentId) {
        documentRepository.deleteById(documentId);
    }
}
