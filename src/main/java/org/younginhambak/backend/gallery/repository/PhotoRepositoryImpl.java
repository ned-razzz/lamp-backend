package org.younginhambak.backend.gallery.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.younginhambak.backend.gallery.entity.Photo;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PhotoRepositoryImpl implements PhotoRepository {

  private final EntityManager em;

  @Override
  public void save(Photo photo) {
    em.persist(photo);
  }

  @Override
  public void saveAll(List<Photo> photos) {
    photos.forEach(em::persist);
  }

  @Override
  public Optional<Photo> findById(Long id) {
    Photo photo = em.find(Photo.class, id);
    return Optional.ofNullable(photo);
  }

  @Override
  public List<Photo> findByIdIn(List<Long> ids) {
    String jsql = "select p from Photo p where p.id in :ids";
    return em.createQuery(jsql, Photo.class)
            .setParameter("ids", ids)
            .getResultList();
  }

  @Override
  public List<Photo> findByTitle(String title) {
    String jsql = "select p from Photo p where p.title like :title";
    return em.createQuery(jsql, Photo.class)
            .setParameter("title", "%" + title + "%")
            .getResultList();
  }

  @Override
  public List<Photo> findAll() {
    return em.createQuery("select p from Photo p", Photo.class)
            .getResultList();
  }
}
