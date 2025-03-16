package org.younginhambak.backend.archive.repository;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.younginhambak.backend.archive.entity.Document;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@Transactional
@Sql("/test-data.sql")
@Slf4j
class DocumentRepositoryImplTest {

  @Autowired DocumentRepository documentRepository;

  @Test
  void searchByCondition() {
    List<Document> documents1 = documentRepository.searchByCondition(
            "공지",
            List.of(),
            null);
    List<Document> documents2 = documentRepository.searchByCondition(
            "공지",
            Arrays.asList(2L, 6L),
            null);
    List<Document> documents3 = documentRepository.searchByCondition(
            "",
            Arrays.asList(2L, 4L, 7L),
            null);

    Assertions.assertThat(documents1.stream().map(Document::getId))
            .contains(1L, 2L);
    Assertions.assertThat(documents2.stream().map(Document::getId))
            .contains(2L);
    Assertions.assertThat(documents3.stream().map(Document::getId))
            .contains(3L);
  }
}