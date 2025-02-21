package org.younginhambak.backend.tag.service;

import org.younginhambak.backend.tag.Tag;

import java.util.List;
import java.util.Optional;

public interface TagService {

  Optional<Tag> getTagByName(String tagName);

  List<String> filterExistTagNames(List<String> tagNames);
}
