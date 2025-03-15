package org.younginhambak.backend.member.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class SnsAuth {

  @EmbeddedId
  private SnsAuthId id;

  @NotBlank
  private String snsId;

  // Metadata
  @PastOrPresent
  private LocalDateTime created;

  @PastOrPresent
  private LocalDateTime updated;

  // Entity Correlation
  @MapsId("memberId")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;
}
