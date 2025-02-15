package org.younginhambak.backend.role;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Permission {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "permission_id")
  private Long id;

  @NotBlank
  private String domain;

  @NotBlank
  private String identifier;

  // Entity Correlation
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "role_id")
  private Role role;
}
