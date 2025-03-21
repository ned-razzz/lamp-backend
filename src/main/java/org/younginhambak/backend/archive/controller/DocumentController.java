package org.younginhambak.backend.archive.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.younginhambak.backend.archive.dto.*;
import org.younginhambak.backend.archive.service.DocumentService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
@Slf4j
public class DocumentController {

  private final DocumentService documentService;

  @GetMapping("/{documentId}")
  public DocumentDetailResponse getDocumentDetail(@PathVariable Long documentId) {
    log.info("GET /documents/{}", documentId);
    return documentService.readDocument(documentId);
  }

  @GetMapping
  public List<DocumentDetailResponse> getDocumentList(@ModelAttribute DocumentSearchRequest searchRequest) {
    log.info("GET /documents");
    log.info("? title={} & tags={}", searchRequest.getTitle(), searchRequest.getTags());
    return documentService.readDocuments(searchRequest);
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public void createDocument(
          @RequestBody DocumentCreateRequest createDto
  ) {
    log.info("POST /documents : {}", createDto);
    documentService.createDocument(createDto);
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PutMapping("/{documentId}")
  public void updatedDocument(
          @PathVariable Long documentId,
          @RequestBody DocumentUpdateRequest updateDto
  ) {
    log.info("PUT /documents/{} : {}", documentId, updateDto);
    documentService.updateDocument(documentId, updateDto);
  }

  @DeleteMapping("/{documentId}")
  public void deleteDocument(@PathVariable Long documentId) {
    log.info("DELETE /documents/{}", documentId);
    documentService.deleteDocument(documentId);
  }
}
