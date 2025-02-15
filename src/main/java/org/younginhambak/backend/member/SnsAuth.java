package org.younginhambak.backend.member;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class SnsAuth {

//  @Id
//  @GeneratedValue(strategy = GenerationType.IDENTITY)
//  @Column(name = "auth_id")
//  private Long id;

  @EmbeddedId
  private SnsAuthId id;
//
//  @MapsId("snsProvider")
//  @NotBlank
//  private String snsProvider;

  @NotBlank
  private String snsId;

  @MapsId("memberId")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;
}
