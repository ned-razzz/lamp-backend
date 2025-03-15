package org.younginhambak.backend.member.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import org.younginhambak.backend.archive.entity.Document;
import org.younginhambak.backend.gallery.entity.Photo;
import org.younginhambak.backend.role.Role;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 웹사이트 유저를 관리합니다.
 * Aggregate Root입니다.
 * @version 1.0
 */
@Entity
@SQLRestriction("status = 'ACTIVE'")
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
  @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9]+[.][A-Za-z]{2,}$")
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

  // Business Logic
  /**
   * 새로운 Member를 생성합니다.
   * @param name 유저 실명
   * @param email 이메일
   * @param nickname 닉네임
   * @return 새로 생성된 Member 객체
   */
  public static Member create(
          String name,
          String email,
          String nickname
  ) {
    Member member = new Member();
    member.name = name;
    member.email = email;
    member.nickname = nickname;
    member.status = MemberStatus.ACTIVE;
    member.created = LocalDateTime.now();
    member.updated = LocalDateTime.now();

    return member;
  }
}
