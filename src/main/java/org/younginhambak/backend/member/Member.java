package org.younginhambak.backend.member;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.younginhambak.backend.archive.Document;
import org.younginhambak.backend.gallery.Photo;
import org.younginhambak.backend.role.Role;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Member {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "member_id")
  private Long id;

  @NotBlank
  @Size(max = 5)
  private String name;

  @NotBlank
  @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9][.][A-Za-z]{2,}$")
  private String email;

  @NotBlank
  @Size(min = 3, max = 15)
  private String nickname;

  // Metadata
  @NotNull
  @Enumerated(EnumType.STRING)
  private MemberStatus status;

  @PastOrPresent
  private LocalDateTime created;

  @PastOrPresent
  private LocalDateTime updated;

  // Entity Correlation
  @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
  private List<SnsAuth> snsAuths = new ArrayList<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "role_id")
  private Role role;

  @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
  private List<Document> documents = new ArrayList<>();

  @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
  private List<Photo> photos = new ArrayList<>();
}
