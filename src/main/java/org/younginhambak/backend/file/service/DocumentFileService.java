package org.younginhambak.backend.file.service;

import org.younginhambak.backend.file.entity.DocumentFile;
import org.younginhambak.backend.file.dto.DocumentFileUploadRequest;

import java.net.URL;
import java.util.List;
import java.util.Optional;

public interface DocumentFileService {
  Optional<DocumentFile> getFile(Long fileId);
  List<DocumentFile> getFiles(List<Long> fileIds);
  Long uploadFile(DocumentFileUploadRequest uploadRequest);
  List<Long> uploadFiles(List<DocumentFileUploadRequest> uploadRequests);
  void deleteFile(Long fileId);
  URL downloadFile(Long fileId);
  List<URL> downloadFiles(List<Long> fileIds);
  URL generateDownloadUrl(String fileKey, String fileName);
}
