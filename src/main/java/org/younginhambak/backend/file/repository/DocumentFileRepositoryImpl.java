package org.younginhambak.backend.file.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.younginhambak.backend.file.entity.DataFile;
import org.younginhambak.backend.file.entity.DocumentFile;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DocumentFileRepositoryImpl implements DocumentFileRepository {

  private final EntityManager em;

  @Override
  public void save(DocumentFile file) {
    em.persist(file);
  }

  @Override
  public Optional<DocumentFile> findById(Long id) {
    DocumentFile file = em.find(DocumentFile.class, id);
    return Optional.ofNullable(file);
  }

  @Override
  public List<DocumentFile> findAll() {
    return em.createQuery("select df from DocumentFile df", DocumentFile.class)
            .getResultList();
  }
}
