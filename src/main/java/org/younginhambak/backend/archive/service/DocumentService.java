package org.younginhambak.backend.archive.service;

import org.younginhambak.backend.archive.dto.DocumentCreateRequest;
import org.younginhambak.backend.archive.dto.DocumentGetResponse;
import org.younginhambak.backend.archive.dto.DocumentUpdateRequest;
import org.younginhambak.backend.archive.entity.Document;

import java.util.List;
import java.util.Optional;

public interface DocumentService {

  Optional<Document> getDocument(Long documentId);
  List<Document> getDocumentAll();
  DocumentGetResponse readDocument(Long documentId);
  List<DocumentGetResponse> readDocumentAll();
  void createDocument(DocumentCreateRequest createDto);
  void updateDocument(Long documentId, DocumentUpdateRequest updateDto);
  void deleteDocument(Long documentId);
}
