package org.younginhambak.backend.gallery.service;

import org.younginhambak.backend.gallery.dto.PhotoCreateBatchRequest;
import org.younginhambak.backend.gallery.dto.PhotoCreateRequest;
import org.younginhambak.backend.gallery.dto.PhotoDetailResponse;
import org.younginhambak.backend.gallery.dto.PhotoUpdateRequest;
import org.younginhambak.backend.gallery.entity.Photo;
import org.younginhambak.backend.gallery.entity.PhotoTag;
import org.younginhambak.backend.gallery.entity.PhotoTagId;

import java.util.List;
import java.util.Optional;

public interface PhotoService {
  Optional<Photo> getPhoto(Long photoId);
  List<Photo> getPhotos(List<Long> photoIds);
  List<Photo> getPhotoAll();
  List<PhotoTag> getPhotoTags(List<PhotoTagId> photoTagIds);
  PhotoDetailResponse readPhoto(Long photoId);
  List<PhotoDetailResponse> readPhotos();
  Long createPhoto(PhotoCreateRequest createRequest);
  List<Long> createPhotos(PhotoCreateBatchRequest createBatchRequest);
  void updatePhoto(Long photoId, PhotoUpdateRequest updateDto);
  void deletePhoto(Long photoId);
  void deletePhotos(List<Long> photoIds);
}
