package org.younginhambak.backend.tag.repository;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.younginhambak.backend.archive.dto.DocumentCreateRequest;
import org.younginhambak.backend.archive.service.DocumentService;
import org.younginhambak.backend.member.entity.Member;
import org.younginhambak.backend.member.repository.MemberRepository;
import org.younginhambak.backend.tag.Tag;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@Transactional
@Sql("/test-data.sql")
@Slf4j
class TagRepositoryImplTest {

  @Autowired
  MemberRepository memberRepository;
  @Autowired
  DocumentService documentService;
  @Autowired
  TagRepository tagRepository;
  @Autowired
  EntityManager em;

  @Test
  void findTagsByRelatedDocumentsExists() {
    List<Tag> tags = tagRepository.findTagsByRelatedDocumentsExists();
    List<String> tagNames = tags.stream()
            .map(Tag::getName)
            .toList();
    Assertions.assertThat(tagNames).containsAll(
            Arrays.asList(
                    "tag1",
                    "tag2",
                    "tag4",
                    "tag5",
                    "tag6",
                    "tag7"
            ));
  }

  @Test
  void findTagsByRelatedPhotosExists() {
    List<Tag> tags = tagRepository.findTagsByRelatedPhotosExists();
    List<String> tagNames = tags.stream()
            .map(Tag::getName)
            .toList();
    Assertions.assertThat(tagNames).containsAll(
            Arrays.asList(
                    "tag3",
                    "tag5",
                    "tag7",
                    "tag8",
                    "tag6",
                    "tag2"
            ));
  }
}