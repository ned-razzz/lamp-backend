package org.younginhambak.backend.gallery;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.younginhambak.backend.tag.Tag;

/**
 * Photo와 Tag의 n:m 관계를 관리하는 bridge Entity입니다.
 * @version 1.0
 */
@Entity
@Table(name = "_photo_tag")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PhotoTag {
  @EmbeddedId
  private PhotoTagId id;

  // Entity Correlation
  @MapsId("photoId")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "photo_id")
  private Photo photo;

  @MapsId("tagId")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "tag_id")
  private Tag tag;


  // Relationship Convenience Method
  public void addPhoto(Photo photo) {
    this.photo = photo;
    photo.getPhotoTags().add(this);
  }

  public void removePhoto() {
    photo.getPhotoTags().remove(this);
    photo = null;
  }

  public void addTag(Tag tag) {
    this.tag = tag;
    tag.getPhotoTags().add(this);
  }

  public void removeTag() {
    tag.getPhotoTags().remove(this);
    tag = null;
  }

  // Business Logic
  /**
   * 새로운 PhotoTag를 생성합니다.
   * @param tag 태그
   * @return 새로 생성한 PhotoTag
   */
  public static PhotoTag create(Tag tag) {
    PhotoTag photoTag = new PhotoTag();
    photoTag.addTag(tag);
    return photoTag;
  }

  /**
   * PhotoTag를 삭제합니다.
   * 기존 연관관계를 전부 제거합니다. (DB 작업 필요)
   */
  public void delete() {
    removePhoto();
    removeTag();
  }
}
