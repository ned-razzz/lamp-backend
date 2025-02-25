package org.younginhambak.backend.file.service;

import io.awspring.cloud.s3.S3Template;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.younginhambak.backend.file.repository.DocumentFileRepository;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;

@SpringBootTest
@Transactional
@Slf4j
class LocalStackS3Test {

  @Autowired
  DocumentFileRepository documentFileRepository;
  @Autowired
  S3Template s3Template;

  @Test
  void getFile() throws IOException {
    String bucketName = "project-lamp";
    String fileName = ".gitattributes";

    URL downloadURL = s3Template.createSignedGetURL(bucketName,fileName, Duration.ofMinutes(1L));
    System.out.println(downloadURL);
  }

  @Test
  void getFileAll() {
  }

  @Test
  void uploadFiles() throws IOException {
    //환경 변수 설정

    String bucketName = "project-lamp";
    String savedFolder = "documents";

    String fileName = "/test-file";
    String contentType = "text/plain";
    MockMultipartFile file = new MockMultipartFile(
            fileName,
            fileName + ".txt",
            contentType,
            "test content".getBytes()
    );

    InputStream inputStream = file.getInputStream();
    s3Template.upload(bucketName, savedFolder + fileName, inputStream);
  }

  @Test
  void deleteFile() {
  }
}