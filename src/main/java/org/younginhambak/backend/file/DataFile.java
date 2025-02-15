package org.younginhambak.backend.file;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

  @NotNull
  @Enumerated(EnumType.STRING)
  private DataFileStatus status;
}
