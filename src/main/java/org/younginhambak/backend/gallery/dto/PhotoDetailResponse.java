package org.younginhambak.backend.gallery.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PhotoDetailResponse {
  @NotNull
  private Long id;

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
  private URL fileUrl;

  @PastOrPresent
  private LocalDateTime created;

  @PastOrPresent
  private LocalDateTime updated;
}
