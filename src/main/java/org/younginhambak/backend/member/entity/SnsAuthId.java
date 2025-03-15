package org.younginhambak.backend.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class SnsAuthId implements Serializable {

  // Entity Correlation
  @Column(name = "member_id")
  private Long memberId;

  @Column(name = "sns_provider")
  private String snsProvider;
}
