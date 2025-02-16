package org.younginhambak.backend.file;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.younginhambak.backend.archive.Document;

import java.time.LocalDateTime;
import java.util.EnumSet;

/**
 * DataFile을 상속한 객체입니다.
 * Document에 속한 실제 자료 데이터들의 저장 위치와 메타데이터들을 관리합니다.
 * @version 1.0
 */
@Entity
@DiscriminatorValue("document")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DocumentFile extends DataFile {

  public static final EnumSet<FileExtension> documentExtensions = EnumSet.of(
          FileExtension.DOC, FileExtension.HWP, FileExtension.PDF,
          FileExtension.PPT, FileExtension.TXT
  );

  // Entity Correlation
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "document_id")
  private Document document;

  // Relationship Convenience Method
  public void addDocument(Document document) {
    this.document = document;
    document.getFiles().add(this);
  }

  private void removeDocument() {
    document.getFiles().remove(this);
    document = null;
  }

  // Business Logic
  /**
   * 새로운 DoucmentFile을 작성합니다.
   * @param fileName 파일 이름
   * @param savedUrl 저장된 주소
   * @param extension 피일 확장자
   * @return 새로 생성된 DocumentFile 객체
   */
  public static DocumentFile create(
          String fileName,
          String savedUrl,
          FileExtension extension
  ) {
    DocumentFile documentFile = new DocumentFile();
    documentFile.setFileName(fileName);
    documentFile.setSavedUrl(savedUrl);
    if (documentFile.isDocumentExtension(extension)) {
      documentFile.setExtension(extension);
    }

    documentFile.setStatus(DataFileStatus.ACTIVE);
    documentFile.setCreated(LocalDateTime.now());
    documentFile.setUpdated(LocalDateTime.now());
    return documentFile;
  }

  /**
   * DocumentFile 객체를 삭제합니다.
   */
  public void delete() {
    removeDocument();
    setStatus(DataFileStatus.DELETED);
    setUpdated(LocalDateTime.now());
  }

  /**
   * 확장자가 DocumentFile이 허용하는 doc, hwp, pdf, ppt, txt 확장자인지 확인합니다.
   * @param extension 파일 확장자
   * @return 허용하는 확장자이면 true, 아니면 false
   */
  public boolean isDocumentExtension(FileExtension extension) {
    return documentExtensions.contains(extension);
  }
}
