package org.younginhambak.backend.file.service;

import org.younginhambak.backend.file.dto.PhotoFileUploadRequest;
import org.younginhambak.backend.file.entity.PhotoFile;

import java.net.URL;
import java.util.List;
import java.util.Optional;

public interface PhotoFileService {
  Optional<PhotoFile> getFile(Long fileId);
  List<PhotoFile> getFiles(List<Long> fileIds);
  Long uploadFile(PhotoFileUploadRequest uploadRequest);
  List<Long> uploadFiles(List<PhotoFileUploadRequest> uploadRequests);
  void deleteFile(Long fileId);
  URL downloadFile(Long fileId);
  List<URL> downloadFiles(List<Long> fileIds);
  URL generateDownloadUrl(String fileKey, String fileName);
}
