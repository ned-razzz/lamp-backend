package org.younginhambak.backend.file.service;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.younginhambak.backend.file.dto.DocumentFileUploadRequest;
import org.younginhambak.backend.file.entity.DataFileStatus;
import org.younginhambak.backend.file.entity.DocumentFile;
import org.younginhambak.backend.file.entity.FileExtension;
import org.younginhambak.backend.file.repository.DocumentFileRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@SpringBootTest
@Transactional
@Slf4j
class DocumentFileServiceImplTest {

  @Autowired
  DocumentFileService documentFileService;
  @Autowired
  DocumentFileRepository documentFileRepository;

  @Test
  void uploadFiles() {
    //given
    List<DocumentFileUploadRequest> createDtos = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      createDtos.add(DocumentFileUploadRequest.builder()
                      .fileName("test_file_%d".formatted(i))
                      .extension(FileExtension.TXT)
                      .file(new MockMultipartFile(
                              "test",
                              "test%d.txt".formatted(i),
                              "text/plain",
                              "test content %d".formatted(i).getBytes()
                              )
                      )
                      .build()
      );
    }

    //when
    documentFileService.uploadFiles(createDtos);

    //then
    DocumentFile file1 = documentFileService.getFile(1L).orElseThrow();
    DocumentFile file2 = documentFileService.getFile(2L).orElseThrow();

    Assertions.assertThat(file1.getFileName()).isEqualTo(createDtos.get(0).getFileName());
    Assertions.assertThat(file2.getFileName()).isEqualTo(createDtos.get(1).getFileName());
  }

  @Test
  void downloadFile() {
    //given
    List<DocumentFileUploadRequest> createDtos = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      createDtos.add(DocumentFileUploadRequest.builder()
              .fileName("test_file_%d".formatted(i))
              .extension(FileExtension.TXT)
              .file(new MockMultipartFile(
                              "test",
                              "test%d.txt".formatted(i),
                              "text/plain",
                              "test content %d".formatted(i).getBytes()
                      )
              )
              .build()
      );
    }
    documentFileService.uploadFiles(createDtos);

    //when
    String url1 = documentFileService.downloadFile(2L);
    String url2 = documentFileService.downloadFile(3L);

    //then
    log.info(url1);
    log.info(url2);
  }

  @Test
  void deleteFile() {    //given
    List<DocumentFileUploadRequest> createDtos = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      createDtos.add(DocumentFileUploadRequest.builder()
              .fileName("test_file_%d".formatted(i))
              .extension(FileExtension.TXT)
              .file(new MockMultipartFile(
                              "test",
                              "test%d.txt".formatted(i),
                              "text/plain",
                              "test content %d".formatted(i).getBytes()
                      )
              )
              .build()
      );
    }
    documentFileService.uploadFiles(createDtos);

    //when
    documentFileService.deleteFile(1L);

    //then
    DocumentFile file1 = documentFileService.getFile(1L).orElseThrow();
    Assertions.assertThat(file1.getStatus()).isEqualTo(DataFileStatus.DELETED);
  }
}