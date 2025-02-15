package org.younginhambak.backend.gallery;

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
public class Photo {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "photo_id")
  private Long id;

  @NotBlank
  @Size(min = 3, max = 30)
  private String title;

  @Size(max = 300)
  private String description;

  @NotBlank
  @Size(max = 30)
  private String photographer;

  private LocalDateTime takenAt;

  @NotNull
  @Enumerated(EnumType.STRING)
  private PhotoStatus status;

  @PastOrPresent
  private LocalDateTime created;

  @PastOrPresent
  private LocalDateTime updated;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @OneToMany(mappedBy = "photo", fetch = FetchType.LAZY)
  private List<PhotoTag> photoTags = new ArrayList<>();

  @OneToOne(mappedBy = "photo", fetch = FetchType.LAZY)
  private PhotoFile file;
}
