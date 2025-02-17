package org.younginhambak.backend.archive.repository;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.younginhambak.backend.archive.entity.Document;
import org.younginhambak.backend.archive.entity.DocumentStatus;
import org.younginhambak.backend.archive.entity.DocumentTag;
import org.younginhambak.backend.file.DocumentFile;
import org.younginhambak.backend.file.FileExtension;
import org.younginhambak.backend.member.Member;
import org.younginhambak.backend.tag.Tag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class DocumentRepositoryImplTest {

  @Autowired DocumentRepository documentRepository;
  @Autowired EntityManager em;

  @Test
  void save() {
    //given
    Member member = Member.create("hong", "member@gmail.com", "nickname1");
    List<DocumentFile> files = createFiles();
    List<Tag> tags = createTags();
    List<DocumentTag> documentTags = createDocumentTags(tags);
    Document document = Document.create(
            "title1",
            "description1",
            "author1",
            member,
            files,
            documentTags
    );

    //when
    documentRepository.save(document);

    //then
    Document findDocument = documentRepository.findById(1L).get();
    assertThat(findDocument).isEqualTo(document);
    assertThat(findDocument.getDocumentTags().get(1).getId().getDocumentId())
            .isEqualTo(1L);
    assertThat(findDocument.getDocumentTags().get(1).getId().getTagId())
            .isEqualTo(documentTags.get(1).getTag().getId());
  }

  @Test
  void findById() {
    //given
    Member member = Member.create("hong", "member@gmail.com", "nickname1");
    List<DocumentFile> files = createFiles();
    List<Tag> tags = createTags();
    List<DocumentTag> documentTags = createDocumentTags(tags);
    Document document = Document.create(
            "title1",
            "description1",
            "author1",
            member,
            files,
            documentTags
    );

    em.persist(member);
    documentRepository.save(document);

    //when
    Document findDocument = documentRepository.findById(1L).get();

    //then
    assertThat(findDocument.getStatus()).isEqualTo(DocumentStatus.ACTIVE);
    assertThat(findDocument.getTitle()).isEqualTo(document.getTitle());
    assertThat(findDocument.getDescription()).isEqualTo(document.getDescription());
    assertThat(findDocument.getAuthor()).isEqualTo(document.getAuthor());
    assertThat(findDocument.getMember()).isEqualTo(member);
    assertThat(findDocument.getDocumentTags()).containsAll(documentTags);
    assertThat(findDocument.getFiles()).containsAll(files);
  }

  @Test
  void findByTitle() {
    //given
    Member member = Member.create("hong", "member@gmail.com", "nickname1");
    List<DocumentFile> files = createFiles();
    List<Tag> tags = createTags();
    List<DocumentTag> documentTags = createDocumentTags(tags);
    Document document = Document.create(
            "title1",
            "description1",
            "author1",
            member,
            files,
            documentTags
    );

    em.persist(member);
    documentRepository.save(document);

    //when
    List<Document> documents1 = documentRepository.findByTitle("title1");
    List<Document> documents2 = documentRepository.findByTitle("titl");
    List<Document> documents3 = documentRepository.findByTitle("le1");

    //then
    assertThat(documents1).contains(document);
    assertThat(documents2).contains(document);
    assertThat(documents3).contains(document);
  }

  @Test
  void findAll() {
    //given
    Member member = Member.create("hong", "member@gmail.com", "nickname1");
    List<DocumentFile> files = createFiles();
    List<Tag> tags = createTags();
    List<DocumentTag> documentTags = createDocumentTags(tags);
    Document docment1 = Document.create(
            "title1",
            "description1",
            "author1",
            member,
            files,
            documentTags
    );

    List<DocumentFile> files2 = createFiles();
    Tag tag1 = Tag.create("tag4");
    Tag tag2 = Tag.create("tag5");
    Tag tag3 = Tag.create("tag6");
    List<DocumentTag> documentTags2 = Stream.of(tag1, tag2, tag3)
            .map(DocumentTag::create)
            .collect(Collectors.toList());

    Document docment2 = Document.create(
            "title2",
            "description2",
            "author2",
            member,
            files2,
            documentTags2
    );

    em.persist(member);
    documentRepository.save(docment1);
    documentRepository.save(docment2);

    //when
    List<Document> list = documentRepository.findAll();

    //then
    assertThat(list).contains(docment1, docment2);
  }

  @Test
  @Rollback(false)
  public void delete() throws Exception {
    //given
    Member member = Member.create("hong", "member@gmail.com", "nickname1");
    List<DocumentFile> files = createFiles();
    List<Tag> tags = createTags();
    List<DocumentTag> documentTags = createDocumentTags(tags);
    Document docment = Document.create(
            "title1",
            "description1",
            "author1",
            member,
            files,
            documentTags
    );
    em.persist(member);
    documentRepository.save(docment);

    //when
    Document findDocument = documentRepository.findById(1L).get();
    findDocument.delete();

    //then
    assertThat(findDocument.getStatus()).isEqualTo(DocumentStatus.DELETED);
    assertThat(findDocument.getDocumentTags().size()).isEqualTo(0);
    assertThat(findDocument.getFiles().size()).isEqualTo(0);
    assertThat(files.get(0).getDocument()).isNull();
    assertThat(documentTags.get(0).getDocument()).isNull();
  }

  private List<Tag> createTags() {
    Tag tag1 = Tag.create("tag1");
    Tag tag2 = Tag.create("tag2");
    Tag tag3 = Tag.create("tag3");
    return List.of(tag1, tag2, tag3);
  }

  private List<DocumentTag> createDocumentTags(List<Tag> tags) {
    return tags.stream()
            .map(DocumentTag::create)
            .collect(Collectors.toList());
  }

  private List<DocumentFile> createFiles() {
    DocumentFile file1 = DocumentFile.create("file1", "url1", FileExtension.PDF);
    DocumentFile file2 = DocumentFile.create("file2", "url2", FileExtension.DOC);
    DocumentFile file3 = DocumentFile.create("file3", "url3", FileExtension.PPT);
    return List.of(file1, file2, file3);
  }
}