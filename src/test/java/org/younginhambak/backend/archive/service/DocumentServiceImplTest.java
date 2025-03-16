package org.younginhambak.backend.archive.service;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.younginhambak.backend.archive.dto.DocumentCreateRequest;
import org.younginhambak.backend.archive.dto.DocumentUpdateRequest;
import org.younginhambak.backend.archive.entity.Document;
import org.younginhambak.backend.archive.entity.DocumentStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Sql("/test-data.sql")
@Slf4j
class DocumentServiceImplTest {

  @Autowired DocumentService documentService;
  @Autowired EntityManager em;

  @Test
  void readDocument() {
  }

  @Test
  void readDocuments() {
  }

  @Test
  void testReadDocuments() {
  }

  @Test
  void readDocumentsInfo() {
  }

  @Test
  void createDocument() {
    //given
    List<String> tagNames = Arrays.asList("church", "bible");
    DocumentCreateRequest createDto = DocumentCreateRequest.builder()
            .title("document")
            .description("this is document")
            .authorName("hong")
            .tagNames(tagNames)
            .creatorMemberId(1L)
            .fileIds(List.of(8L))
            .build();

    //when
    Long id = documentService.createDocument(createDto);
    log.info("id : {}", id);

    //then
    Document document = documentService.getDocument(id).get();

    assertThat(document.getMember().getId()).isEqualTo(1L);
    assertThat(document.getTitle()).isEqualTo("document");
    assertThat(document.getDescription()).isEqualTo("this is document");
    assertThat(document.getAuthorName()).isEqualTo("hong");
    assertThat(document.getStatus()).isEqualTo(DocumentStatus.ACTIVE);
    assertThat(document.getFileIds()).contains(8L);
    assertThat(document.getTagNames()).containsAll(tagNames);
  }

  @Test
  void updateDocument() {
    //given
    List<String> tagNames = Arrays.asList("tag2", "tag3", "tag12");
    DocumentUpdateRequest updateDto = DocumentUpdateRequest.builder()
            .title("updated document")
            .description("this is updated document")
            .authorName("updated user")
            .tagNames(tagNames)
            .build();

    //when
    Long documentId = 1L;
    documentService.updateDocument(documentId, updateDto);

    //then
    Document document = documentService.getDocument(documentId).get();
    assertThat(document.getTitle()).isEqualTo("updated document");
    assertThat(document.getDescription()).isEqualTo("this is updated document");
    assertThat(document.getAuthorName()).isEqualTo("updated user");
    assertThat(document.getStatus()).isEqualTo(DocumentStatus.ACTIVE);
    assertThat(document.getFileIds()).contains(1L);
    assertThat(document.getTagNames()).containsAll(tagNames);

  }

  @Test
  void deleteDocument() {
    //given
    Long documentId = 2L;

    //when
    documentService.deleteDocument(documentId);
    em.flush();
    em.clear();

    //then
    Optional<Document> document = documentService.getDocument(2L);
    assertThat(document.isPresent()).isFalse();
  }
}