package org.younginhambak.backend.file.service;

import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.younginhambak.backend.file.entity.DocumentFile;
import org.younginhambak.backend.file.dto.DocumentFileUploadRequest;
import org.younginhambak.backend.file.repository.DocumentFileRepository;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class DocumentFileServiceImpl implements DocumentFileService{

  private final DocumentFileRepository documentFileRepository;
  private final S3Template s3Template;
  private final S3Presigner s3Presigner;

  @Value("${spring.cloud.aws.s3.bucket}")
  private String bucketName;
  @Value("${spring.cloud.aws.s3.folder.document}")
  private String savedFolder;

  private static final Duration presignedUrlTTL = Duration.ofMinutes(20L);

  @Override
  public Optional<DocumentFile> getFile(Long fileId) {
    return documentFileRepository.findById(fileId);
  }

  @Override
  public List<DocumentFile> getFiles(List<Long> fileIds) {
    List<DocumentFile> files = documentFileRepository.findByIdIn(fileIds);
    if (files.size() != fileIds.size()) {
      throw new NoSuchElementException("file ids 중에 존재하지 않는 record의 id가 있습니다.");
    }
    return files;
  }

  @Override
  @Transactional
  public void uploadFile(DocumentFileUploadRequest uploadRequest) {
    // 원본 파일명에 UUID를 추가하여 고유한 키 생성
    String fileKey = generateUniqueKey(savedFolder + uploadRequest.getFileName());

    // S3Template을 사용하여 파일 업로드
    try (InputStream inputStream = uploadRequest.getFile().getInputStream()) {
      s3Template.upload(bucketName, fileKey, inputStream);
    } catch (IOException e) {
      throw new RuntimeException("파일 업로드 실패", e);
    }

    //메타데이터 DB 저장
    DocumentFile documentFile = DocumentFile.create(uploadRequest.getFileName(), fileKey, uploadRequest.getExtension());
    documentFileRepository.save(documentFile);
  }

  @Override
  @Transactional
  public void uploadFiles(List<DocumentFileUploadRequest> uploadRequests) {
    uploadRequests.forEach(uploadDto -> {
      // 원본 파일명에 UUID를 추가하여 고유한 키 생성
      String fileKey = generateUniqueKey(savedFolder + uploadDto.getFileName());

      // S3Template을 사용하여 파일 업로드
      try (InputStream inputStream = uploadDto.getFile().getInputStream()) {
        s3Template.upload(bucketName, fileKey, inputStream);
      } catch (IOException e) {
        throw new RuntimeException("파일 업로드 실패", e);
      }

      //메타데이터 DB 저장
      DocumentFile documentFile = DocumentFile.create(uploadDto.getFileName(), fileKey, uploadDto.getExtension());
      documentFileRepository.save(documentFile);
    });
  }

  @Override
  @Transactional
  public void deleteFile(Long fileId) {
    DocumentFile documentFile = documentFileRepository.findById(fileId).orElseThrow();
    documentFile.delete();
  }

  @Override
  public URL downloadFile(Long fileId) {
    DocumentFile file = documentFileRepository.findById(fileId).orElseThrow();
    return generateDownloadUrl(file.getFileKey(), file.getFileName());
  }

  @Override
  public List<URL> downloadFiles(List<Long> fileIds) {
    List<DocumentFile> files = getFiles(fileIds);
    return files.stream()
            .map(file -> generateDownloadUrl(file.getFileKey(), file.getFileName()))
            .toList();
  }

  private String generateUniqueKey(String fileName) {
    String uuid = UUID.randomUUID().toString();
    return fileName + "_" + uuid;
  }

  private URL generateDownloadUrl(String fileKey, String fileName) {
    PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignReq -> presignReq
            .signatureDuration(presignedUrlTTL)
            .getObjectRequest(req -> req
                    .bucket(bucketName)
                    .key(fileKey)
                    //다운로드 받을 때 파일명을 fileKey가 아니라 fileName으로 설정
                    .responseContentDisposition("attachment; filename=\"%s\"".formatted(fileName))
                    .build()
            )
    );
    return presignedRequest.url();
  }
}
