package org.younginhambak.backend.file.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.younginhambak.backend.file.dto.DocumentFileUploadRequest;
import org.younginhambak.backend.file.dto.PhotoFileUploadRequest;
import org.younginhambak.backend.file.entity.FileExtension;
import org.younginhambak.backend.file.service.DocumentFileService;
import org.younginhambak.backend.file.service.PhotoFileService;

import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/files/photos")
@RequiredArgsConstructor
@Slf4j
public class PhotoFileController {

  private final PhotoFileService photoFileService;

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public void uploadFile(@RequestParam MultipartFile file) {
    log.info("POST /files/photos : {}", file.getOriginalFilename());

    PhotoFileUploadRequest uploadRequest = convertUploadDto(file);
    photoFileService.uploadFile(uploadRequest);
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(value = "/batch", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public void uploadFiles(@RequestParam List<MultipartFile> files) {
    log.info("POST /files/photos/batch : {}", getFilesName(files));
    List<PhotoFileUploadRequest> uploadRequests = files.stream()
            .map(this::convertUploadDto)
            .toList();
    photoFileService.uploadFiles(uploadRequests);
  }

  @DeleteMapping("/{fileId}")
  public void deleteFile(@PathVariable Long fileId) {
    log.info("DELETE /files/photos/{}", fileId);
    photoFileService.deleteFile(fileId);
  }

  @GetMapping("/{fileId}")
  public URL downloadFile(@PathVariable Long fileId) {
    log.info("GET /files/photos/{}", fileId);
    return photoFileService.downloadFile(fileId);
  }

  private PhotoFileUploadRequest convertUploadDto(MultipartFile file) {
    return PhotoFileUploadRequest.builder()
            .file(file)
            .fileName(file.getOriginalFilename())
            .extension(extractExtension(file.getOriginalFilename()))
            .build();
  }

  private FileExtension extractExtension(String fileName) {
    Assert.hasText(fileName, "fileName parameter is empty.");
    String extension = fileName.split("\\.")[1].trim();
    return FileExtension.valueOf(extension.toUpperCase());
  }

  private String getFilesName(List<MultipartFile> files) {
    return files.stream().map(MultipartFile::getOriginalFilename).collect(Collectors.joining(", "));
  }
}
