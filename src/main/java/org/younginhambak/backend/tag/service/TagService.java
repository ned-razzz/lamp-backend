package org.younginhambak.backend.tag.service;

import org.younginhambak.backend.tag.Tag;
import org.younginhambak.backend.tag.dto.TagSplitDto;

import java.util.List;
import java.util.Optional;

public interface TagService {

  Optional<Tag> getTagByName(String tagName);
  List<Tag> getTagsByName(List<String> tagNames);
  List<String> filterExistTagNames(List<String> tagNames);
  TagSplitDto splitTagNamesByExistence(List<String> tagNames);
}
