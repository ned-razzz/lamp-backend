package org.younginhambak.backend.archive.service;

import org.younginhambak.backend.archive.dto.DocumentCreateRequest;
import org.younginhambak.backend.archive.dto.DocumentDetailResponse;
import org.younginhambak.backend.archive.dto.DocumentInfoResponse;
import org.younginhambak.backend.archive.dto.DocumentUpdateRequest;
import org.younginhambak.backend.archive.entity.Document;
import org.younginhambak.backend.archive.entity.DocumentTag;
import org.younginhambak.backend.archive.entity.DocumentTagId;

import java.util.List;
import java.util.Optional;

public interface DocumentService {
  Optional<Document> getDocument(Long documentId);
  List<Document> getDocumentAll();
  List<DocumentTag> getDocumentTags(List<DocumentTagId> documentTagIds);
  DocumentDetailResponse readDocumentDetail(Long documentId);
  List<DocumentInfoResponse> readDocumentsInfo();
  void createDocument(DocumentCreateRequest createDto);
  void updateDocument(Long documentId, DocumentUpdateRequest updateDto);
  void deleteDocument(Long documentId);
}
