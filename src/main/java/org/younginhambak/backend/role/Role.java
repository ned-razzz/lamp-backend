package org.younginhambak.backend.role;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.younginhambak.backend.member.Member;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "role_id")
  private Long id;

  @NotBlank
  private String name;

  @NotBlank
  @Size(max = 100)
  private String description;

  @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
  private List<Member> members = new ArrayList<>();

  @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
  private List<Permission> permissions = new ArrayList<>();
}
