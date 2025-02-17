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
    em.persist(document);
  }

  @Override
  public Optional<Document> findById(Long id) {
    Document document = em.find(Document.class, id);
    return Optional.of(document);
  }

  @Override
  public List<Document> findByTitle(String title) {
    String jsql = "select d from Document d where d.title like :title";
    return em.createQuery(jsql, Document.class)
            .setParameter("title", title)
            .getResultList();
  }

  @Override
  public List<Document> findAll() {
    return em.createQuery("select d from Document d", Document.class)
            .getResultList();
  }
}
