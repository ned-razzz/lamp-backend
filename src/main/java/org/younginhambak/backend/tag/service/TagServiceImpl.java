package org.younginhambak.backend.tag.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.younginhambak.backend.tag.Tag;
import org.younginhambak.backend.tag.dto.TagSplitDto;
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
  public List<Tag> getTagsByName(List<String> tagNames) {
    List<Tag> tags = tagRepository.findByNameIn(tagNames);
    Assert.state(tags.size() == tagNames.size(), "There are some tag names that don't exist.");
    return tags;
  }

  @Override
  public List<String> filterExistTagNames(List<String> tagNames) {
    return tagRepository.findExistingNames(tagNames);
  }

  @Override
  public TagSplitDto splitTagNamesByExistence(List<String> tagNames) {
    List<String> existTagNames = tagRepository.findExistingNames(tagNames);
    List<String> nonexistTagNames = tagNames.stream()
            .filter(tagName -> !existTagNames.contains(tagName))
            .toList();
    return TagSplitDto.builder()
            .existingTagNames(existTagNames)
            .nonExistingTagNames(nonexistTagNames)
            .build();
  }
}
