package org.younginhambak.backend.archive.service;

import org.younginhambak.backend.archive.entity.Document;

import java.util.List;
import java.util.Optional;

public interface DocumentService {

  public Optional<Document> getDocument();

  public List<Document> getDocumantAll();

  public void createDocument();

  public void updatedDocument();

  public void deleteDocument();
}
