package org.younginhambak.backend.gallery.repository;

import org.younginhambak.backend.gallery.entity.PhotoTag;
import org.younginhambak.backend.gallery.entity.PhotoTagId;

import java.util.List;
import java.util.Optional;

public interface PhotoTagRepository {
  Optional<PhotoTag> findById(PhotoTagId id);
  List<PhotoTag> findByPhotoId(Long photoId);
  List<PhotoTag> findByIdsIn(List<PhotoTagId> ids);
}
