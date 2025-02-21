package org.younginhambak.backend.archive.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.younginhambak.backend.archive.dto.DocumentCreateRequest;
import org.younginhambak.backend.archive.dto.DocumentGetResponse;
import org.younginhambak.backend.archive.dto.DocumentUpdateRequest;
import org.younginhambak.backend.archive.service.DocumentService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/documents")
@Slf4j
@RequiredArgsConstructor
public class DocumentController {

  private final DocumentService documentService;

  @GetMapping("/{documentId}")
  public DocumentGetResponse getDocument(@PathVariable Long documentId) {
    return documentService.readDocument(documentId);
  }

  @GetMapping
  public List<DocumentGetResponse> getDocumentAll() {
    return documentService.readDocumentAll();
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public void createDocument(
          @RequestBody DocumentCreateRequest createDto
  ) {
    documentService.createDocument(createDto);
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PatchMapping("/{documentId}")
  public void updatedDocument(
          @PathVariable Long documentId,
          @RequestBody DocumentUpdateRequest updateDto
  ) {
    documentService.updateDocument(documentId, updateDto);
  }

  @DeleteMapping("/{documentId}")
  public void deleteDocument(@PathVariable Long documentId) {
    documentService.deleteDocument(documentId);
  }
}
