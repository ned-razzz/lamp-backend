package org.younginhambak.backend.gallery;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.younginhambak.backend.member.Member;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 갤러리에 저장되는 사진들을 관리합니다.
 * Photo 객체는 하나의 사진 데이터를 가질 수 있고 여러 개의 태그를 가질 수 있습니다.
 * Aggregate Root입니다.
 * @version 1.0
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Photo {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "photo_id")
  private Long id;

  @NotBlank
  @Size(min = 3, max = 30)
  private String title;

  @Size(max = 300)
  private String description;

  @NotBlank
  @Size(max = 30)
  private String photographer;

  private LocalDateTime takenAt;

  // Metadata
  @NotNull
  @Enumerated(EnumType.STRING)
  private PhotoStatus status;

  @PastOrPresent
  private LocalDateTime created;

  @PastOrPresent
  private LocalDateTime updated;

  // Entity Correlation
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @OneToMany(mappedBy = "photo", fetch = FetchType.LAZY)
  private List<PhotoTag> photoTags = new ArrayList<>();

  @Setter(AccessLevel.PROTECTED)
  @OneToOne(mappedBy = "photo", fetch = FetchType.LAZY)
  private PhotoFile file;

  // Relationship Convenience Method
  public void addMember(Member member) {
    this.member = member;
    member.getPhotos().add(this);
  }

  public void removeMember() {
    this.member.getPhotos().remove(this);
    this.member = null;
  }

  // Business Logic

  /**
   * 새로운 Photo를 생성합니다.
   * @param title 사진 제목
   * @param description 사진 설명
   * @param photographer 촬영자
   * @param takenAt 활영 일자
   * @param member 사진 소유자
   * @param file 이미지 파일
   * @param photoTags 사진 태그들
   * @return 새로 생성된 Photo 객체
   */
  public static Photo create(
          String title,
          String description,
          String photographer,
          LocalDateTime takenAt,
          Member member,
          PhotoFile file,
          List<PhotoTag> photoTags
  ) {
    Photo photo = new Photo();
    photo.title = title;
    photo.description = description;
    photo.photographer = photographer;
    photo.takenAt = takenAt;

    photo.status = PhotoStatus.ACTIVE;
    photo.created = LocalDateTime.now();
    photo.updated = LocalDateTime.now();

    photo.addMember(member);
    file.addPhoto(photo);
    photoTags.forEach(photoTag -> {
      photoTag.addPhoto(photo);
    });

    return photo;
  }

  /**
   * 이미 존재하는 Photo 객체를 수정합니다.
   * Photo 객체에서 연관관계가 끊긴 PhotoFile, PhotoTag 객체들은 삭제 처리됩니다. (DB 작업은 따로 필요합니다.)
   * @param title 사진 제목
   * @param description 사진 설명
   * @param photographer 촬영자
   * @param takenAt 활영 일자
   * @param member 사진 소유자
   * @param file 이미지 파일
   * @param photoTags 사진 태그들
   */
  public void update(
          String title,
          String description,
          String photographer,
          LocalDateTime takenAt,
          Member member,
          PhotoFile file,
          List<PhotoTag> photoTags) {
    this.title = title;
    this.description = description;
    this.photographer = photographer;
    this.takenAt = takenAt;

    updateMember(member);
    updateFile(file);
    updatePhotoTags(photoTags);

    this.updated = LocalDateTime.now();
  }

  /**
   * Photo 객체를 삭제합니다.
   * 해당 Photo 객체와 연관관계가 설정된 PhotoFile, PhotoTag 객체들은 삭제 처리됩니다. (DB 작업은 따로 필요합니다.)
   */
  public void delete() {
    removeMember();
    file.delete();
    new ArrayList<>(photoTags).forEach(PhotoTag::delete);

    status = PhotoStatus.DELETED;
    updated = LocalDateTime.now();
  }

  private void updateMember(Member member) {
    if (member.equals(this.member)) {
      return;
    }
    removeMember();
    addMember(member);
  }

  private void updateFile(PhotoFile file) {
    if (file.equals(this.file)) {
      return;
    }
    this.file.removePhoto();
    file.addPhoto(this);
  }

  private void updatePhotoTags(List<PhotoTag> photoTags) {
    // 기존 photoTag 중에서 업데이트 photoTag에 없는 객체는 삭제
    List<PhotoTag> list = this.photoTags.stream()
            .filter(photoTag -> !photoTags.contains(photoTag))
            .toList();
    list.forEach(PhotoTag::delete);

    // 이미 존재하는 photoTag 제외하고 새로운 photoTag 추가
    photoTags.stream()
            .filter(photoTag -> !this.photoTags.contains(photoTag))
            .forEach(photoTag -> photoTag.addPhoto(this));
  }
}
