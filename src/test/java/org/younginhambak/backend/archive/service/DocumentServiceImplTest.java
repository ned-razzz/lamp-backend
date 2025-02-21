package org.younginhambak.backend.archive.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.younginhambak.backend.archive.dto.DocumentCreateDto;
import org.younginhambak.backend.archive.dto.DocumentUpdateDto;
import org.younginhambak.backend.archive.entity.Document;
import org.younginhambak.backend.member.Member;
import org.younginhambak.backend.member.repository.MemberRepository;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class DocumentServiceImplTest {

  @Autowired
  DocumentService documentService;
  @Autowired
  MemberRepository memberRepository;
  @Autowired
  EntityManager em;

  @Test
  void getDocument() {

  }

  @Test
  void getDocumentAll() {
  }

  @Test
  @Rollback(false)
  void createDocument() {
    //given
    Member member = Member.create("song", "user@naver.com", "user1");
    memberRepository.save(member);
    List<String> tagNames = Arrays.asList("church", "bible");
    DocumentCreateDto createDto = DocumentCreateDto.builder()
            .title("document")
            .description("this is document")
            .authorName("hong")
            .tagNames(tagNames)
            .creatorMemberId(1L)
            .build();

    //when
    documentService.createDocument(createDto);

    //then
    List<Document> documentAll = documentService.getDocumentAll();
    Document document = documentService.getDocument(1L).get();
    assertThat(document.getMember()).isEqualTo(member);
    assertThat(document.getTagNames()).containsAll(tagNames);
    assertThat(documentAll).isNotNull();
  }

  @Test
  @Rollback(false)
  public void createDocumentMany() throws Exception {
    //given
    Member member = Member.create("song", "user@naver.com", "user1");
    memberRepository.save(member);

    DocumentCreateDto createDto1 = DocumentCreateDto.builder()
            .title("document")
            .description("this is document")
            .authorName("hong")
            .tagNames(Arrays.asList("tag1", "tag2", "tag3", "tag4", "tag5"))
            .creatorMemberId(1L)
            .build();
    DocumentCreateDto createDto2 = DocumentCreateDto.builder()
            .title("document2")
            .description("this is document2")
            .authorName("song")
            .tagNames(Arrays.asList("tag1", "tag2", "tag3"))
            .creatorMemberId(1L)
            .build();
    DocumentCreateDto createDto3 = DocumentCreateDto.builder()
            .title("document3")
            .description("this is document3")
            .authorName("song")
            .tagNames(Arrays.asList("tag2", "tag3", "tag6"))
            .creatorMemberId(1L)
            .build();
    DocumentCreateDto createDto4 = DocumentCreateDto.builder()
            .title("document4")
            .description("this is document4")
            .authorName("song")
            .tagNames(Arrays.asList("tag7", "tag8"))
            .creatorMemberId(1L)
            .build();

    //when
    documentService.createDocument(createDto1);
    documentService.createDocument(createDto2);
    documentService.createDocument(createDto3);
    documentService.createDocument(createDto4);
  }

  @Test
  @Rollback(false)
  void updateDocument() {
    //given
    Member member = Member.create("song", "user@naver.com", "user1");
    memberRepository.save(member);
    List<String> tagNames = Arrays.asList("church", "bible");
    List<String> tagNamesUpdated = Arrays.asList("church", "song", "map");

    DocumentCreateDto createDto = DocumentCreateDto.builder()
            .title("document")
            .description("this is document")
            .authorName("hong")
            .tagNames(tagNames)
            .creatorMemberId(1L)
            .build();
    documentService.createDocument(createDto);

    DocumentUpdateDto updateDto = DocumentUpdateDto.builder()
            .title("updated document")
            .description("this is document")
            .authorName("song")
            .tagNames(tagNamesUpdated)
            .build();

    //when
    documentService.updateDocument(1L, updateDto);

    //then
    Document document = documentService.getDocument(1L).get();

    assertThat(document.getTagNames()).containsAll(tagNamesUpdated);
  }

  @Test
  @Rollback(false)
  public void updateDocumentMany() throws Exception {
    //given
    Member member = Member.create("song", "user@naver.com", "user1");
    memberRepository.save(member);

    DocumentCreateDto createDto1 = DocumentCreateDto.builder()
            .title("document")
            .description("this is document")
            .authorName("hong")
            .tagNames(Arrays.asList("tag1", "tag2", "tag3", "tag4", "tag5"))
            .creatorMemberId(1L)
            .build();
    DocumentCreateDto createDto2 = DocumentCreateDto.builder()
            .title("document2")
            .description("this is document2")
            .authorName("song")
            .tagNames(Arrays.asList("tag1", "tag2", "tag3"))
            .creatorMemberId(1L)
            .build();
    DocumentCreateDto createDto3 = DocumentCreateDto.builder()
            .title("document3")
            .description("this is document3")
            .authorName("song")
            .tagNames(Arrays.asList("tag2", "tag3", "tag6"))
            .creatorMemberId(1L)
            .build();
    DocumentCreateDto createDto4 = DocumentCreateDto.builder()
            .title("document4")
            .description("this is document4")
            .authorName("song")
            .tagNames(Arrays.asList("tag7", "tag8"))
            .creatorMemberId(1L)
            .build();
    documentService.createDocument(createDto1);
    documentService.createDocument(createDto2);
    documentService.createDocument(createDto3);
    documentService.createDocument(createDto4);

    DocumentUpdateDto updateDto1 = DocumentUpdateDto.builder()
            .title("first updated document")
            .description("this is first updated document")
            .authorName("ssong")
            .tagNames(Arrays.asList("tag1", "tag2", "tag5", "tag6", "tag9"))
            .build();
    DocumentUpdateDto updateDto2 = DocumentUpdateDto.builder()
            .title("first updated document")
            .description("this is first updated document")
            .authorName("ssong")
            .tagNames(Arrays.asList("tag10"))
            .build();
    DocumentUpdateDto updateDto4 = DocumentUpdateDto.builder()
            .title("second updated document")
            .description("this is second updated document")
            .authorName("hacker")
            .tagNames(Arrays.asList("tag1", "tag2", "tag11"))
            .build();

    //when
    documentService.updateDocument(1L, updateDto1);
    em.flush();
    documentService.updateDocument(1L, updateDto2);
    em.flush();
    documentService.updateDocument(4L, updateDto4);

    //then

  }

  @Test
  @Rollback(value = false)
  void deleteDocument() {
    //given
    Member member = Member.create("song", "user@naver.com", "user1");
    memberRepository.save(member);

    DocumentCreateDto createDto1 = DocumentCreateDto.builder()
            .title("document")
            .description("this is document")
            .authorName("hong")
            .tagNames(Arrays.asList("tag1", "tag2", "tag3", "tag4", "tag5"))
            .creatorMemberId(1L)
            .build();
    DocumentCreateDto createDto2 = DocumentCreateDto.builder()
            .title("document2")
            .description("this is document2")
            .authorName("song")
            .tagNames(Arrays.asList("tag1", "tag2", "tag3"))
            .creatorMemberId(1L)
            .build();
    DocumentCreateDto createDto3 = DocumentCreateDto.builder()
            .title("document3")
            .description("this is document3")
            .authorName("song")
            .tagNames(Arrays.asList("tag2", "tag3", "tag6"))
            .creatorMemberId(1L)
            .build();
    DocumentCreateDto createDto4 = DocumentCreateDto.builder()
            .title("document4")
            .description("this is document4")
            .authorName("song")
            .tagNames(Arrays.asList("tag7", "tag8"))
            .creatorMemberId(1L)
            .build();
    documentService.createDocument(createDto1);
    documentService.createDocument(createDto2);
    documentService.createDocument(createDto3);
    documentService.createDocument(createDto4);


    //when
    List<Document> documentAll = documentService.getDocumentAll();
    documentService.deleteDocument(documentAll.get(0).getId());
    documentService.deleteDocument(documentAll.get(1).getId());
  }
}