package org.younginhambak.backend.archive;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.younginhambak.backend.file.DocumentFile;
import org.younginhambak.backend.member.Member;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 자료실에 저장되는 문서 자료를 관리합니다.
 * 문서는 여러 개의 자료를 가질 수 있고 여러 개의 태그를 가질 수 있습니다.
 * Aggregate Root입니다.
 * @version 1.0
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Document {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "document_id")
  private Long id;

  @NotBlank
  @Size(min = 3, max = 100)
  private String title;

  @Size(max = 500)
  private String description;

  @Size(min = 3, max = 30)
  private String author;

  // Metadata
  @NotNull
  @Enumerated(EnumType.STRING)
  private DocumentStatus status;

  @PastOrPresent
  private LocalDateTime created;

  @PastOrPresent
  private LocalDateTime updated;

  // Entity Correlation
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @OneToMany(mappedBy = "document", fetch = FetchType.LAZY)
  private List<DocumentFile> files = new ArrayList<>();

  @OneToMany(mappedBy = "document", fetch = FetchType.LAZY)
  private List<DocumentTag> documentTags = new ArrayList<>();

  // Relationship Convenience Method
  public void addMember(Member member) {
    this.member = member;
    member.getDocuments().add(this);
  }

  public void removeMember() {
    this.member.getDocuments().remove(this);
    this.member = null;
  }

  public void updateMember(Member member) {
    if (member.equals(this.member)) {
      return;
    }
    removeMember();
    addMember(member);
  }

  // Business Logic

  /**
   * 새로운 Document 객체를 생성합니다.
   * @param title 문서 제목
   * @param description 문서 설명
   * @param author 문서 작성자
   * @param member 문서 소유자
   * @param files 문서 자료 파일들
   * @param documentTags 문서 태그들
   * @return 새로 생성된 Document 객체
   */
  public static Document create(
          String title,
          String description,
          String author,
          Member member,
          List<DocumentFile> files,
          List<DocumentTag> documentTags) {
    Document document = new Document();
    document.title = title;
    document.description = description;
    document.author = author;
    document.status = DocumentStatus.ACTIVE;
    document.created = LocalDateTime.now();
    document.updated = LocalDateTime.now();

    document.addMember(member);
    files.forEach(file -> {
      file.addDocument(document);
    });
    documentTags.forEach(documentTag -> {
      documentTag.addDocument(document);
    });

    return document;
  }

  /**
   * 이미 존재하는 Document 객체의 값을 수정합니다.
   * Document 객체에서 연관관계가 끊긴 DocumentFile, DocumentTag 객체들은 삭제 처리됩니다. (DB 작업은 따로 필요합니다.)
   * @param title 문서 제목
   * @param description 문서 설명
   * @param author 문서 작성자
   * @param member 문서 소유자
   * @param files 문서 자료 파일들
   * @param documentTags 문서 태그들
   */
  public void update(
          String title,
          String description,
          String author,
          Member member,
          List<DocumentFile> files,
          List<DocumentTag> documentTags) {
    this.title = title;
    this.description = description;
    this.author = author;
    updateMember(member);
    updateFiles(files);
    updateDocumentTags(documentTags);

    updated = LocalDateTime.now();
  }

  /**
   * Document 객체를 삭제합니다.
   * 해당 Document 객체와 연관관계가 설정된 DocumentFile, DocumentTag 객체들은 삭제 처리됩니다. (DB 작업은 따로 필요합니다.)
   */
  public void delete() {
    removeMember();
    new ArrayList<>(files).forEach(DocumentFile::delete);
    new ArrayList<>(documentTags).forEach(DocumentTag::delete);

    status = DocumentStatus.DELETED;
    updated = LocalDateTime.now();
  }

  private void updateFiles(List<DocumentFile> files) {
    // 기존 파일들 중 변경 파라미터에 없는 파일들은 삭제
    List<DocumentFile> list = this.files.stream()
            .filter(file -> !files.contains(file))
            .toList();
    list.forEach(DocumentFile::delete);

    // 이미 존재하는 파일들을 제외하고 새 파일들 추가
    files.stream()
            .filter(file -> !this.files.contains(file))
            .forEach(file -> file.addDocument(this));
  }

  private void updateDocumentTags(List<DocumentTag> documentTags) {
    // 기존 문서태그들 중 변경 파라미터에 없는 건 삭제
    List<DocumentTag> list = this.documentTags.stream()
            .filter(documentTag -> !documentTags.contains(documentTag))
            .toList();
    list.forEach(DocumentTag::delete);

    // 이미 존재하는 문서태그들을 제외하고 새 문서태그 추가
    documentTags.stream()
            .filter(documentTag -> !this.documentTags.contains(documentTag))
            .forEach(documentTag -> documentTag.addDocument(this));
  }
}
