package org.younginhambak.backend.archive.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;
import org.younginhambak.backend.tag.Tag;

/**
 * Document와 Tag의 n:m 관계를 관리하는 bridge Entity입니다.
 * @version 1.0
 */
@Entity
@Table(name = "_document_tag")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DocumentTag {

  @EmbeddedId
  private DocumentTagId id = new DocumentTagId();

  // Entity Correlation
  @MapsId("documentId")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "document_id")
  private Document document;

  @MapsId("tagId")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "tag_id")
  private Tag tag;

  // Relationship Convenience Method
  public void addDocument(Document document) {
    Assert.notNull(document, "document parameter is null.");
    this.document = document;
    document.getDocumentTags().add(this);
  }

  public void removeDocument() {
    Assert.state(this.document != null, "document field is already null.");
    this.document.getDocumentTags().remove(this);
    this.document = null;
  }

  public void addTag(Tag tag) {
    Assert.notNull(tag, "tag parameter is null.");
    this.tag = tag;
    tag.getDocumentTags().add(this);
  }

  public void removeTag() {
    Assert.state(this.tag != null, "tag field is already null.");
    this.tag.getDocumentTags().remove(this);
    this.tag = null;
  }

  // Business Logic
  /**
   * 새로운 DocumentTag를 생성합니다.
   * @param tag 태그
   * @return 새로 생성한 DocumentTag
   */
  public static DocumentTag create(Tag tag) {
    DocumentTag documentTag = new DocumentTag();
    documentTag.addTag(tag);
    return documentTag;
  }

  /**
   * DocumentTag를 삭제합니다.
   * 기존 연관관계를 전부 제거합니다. (DB 작업 필요)
   */
  public void delete() {
    removeDocument();
    removeTag();
  }
}
