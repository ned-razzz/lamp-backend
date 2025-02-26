package org.younginhambak.backend.file.repository;

import org.younginhambak.backend.file.entity.PhotoFile;

import java.util.List;
import java.util.Optional;

public interface PhotoFileRepository {
  void save(PhotoFile file);
  Optional<PhotoFile> findById(Long id);
  List<PhotoFile> findByIdIn(List<Long> ids);
  List<PhotoFile> findAll();
}
