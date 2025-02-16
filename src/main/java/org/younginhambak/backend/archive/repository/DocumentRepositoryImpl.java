package org.younginhambak.backend.archive.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.younginhambak.backend.archive.entity.Document;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DocumentRepositoryImpl implements DocumentRepository {

  private final EntityManager em;

  @Override
  public void save(Document document) {

  }

  @Override
  public Optional<Document> findById(Long id) {
    return Optional.empty();
  }

  @Override
  public List<Document> findAll() {
    return List.of();
  }
}
