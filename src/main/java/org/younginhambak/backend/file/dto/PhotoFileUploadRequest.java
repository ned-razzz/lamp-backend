package org.younginhambak.backend.file.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import org.younginhambak.backend.file.entity.FileExtension;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhotoFileUploadRequest {
  @NotNull
  private MultipartFile file;

  @NotBlank
  private String fileName;

  @NotNull
  private FileExtension extension;
}
