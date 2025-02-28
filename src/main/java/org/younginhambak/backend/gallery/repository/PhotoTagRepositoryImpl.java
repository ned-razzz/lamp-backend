package org.younginhambak.backend.gallery.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.younginhambak.backend.gallery.entity.PhotoTag;
import org.younginhambak.backend.gallery.entity.PhotoTagId;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PhotoTagRepositoryImpl implements PhotoTagRepository {

  private final EntityManager em;

  @Override
  public Optional<PhotoTag> findById(PhotoTagId id) {
    PhotoTag photoTag = em.find(PhotoTag.class, id);
    return Optional.ofNullable(photoTag);
  }

  @Override
  public List<PhotoTag> findByPhotoId(Long photoId) {
    String jsql = "select pt from PhotoTag pt where pt.id.photoId = :photoId";
    return em.createQuery(jsql, PhotoTag.class)
            .setParameter("photoId", photoId)
            .getResultList();
  }

  @Override
  public List<PhotoTag> findByIdsIn(List<PhotoTagId> ids) {
    String jsql = "select pt from PhotoTag pt where pt.id in :ids";
    return em.createQuery(jsql, PhotoTag.class)
            .setParameter("ids", ids)
            .getResultList();
  }
}
