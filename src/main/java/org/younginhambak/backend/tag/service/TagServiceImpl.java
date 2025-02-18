package org.younginhambak.backend.tag.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.younginhambak.backend.tag.Tag;
import org.younginhambak.backend.tag.repository.TagRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

  private final TagRepository tagRepository;

  @Override
  public Optional<Tag> getTagByName(String tagName) {
    return tagRepository.findByName(tagName);
  }

  @Override
  public Boolean existTag(String tagName) {
    List<Tag> tags = tagRepository.findAll();
    return tags.stream()
            .anyMatch(tag -> tag.isSameName(tagName));
  }
}
