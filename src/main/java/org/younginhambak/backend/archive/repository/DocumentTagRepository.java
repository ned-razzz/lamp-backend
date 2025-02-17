package org.younginhambak.backend.archive.repository;


import org.younginhambak.backend.archive.entity.DocumentTag;
import org.younginhambak.backend.archive.entity.DocumentTagId;

import java.util.List;
import java.util.Optional;

public interface DocumentTagRepository {

  void save(DocumentTag documentTag);

  Optional<DocumentTag> findById(DocumentTagId id);

  List<DocumentTag> findByDocumentId(Long id);

  List<DocumentTag> findByTagId(Long id);

  List<DocumentTag> findAll();
}
