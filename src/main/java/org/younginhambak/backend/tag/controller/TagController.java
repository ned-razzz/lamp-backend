package org.younginhambak.backend.tag.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.younginhambak.backend.tag.dto.TagResponse;
import org.younginhambak.backend.tag.service.TagService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
@Slf4j
public class TagController {

  private final TagService tagService;

  @GetMapping("/documents")
  public List<TagResponse> getTagsInDocuments() {
    log.info("GET /tags/documents");
    return tagService.readTagsRelatedByDocuments();
  }

  @GetMapping("/photos")
  public List<TagResponse> getTagsInPhotos() {
    log.info("GET /tags/photos");
    return tagService.readTagsRelatedByPhotos();
  }
}
