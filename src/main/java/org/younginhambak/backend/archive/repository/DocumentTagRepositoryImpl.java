package org.younginhambak.backend.archive.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.younginhambak.backend.archive.entity.DocumentTag;
import org.younginhambak.backend.archive.entity.DocumentTagId;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DocumentTagRepositoryImpl implements DocumentTagRepository {

  private final EntityManager em;

  @Override
  public Optional<DocumentTag> findById(DocumentTagId id) {
    DocumentTag documentTag = em.find(DocumentTag.class, id);
    return Optional.ofNullable(documentTag);
  }

  @Override
  public List<DocumentTag> findByIdsIn(List<DocumentTagId> ids) {
    return em.createQuery("select dt from DocumentTag dt where dt.id in :ids", DocumentTag.class)
            .setParameter("ids", ids)
            .getResultList();
  }
}
