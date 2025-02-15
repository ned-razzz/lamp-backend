package org.younginhambak.backend.archive;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.younginhambak.backend.tag.Tag;

@Entity
@Table(name = "_document_tag")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DocumentTag {

  @EmbeddedId
  private DocumentTagId id;

  @MapsId("documentId")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "document_id")
  private Document document;

  @MapsId("tagId")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "tag_id")
  private Tag tag;
}
