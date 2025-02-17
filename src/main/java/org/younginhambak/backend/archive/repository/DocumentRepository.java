package org.younginhambak.backend.archive.repository;


import org.younginhambak.backend.archive.entity.Document;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository {

  void save(Document document);

  Optional<Document> findById(Long id);

  List<Document> findByTitle(String title);

  List<Document> findAll();
}
