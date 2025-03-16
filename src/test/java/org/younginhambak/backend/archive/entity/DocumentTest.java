package org.younginhambak.backend.archive.entity;

import org.junit.jupiter.api.Test;
import org.younginhambak.backend.file.entity.DocumentFile;
import org.younginhambak.backend.file.entity.FileExtension;
import org.younginhambak.backend.member.entity.Member;
import org.younginhambak.backend.tag.Tag;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;


class DocumentTest {

  @Test
  public void 자료저장() throws Exception {
    //given
    List<DocumentFile> documentFiles = createDocumentFiles();
    ArrayList<DocumentTag> documentTags = createDocumentTags();

    //when
    Document document = createDocument(documentFiles, documentTags);

    //then
    assertThat(document.getMember().getName()).isEqualTo("member1");
    assertThat(document.getFiles()).containsAll(documentFiles);
    assertThat(document.getDocumentTags()).containsAll(documentTags);
  }

  @Test
  public void 자료수정() throws Exception {
    //given
    List<DocumentFile> documentFiles = createDocumentFiles();
    ArrayList<DocumentTag> documentTags = createDocumentTags();
    Document document = createDocument(documentFiles, documentTags);

    //when
    DocumentFile fileU = DocumentFile.create("fileu", "urlu", FileExtension.TXT);
    ArrayList<DocumentFile> filesU = new ArrayList<>();
    filesU.add(documentFiles.get(1));
    filesU.add(fileU);

    Tag tagU = Tag.create("tagU");
    Tag tagU2 = Tag.create("tagU2");
    DocumentTag documentTagU = DocumentTag.create(tagU);
    DocumentTag documentTagU2 = DocumentTag.create(tagU2);
    ArrayList<DocumentTag> documentTagsU = new ArrayList<>();
    documentTagsU.add(documentTags.get(0));
    documentTagsU.add(documentTagU);
    documentTagsU.add(documentTagU2);

    document.update("titleu", "descriptionu", "authoru",
            filesU,
            documentTagsU
    );

    //then
    assertThat(document.getFiles()).hasSize(2);
    assertThat(document.getFiles()).containsAll(filesU);
    assertThat(document.getFiles()).doesNotContain(documentFiles.get(0));

    assertThat(document.getDocumentTags()).hasSize(3);
    assertThat(document.getDocumentTags()).containsAll(documentTagsU);
    assertThat(document.getDocumentTags()).doesNotContain(documentTags.get(1));

    assertThat(documentFiles.get(0).getDocument()).isNull();
    assertThat(documentTags.get(1).getDocument()).isNull();
  }

  @Test
  public void 자료삭제() throws Exception {
    //given
    List<DocumentFile> documentFiles = createDocumentFiles();
    ArrayList<DocumentTag> documentTags = createDocumentTags();
    Document document = createDocument(documentFiles, documentTags);

    //when
    document.delete();

    //then
    assertThat(document.getStatus()).isEqualTo(DocumentStatus.DELETED);
    assertThat(document.getFiles().size()).isEqualTo(2);
    assertThat(document.getDocumentTags().size()).isEqualTo(0);
    documentFiles.forEach(documentFile -> {
      assertThat(documentFile.getDocument()).isNotNull();
    });
    documentTags.forEach(documentTag -> {
      assertThat(documentTag.getDocument()).isNull();
      assertThat(documentTag.getTag()).isNull();
    });


  }

  public Document createDocument(List<DocumentFile> files, List<DocumentTag> documentTags) {
    Member member1 = Member.create("member1", "member@gmail.com", "nickname1");

    return Document.create(
            "title1",
            "description1",
            "author1",
            member1,
            files,
            documentTags
    );
  }

  private static ArrayList<DocumentTag> createDocumentTags() {
    Tag tag1 = Tag.create("tag1");
    Tag tag2 = Tag.create("tag2");
    DocumentTag documentTag1 = DocumentTag.create(tag1);
    DocumentTag documentTag2 = DocumentTag.create(tag2);
    ArrayList<DocumentTag> documentTags = new ArrayList<>();
    documentTags.add(documentTag1);
    documentTags.add(documentTag2);
    return documentTags;
  }

  public List<DocumentFile> createDocumentFiles() {
    DocumentFile file1 = DocumentFile.create("file1", "url1", FileExtension.PDF);
    DocumentFile file2 = DocumentFile.create("file2", "url2", FileExtension.DOCX);
    ArrayList<DocumentFile> files = new ArrayList<>();
    files.add(file1);
    files.add(file2);
    return files;
  }
}