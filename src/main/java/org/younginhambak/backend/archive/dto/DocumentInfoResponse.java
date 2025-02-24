package org.younginhambak.backend.archive.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class DocumentInfoResponse {
  @NotNull
  private Long id;

  @NotBlank
  @Size(min = 3, max = 100)
  private String title;

  @Size(min = 3, max = 30)
  private String authorName;

  private List<String> tags;

  @PastOrPresent
  private LocalDateTime created;

  @PastOrPresent
  private LocalDateTime updated;
}
