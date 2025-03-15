package org.younginhambak.backend.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberCreateDto {
  @NotBlank
  @Size(max = 5)
  private String name;

  @NotBlank
  @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9]+[.][A-Za-z]{2,}$")
  private String email;

  @NotBlank
  @Size(min = 3, max = 15)
  private String nickname;
}
