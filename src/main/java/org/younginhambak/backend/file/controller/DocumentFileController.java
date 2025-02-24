package org.younginhambak.backend.file.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.younginhambak.backend.file.entity.FileExtension;
import org.younginhambak.backend.file.dto.DocumentFileUploadRequest;
import org.younginhambak.backend.file.service.DocumentFileService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/files/documents")
@RequiredArgsConstructor
@Slf4j
public class DocumentFileController {

  private final DocumentFileService documentFileService;

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public void uploadFile(@RequestParam MultipartFile file) {
    log.info("POST /files/documents : {}", file.getOriginalFilename());

    DocumentFileUploadRequest uploadRequest = convertUploadDto(file);
    documentFileService.uploadFile(uploadRequest);
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(value = "/batch", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public void uploadFiles(@RequestParam List<MultipartFile> files) {
    log.info("POST /files/documents/batch : {}", getFilesName(files));
    List<DocumentFileUploadRequest> uploadRequests = files.stream()
            .map(this::convertUploadDto)
            .toList();
    documentFileService.uploadFiles(uploadRequests);
  }

  @DeleteMapping("/{fileId}")
  public void deleteFile(@PathVariable Long fileId) {
    log.info("DELETE /files/documents : {}", fileId);
    documentFileService.deleteFile(fileId);
  }

  @GetMapping("/{fileId}")
  public String downloadFile(@PathVariable Long fileId) {
    log.info("GET /files/documents : {}", fileId);
    return documentFileService.downloadFile(fileId);
  }

  private DocumentFileUploadRequest convertUploadDto(MultipartFile file) {
    return DocumentFileUploadRequest.builder()
            .file(file)
            .fileName(file.getOriginalFilename())
            .extension(extractExtension(file.getOriginalFilename()))
            .build();
  }

  private FileExtension extractExtension(String fileName) {
    if (fileName == null || fileName.isEmpty()) {
      throw new IllegalArgumentException("fileName is null");
    }

    String extension = fileName.split("\\.")[1].trim();
    return FileExtension.valueOf(extension.toUpperCase());
  }

  private String getFilesName(List<MultipartFile> files) {
    return files.stream().map(MultipartFile::getOriginalFilename).collect(Collectors.joining(", "));
  }
}
