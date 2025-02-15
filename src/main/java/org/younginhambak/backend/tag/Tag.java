package org.younginhambak.backend.tag;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.younginhambak.backend.archive.DocumentTag;
import org.younginhambak.backend.gallery.PhotoTag;

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

  @Column(unique = true)
  private String name;

  @NotNull
  @Enumerated(EnumType.STRING)
  private TagStatus tag;

  @OneToMany(mappedBy = "tag", fetch = FetchType.LAZY)
  private List<DocumentTag> documentTags = new ArrayList<>();

  @OneToMany(mappedBy = "tag", fetch = FetchType.LAZY)
  private List<PhotoTag> photoTags = new ArrayList<>();
}
