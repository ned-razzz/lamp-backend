package org.younginhambak.backend.archive.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.younginhambak.backend.archive.dto.DocumentSearchRequest;
import org.younginhambak.backend.archive.entity.Document;
import org.younginhambak.backend.archive.entity.QDocument;
import org.younginhambak.backend.archive.entity.QDocumentTag;
import org.younginhambak.backend.tag.QTag;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DocumentRepositoryImpl implements DocumentRepository {

  private final EntityManager em;
  private final JPAQueryFactory queryFactory;

  @Override
  public void save(Document document) {
    em.persist(document);
  }

  @Override
  public Optional<Document> findById(Long id) {
    Document document = em.find(Document.class, id);
    return Optional.ofNullable(document);
  }

  @Override
  public List<Document> findByTitle(String title) {
    String jsql = "select d from Document d where d.title like :title";
    return em.createQuery(jsql, Document.class)
            .setParameter("title", "%" + title + "%")
            .getResultList();
  }

  @Override
  public List<Document> findAll() {
    return em.createQuery("select d from Document d", Document.class)
            .getResultList();
  }

  @Override
  public List<Document> searchByCondition(String title, List<Long> tagIds, String sort) {
    QDocument document = QDocument.document;
    QDocumentTag documentTag = QDocumentTag.documentTag;

    JPAQuery<Document> query = queryFactory
            .selectDistinct(document)
            .from(document);

    // Create predicate for conditions
    BooleanBuilder builder = new BooleanBuilder();
    if (!title.isBlank()) {
      builder.and(document.title.containsIgnoreCase(title));
    }

    if (!tagIds.isEmpty()) {
      builder.and(documentTag.id.tagId.in(tagIds));
      query = query
              .join(document.documentTags, documentTag)
              .groupBy(document.id)
              .having(documentTag.id.tagId.countDistinct().eq((long)tagIds.size()));
    }

    return query
            .where(builder)
            .orderBy(document.updated.desc())
            .fetch();
  }
}
