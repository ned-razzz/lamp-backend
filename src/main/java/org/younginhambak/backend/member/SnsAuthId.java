package org.younginhambak.backend.member;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class SnsAuthId implements Serializable {

  @Column(name = "member_id")
  private Long memberId;

  @Column(name = "sns_provider")
  private String snsProvider;
}
