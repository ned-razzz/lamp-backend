package org.younginhambak.backend.archive.service;

import org.younginhambak.backend.archive.dto.DocumentCreateDto;
import org.younginhambak.backend.archive.dto.DocumentUpdateDto;
import org.younginhambak.backend.archive.entity.Document;

import java.util.List;
import java.util.Optional;

public interface DocumentService {

  Optional<Document> getDocument(Long documentId);
  List<Document> getDocumentAll();
  void createDocument(DocumentCreateDto createDto);
  void updatedDocument(Long documentId, DocumentUpdateDto updateDto);
  void deleteDocument(Long documentId);
}
