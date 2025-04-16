package com.yab.multitenantERP.controllers;

import com.yab.multitenantERP.entity.Document;
import com.yab.multitenantERP.enums.EmployeeDocumentType;
import com.yab.multitenantERP.services.DocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/documents")
public class DocumentController {
    
    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/{employeeId}")
    public ResponseEntity<Document> uploadDocument(
            @RequestBody Document document,
            @PathVariable Long employeeId
    ) {
        try {
            Document newDocument = documentService.uploadDocument(document, employeeId);
            return ResponseEntity.ok(document);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<List<Document>> getDocuments(@PathVariable Long employeeId) {
        return ResponseEntity.ok(documentService.getDocumentsByEmployee(employeeId));
    }

    @DeleteMapping("/{documentId}")
    public ResponseEntity<String> deleteDocument(@PathVariable Long documentId) {
        documentService.deleteDocument(documentId);
        return ResponseEntity.ok("Document deleted successfully");
    }
}
