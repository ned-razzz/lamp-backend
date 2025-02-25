package org.younginhambak.backend.file.repository;

import org.younginhambak.backend.file.entity.DocumentFile;

import java.util.List;
import java.util.Optional;

public interface DocumentFileRepository {
  void save(DocumentFile file);
  Optional<DocumentFile> findById(Long id);
  List<DocumentFile> findByIdIn(List<Long> ids);
  List<DocumentFile> findAll();
}
