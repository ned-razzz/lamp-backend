package org.younginhambak.backend.archive.service;

import org.younginhambak.backend.archive.dto.*;
import org.younginhambak.backend.archive.entity.Document;
import org.younginhambak.backend.archive.entity.DocumentTag;
import org.younginhambak.backend.archive.entity.DocumentTagId;

import java.util.List;
import java.util.Optional;

public interface DocumentService {
  Optional<Document> getDocument(Long documentId);
  List<Document> getDocumentAll();
  List<DocumentTag> getDocumentTags(List<DocumentTagId> documentTagIds);
  DocumentDetailResponse readDocument(Long documentId);
  List<DocumentDetailResponse> readDocuments();
  List<DocumentDetailResponse> readDocuments(DocumentSearchRequest searchRequest);
  List<DocumentInfoResponse> readDocumentsInfo();
  Long createDocument(DocumentCreateRequest createDto);
  void updateDocument(Long documentId, DocumentUpdateRequest updateDto);
  void deleteDocument(Long documentId);
}
