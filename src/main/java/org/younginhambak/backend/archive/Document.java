package org.younginhambak.backend.archive;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.younginhambak.backend.member.Member;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
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

  @NotBlank
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
}
