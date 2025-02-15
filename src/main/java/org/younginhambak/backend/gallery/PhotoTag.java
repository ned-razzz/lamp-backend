package org.younginhambak.backend.gallery;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.younginhambak.backend.tag.Tag;

@Entity
@Table(name = "_photo_tag")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PhotoTag {
  @EmbeddedId
  private PhotoTagId id;

  @MapsId("photoId")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "photo_id")
  private Photo photo;

  @MapsId("tagId")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "tag_id")
  private Tag tag;



}
