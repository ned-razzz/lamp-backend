package org.younginhambak.backend.file.service;

import org.younginhambak.backend.file.entity.DocumentFile;
import org.younginhambak.backend.file.dto.DocumentFileUploadRequest;

import java.util.List;
import java.util.Optional;

public interface DocumentFileService {
  Optional<DocumentFile> getFile(Long fileId);
  void uploadFile(DocumentFileUploadRequest uploadRequest);
  void uploadFiles(List<DocumentFileUploadRequest> uploadRequests);
  void deleteFile(Long fileId);
  String downloadFile(Long fileId);
}
