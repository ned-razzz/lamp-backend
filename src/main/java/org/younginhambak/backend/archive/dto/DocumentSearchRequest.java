package org.younginhambak.backend.archive.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentSearchRequest {
  @NotNull
  @Size(min = 3, max = 100)
  @Builder.Default
  private String title = "";

  @NotNull
  @Builder.Default
  private List<Long> tags = List.of();

  @NotNull
  @Builder.Default
  private String sort = "created";
}
