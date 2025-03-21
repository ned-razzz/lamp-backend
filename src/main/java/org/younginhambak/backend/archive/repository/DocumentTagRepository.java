package org.younginhambak.backend.archive.repository;

import org.younginhambak.backend.archive.entity.DocumentTag;
import org.younginhambak.backend.archive.entity.DocumentTagId;

import java.util.List;
import java.util.Optional;

public interface DocumentTagRepository {
  Optional<DocumentTag> findById(DocumentTagId id);
  List<DocumentTag> findByIdsIn(List<DocumentTagId> ids);
  List<DocumentTag> findByDocumentId(Long documentId);
}
