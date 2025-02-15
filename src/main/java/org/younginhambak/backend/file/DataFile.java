package org.younginhambak.backend.file;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "dtype")
@Getter
@NoArgsConstructor
public abstract class DataFile {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "file_id")
  private Long id;

  @NotBlank
  private String savedUrl;

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
