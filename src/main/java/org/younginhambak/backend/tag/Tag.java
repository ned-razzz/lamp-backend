package org.younginhambak.backend.tag;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.younginhambak.backend.archive.DocumentTag;
import org.younginhambak.backend.gallery.PhotoTag;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

  @NotNull
  @Enumerated(EnumType.STRING)
  private TagStatus tag;

  @PastOrPresent
  private LocalDateTime created;

  @PastOrPresent
  private LocalDateTime updated;

  @OneToMany(mappedBy = "tag", fetch = FetchType.LAZY)
  private List<DocumentTag> documentTags = new ArrayList<>();

  @OneToMany(mappedBy = "tag", fetch = FetchType.LAZY)
  private List<PhotoTag> photoTags = new ArrayList<>();
}
