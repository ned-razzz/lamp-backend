package org.younginhambak.backend.file.service;

import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.younginhambak.backend.file.entity.DataFile;
import org.younginhambak.backend.file.entity.DocumentFile;
import org.younginhambak.backend.file.dto.DocumentFileUploadRequest;
import org.younginhambak.backend.file.repository.DocumentFileRepository;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class DocumentFileServiceImpl implements DocumentFileService{

  private final DocumentFileRepository documentFileRepository;
  private final S3Template s3Template;
//  private final S3Client s3Client;
  private final S3Presigner s3Presigner;

  @Value("${spring.cloud.aws.s3.bucket}")
  private String bucketName;
  @Value("${spring.cloud.aws.s3.folder.document}")
  private String savedFolder;

  @Override
  public Optional<DocumentFile> getFile(Long fileId) {
    return documentFileRepository.findById(fileId);
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
  public String downloadFile(Long fileId) {
    DataFile file = documentFileRepository.findById(fileId).orElseThrow();

    //s3 객체 가져오는 요청 객체 생성
    //UUID가 포함된 fileKey가 아니라 fileName으로 가져오도록 헤더 설정
    GetObjectRequest getRequest = GetObjectRequest.builder()
            .bucket(bucketName)
            .key(file.getFileKey())
            .responseContentDisposition("attachment; filename=\"%s\"".formatted(file.getFileName()))
            .build();

    //presign로 유효 기간까지 설정하는 요청 객체 생성
    GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
            .getObjectRequest(getRequest)
            .signatureDuration(Duration.ofMinutes(20L))
            .build();

    //실제 요청하여 응답 url 반환
    URL downloadURL = s3Presigner.presignGetObject(presignRequest).url();
    return downloadURL.toString();
  }

  private String generateUniqueKey(String fileName) {
    String uuid = UUID.randomUUID().toString();
    return fileName + "_" + uuid;
  }
}
