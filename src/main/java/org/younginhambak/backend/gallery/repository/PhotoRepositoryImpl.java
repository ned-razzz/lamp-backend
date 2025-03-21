package org.younginhambak.backend.gallery.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.younginhambak.backend.gallery.entity.Photo;
import org.younginhambak.backend.gallery.entity.QPhoto;
import org.younginhambak.backend.gallery.entity.QPhotoTag;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PhotoRepositoryImpl implements PhotoRepository {

  private final EntityManager em;
  private final JPAQueryFactory queryFactory;

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

  @Override
  public List<Photo> searchByCondition(String title, List<Long> tagIds, String sort) {
    QPhoto photo = QPhoto.photo;
    QPhotoTag photoTag = QPhotoTag.photoTag;

    JPAQuery<Photo> query = queryFactory
            .selectDistinct(photo)
            .from(photo)
            .orderBy(photo.takenAt.desc());

    BooleanBuilder builder = new BooleanBuilder();

    if (!title.isBlank()) {
      builder.and(photo.title.containsIgnoreCase(title));
    }

    if (!tagIds.isEmpty()) {
      builder.and(photoTag.id.tagId.in(tagIds));
      query.join(photo.photoTags, photoTag)
              .groupBy(photo.id)
              .having(photoTag.id.tagId.countDistinct().eq((long)tagIds.size()));
    }

    return query.where(builder).fetch();
  }
}
