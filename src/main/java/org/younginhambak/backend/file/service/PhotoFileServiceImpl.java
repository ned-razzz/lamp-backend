package org.younginhambak.backend.file.service;

import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.younginhambak.backend.file.dto.PhotoFileUploadRequest;
import org.younginhambak.backend.file.entity.PhotoFile;
import org.younginhambak.backend.file.repository.PhotoFileRepository;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PhotoFileServiceImpl implements PhotoFileService {

  private final PhotoFileRepository photoFileRepository;
  private final S3Template s3Template;
  private final S3Presigner s3Presigner;

  @Value("${spring.cloud.aws.s3.bucket}")
  private String bucketName;
  @Value("${spring.cloud.aws.s3.folder.photo}")
  private String savedFolder;

  private static final Duration presignedUrlTTL = Duration.ofMinutes(20L);


  @Override
  public Optional<PhotoFile> getFile(Long fileId) {
    return photoFileRepository.findById(fileId);
  }

  @Override
  public List<PhotoFile> getFiles(List<Long> fileIds) {
    List<PhotoFile> files = photoFileRepository.findByIdIn(fileIds);
    Assert.state(files.size() == fileIds.size(), "file ids 중에 존재하지 않는 record의 id가 있습니다.");
    return files;
  }

  @Override
  @Transactional
  public void uploadFile(PhotoFileUploadRequest uploadRequest) {
    String fileKey = generateUniqueKey(savedFolder + uploadRequest.getFileName());
    try (InputStream inputStream = uploadRequest.getFile().getInputStream()) {
      s3Template.upload(bucketName, fileKey, inputStream);
    } catch (IOException e) {
      throw new RuntimeException("Faild to upload file", e);
    }
    PhotoFile photoFile = PhotoFile.create(uploadRequest.getFileName(), fileKey, uploadRequest.getExtension());
    photoFileRepository.save(photoFile);
  }

  @Override
  @Transactional
  public void uploadFiles(List<PhotoFileUploadRequest> uploadRequests) {
    uploadRequests.forEach(this::uploadFile);
  }

  @Override
  @Transactional
  public void deleteFile(Long fileId) {
    PhotoFile photoFile = photoFileRepository.findById(fileId).orElseThrow();
    photoFile.delete();
  }

  @Override
  public URL downloadFile(Long fileId) {
    PhotoFile file = photoFileRepository.findById(fileId).orElseThrow();
    return generateDownloadUrl(file.getFileKey(), file.getFileName());
  }

  @Override
  public List<URL> downloadFiles(List<Long> fileIds) {
    List<PhotoFile> files = getFiles(fileIds);
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
