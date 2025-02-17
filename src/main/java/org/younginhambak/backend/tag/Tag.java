package org.younginhambak.backend.tag;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.younginhambak.backend.archive.entity.DocumentTag;
import org.younginhambak.backend.gallery.PhotoTag;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 태그를 관리합니다.
 * Aggregate Root입니다.
 * @version 1.0
 */
@Entity
@Getter
@NoArgsConstructor
public class Tag {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "tag_id")
  private Long id;

  @NotBlank
  @Size(min = 2, max = 20)
  @Column(unique = true)
  private String name;

  // Metadata
  @NotNull
  @Enumerated(EnumType.STRING)
  private TagStatus status;

  @PastOrPresent
  private LocalDateTime created;

  @PastOrPresent
  private LocalDateTime updated;

  // Entity Correlation
  @OneToMany(mappedBy = "tag", fetch = FetchType.LAZY)
  private List<DocumentTag> documentTags = new ArrayList<>();

  @OneToMany(mappedBy = "tag", fetch = FetchType.LAZY)
  private List<PhotoTag> photoTags = new ArrayList<>();

  // Business Logic

  /**
   * 새로운 Tag를 생성합니다.
   * @param name 태그명
   * @return 새로 생성된 Tag 객체
   */
  public static Tag create(String name) {
    Tag tag = new Tag();
    tag.name = name;
    tag.status = TagStatus.ACTIVE;
    tag.created = LocalDateTime.now();
    tag.updated= LocalDateTime.now();
    return tag;
  }

  /**
   * Tag를 삭제합니다.
   * 해당 Tag에 연관관계로 설정되어 있는 DocumentTag, PhotoTag들을 삭제합니다. (DB 작업 필요)
   */
  public void delete() {
    documentTags.forEach(DocumentTag::delete);
    photoTags.forEach(PhotoTag::delete);

    status = TagStatus.DELETED;
    updated = LocalDateTime.now();
  }
}
