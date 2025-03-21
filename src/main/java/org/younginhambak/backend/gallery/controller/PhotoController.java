package org.younginhambak.backend.gallery.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.younginhambak.backend.gallery.dto.*;
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
  public List<PhotoDetailResponse> searchPhotos(@ModelAttribute PhotoSearchRequest searchRequest) {
    log.info("GET /api/v1/photos?");
    log.info("title = {}", searchRequest.getTitle());
    log.info("tags = {}", searchRequest.getTags());
    return photoService.readPhotos(searchRequest);
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public Long createPhoto(@RequestBody PhotoCreateRequest createRequest) {
    log.info("POST /api/v1/photos : {}", createRequest);
    return photoService.createPhoto(createRequest);
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("/batch")
  public List<Long> createPhotoBatch(@RequestBody PhotoCreateBatchRequest createBatchRequest) {
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


  @DeleteMapping("/batch")
  public void deletePhotoBatch(@RequestBody List<Long> photoIds) {
    log.info("DELETE /api/v1/photos/batch : {}", photoIds);
    photoService.deletePhotos(photoIds);
  }
}
