package org.younginhambak.backend.gallery.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PhotoCreateRequest {
  @NotNull
  private Long creatorMemberId;

  @NotBlank
  @Size(min = 3, max = 30)
  private String title;

  @Size(max = 300)
  private String description;

  @Size(max = 30)
  private String photographer;

  @Past
  private LocalDateTime takenAt;

  @NotNull
  private List<String> tagNames;

  @NotNull
  private Long fileId;
}
