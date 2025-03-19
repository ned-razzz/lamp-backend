package org.younginhambak.backend.gallery.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.younginhambak.backend.gallery.dto.PhotoCreateBatchRequest;
import org.younginhambak.backend.gallery.dto.PhotoCreateRequest;
import org.younginhambak.backend.gallery.dto.PhotoDetailResponse;
import org.younginhambak.backend.gallery.dto.PhotoUpdateRequest;
import org.younginhambak.backend.gallery.service.PhotoService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/photos")
@RequiredArgsConstructor
@Slf4j
public class PhotoController {

  private final PhotoService photoService;

  @GetMapping("/{photoId}")
  public PhotoDetailResponse readPhoto(@PathVariable Long photoId) {
    log.info("GET /api/v1/photos/{}", photoId);
    return photoService.readPhoto(photoId);
  }

  @GetMapping
  public List<PhotoDetailResponse> readPhotos() {
    log.info("GET /api/v1/photos");
    return photoService.readPhotos();
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public Long createPhoto(@RequestBody PhotoCreateRequest createRequest) {
    log.info("POST /api/v1/photos : {}", createRequest);
    return photoService.createPhoto(createRequest);
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("/batch")
  public List<Long> createPhotos(@RequestBody PhotoCreateBatchRequest createBatchRequest) {
    log.info("POST /api/v1/photos/batch : {}", createBatchRequest);
    return photoService.createPhotos(createBatchRequest);
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PutMapping("/{photoId}")
  public void updatePhoto(
          @PathVariable Long photoId,
          @RequestBody PhotoUpdateRequest updateRequest
          ) {
    log.info("PUT /api/v1/photos/{} : {}", photoId, updateRequest);
    photoService.updatePhoto(photoId, updateRequest);
  }

  @DeleteMapping("/{photoId}")
  public void deletePhoto(@PathVariable Long photoId) {
    log.info("DELETE /api/v1/photos/{}", photoId);
    photoService.deletePhoto(photoId);
  }
}
