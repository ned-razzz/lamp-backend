package org.younginhambak.backend.archive.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.younginhambak.backend.archive.entity.DocumentTag;
import org.younginhambak.backend.archive.entity.DocumentTagId;
import org.younginhambak.backend.member.Member;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DocumentTagRepositoryImpl implements DocumentTagRepository{

  private final EntityManager em;

  @Override
  public void save(DocumentTag documentTag) {
    em.persist(documentTag);
  }

  @Override
  public Optional<DocumentTag> findById(DocumentTagId id) {
    DocumentTag documentTag = em.find(DocumentTag.class, id);
    return Optional.of(documentTag);
  }

  @Override
  public List<DocumentTag> findByDocumentId(Long id) {
    String jsql = "select dt from DocumentTag dt where e.id.documentId = :documentId";
    return em.createQuery(jsql, DocumentTag.class)
            .setParameter("documentId", id)
            .getResultList();
  }

  @Override
  public List<DocumentTag> findByTagId(Long id) {
    String jsql = "select dt from DocumentTag dt where e.id.tagId = :tagId";
    return em.createQuery(jsql, DocumentTag.class)
            .setParameter("tagId", id)
            .getResultList();
  }

  @Override
  public List<DocumentTag> findAll() {
    return em.createQuery("select dt from DocumentTag dt", DocumentTag.class)
            .getResultList();
  }
}
