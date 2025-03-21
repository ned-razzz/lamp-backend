package org.younginhambak.backend.gallery.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PhotoCreateBatchRequest {
  @NotNull
  private Long creatorMemberId;

  @NotNull
  @Size(min = 1)
  private List<Long> fileIds;

  @NotNull
  private List<String> commonTagNames;

  @NotNull
  @Size(min = 1)
  private List<PhotoData> photos;

  @AssertTrue
  public boolean validateSameSize() {
    return fileIds.size() == photos.size();
  }

  @Data
  public static class PhotoData {

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
    private Long fileId;
  }
}

