package org.younginhambak.backend.file.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.younginhambak.backend.file.entity.DocumentFile;
import org.younginhambak.backend.file.entity.PhotoFile;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PhotoFileRepositoryImpl implements PhotoFileRepository {

  private final EntityManager em;

  @Override
  public void save(PhotoFile file) {
    em.persist(file);
  }

  @Override
  public Optional<PhotoFile> findById(Long id) {
    PhotoFile file = em.find(PhotoFile.class, id);
    return Optional.ofNullable(file);
  }

  @Override
  public List<PhotoFile> findByIdIn(List<Long> ids) {
    return em.createQuery("select pf from PhotoFile pf where pf.id in :ids", PhotoFile.class)
            .setParameter("ids", ids)
            .getResultList();
  }

  @Override
  public List<PhotoFile> findAll() {
    return em.createQuery("select pf from PhotoFile pf", PhotoFile.class)
            .getResultList();
  }
}
