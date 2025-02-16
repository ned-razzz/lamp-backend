package org.younginhambak.backend.gallery;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.younginhambak.backend.file.DataFile;
import org.younginhambak.backend.file.DataFileStatus;
import org.younginhambak.backend.file.FileExtension;

import java.time.LocalDateTime;
import java.util.EnumSet;

/**
 * DataFile을 상속한 객체입니다.
 * Photo에 속한 실제 사진 데이터의 저장 위치와 메타데이터들을 관리합니다.
 * @version 1.0
 */
@Entity
@DiscriminatorValue("photo")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PhotoFile extends DataFile {

  public static final EnumSet<FileExtension> photoExtensions = EnumSet.of(
          FileExtension.PNG, FileExtension.JPG
  );

  // Entity Correlation
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "photo_id")
  private Photo photo;

  // Relationship Convenience Method
  public void addPhoto(Photo photo) {
    this.photo = photo;
    photo.setFile(this);
  }

  public void removePhoto() {
    this.photo.setFile(null);
    this.photo = null;
  }

  // Business Logic
  /**
   * 새로운 PhotoFile을 작성합니다.
   * @param fileName 파일 이름
   * @param savedUrl 저장된 주소
   * @param extension 피일 확장자
   * @return 새로 생성된 PhotoFile 객체
   */
  public static PhotoFile create(
          String fileName,
          String savedUrl,
          FileExtension extension
  ) {
    PhotoFile photoFile = new PhotoFile();
    photoFile.setFileName(fileName);
    photoFile.setSavedUrl(savedUrl);
    if (!photoFile.isPhotoExtension(extension)) {
      photoFile.setExtension(extension);
    }

    photoFile.setStatus(DataFileStatus.ACTIVE);
    photoFile.setCreated(LocalDateTime.now());
    photoFile.setUpdated(LocalDateTime.now());
    return photoFile;
  }

  /**
   * DocumentFile 객체를 삭제합니다.
   */
  public void delete() {
    removePhoto();
    setStatus(DataFileStatus.DELETED);
    setUpdated(LocalDateTime.now());
  }

  /**
   * 확장자가 DocumentFile이 허용하는 png, jpg 확장자인지 확인합니다.
   * @param extension 파일 확장자
   * @return 허용하는 확장자이면 true, 아니면 false
   */
  public boolean isPhotoExtension(FileExtension extension) {
    return photoExtensions.contains(extension);
  }
}
