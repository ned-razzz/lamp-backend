package org.younginhambak.backend.archive.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.younginhambak.backend.archive.entity.Document;
import org.younginhambak.backend.archive.repository.DocumentRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

  private final DocumentRepository documentRepository;

  @Override
  public Optional<Document> getDocument() {
    return Optional.empty();
  }

  @Override
  public List<Document> getDocumantAll() {
    return List.of();
  }

  @Override
  public void createDocument() {

  }

  @Override
  public void updatedDocument() {

  }

  @Override
  public void deleteDocument() {

  }
}
