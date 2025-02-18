package org.younginhambak.backend.archive.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DocumentUpdateDto {

  @NotBlank
  @Size(min = 3, max = 100)
  private String title;

  @Size(max = 500)
  private String description;

  @Size(min = 3, max = 30)
  private String authorName;

  private List<String> tagNames;
}
