package org.younginhambak.backend.tag.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TagSplitDto {
  private List<String> existingTagNames;
  private List<String> nonExistingTagNames;
}

