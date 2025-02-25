package org.younginhambak.backend.file.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

/**
 * S3에 저장되는 데이터들을 관리합니다.
 */
@Entity
@Table(name = "file")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@SQLRestriction("status = 'ACTIVE'")
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor
public abstract class DataFile {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "file_id")
  private Long id;

  @NotBlank
  private String fileKey;

  @NotBlank
  private String fileName;

  @NotNull
  @Enumerated(EnumType.STRING)
  private FileExtension extension;

  // Metadata
  @NotNull
  @Enumerated(EnumType.STRING)
  private DataFileStatus status;

  @PastOrPresent
  private LocalDateTime created;

  @PastOrPresent
  private LocalDateTime updated;
}
