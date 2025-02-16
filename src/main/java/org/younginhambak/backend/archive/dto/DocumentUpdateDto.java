package org.younginhambak.backend.archive.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class DocumentUpdateDto {

  @NotBlank
  @Size(min = 3, max = 100)
  private String title;

  @Size(max = 500)
  private String description;

  @Size(min = 3, max = 30)
  private String author;

  private List<String> tagNames;
}
