package org.younginhambak.backend.member;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class SnsAuth {

  @EmbeddedId
  private SnsAuthId id;

  @NotBlank
  private String snsId;

  @MapsId("memberId")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;
}
