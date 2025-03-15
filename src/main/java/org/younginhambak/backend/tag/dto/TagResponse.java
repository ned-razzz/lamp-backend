package org.younginhambak.backend.tag.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TagResponse {

  @NotNull
  private Long id;

  @NotBlank
  @Size(min = 2, max = 20)
  private String name;
}
