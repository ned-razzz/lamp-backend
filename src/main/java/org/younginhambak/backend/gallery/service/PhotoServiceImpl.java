package org.younginhambak.backend.gallery.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.younginhambak.backend.file.entity.PhotoFile;
import org.younginhambak.backend.file.service.PhotoFileService;
import org.younginhambak.backend.gallery.dto.PhotoCreateRequest;
import org.younginhambak.backend.gallery.dto.PhotoDetailResponse;
import org.younginhambak.backend.gallery.dto.PhotoUpdateRequest;
import org.younginhambak.backend.gallery.entity.Photo;
import org.younginhambak.backend.gallery.entity.PhotoTag;
import org.younginhambak.backend.gallery.entity.PhotoTagId;
import org.younginhambak.backend.gallery.repository.PhotoRepository;
import org.younginhambak.backend.gallery.repository.PhotoTagRepository;
import org.younginhambak.backend.member.entity.Member;
import org.younginhambak.backend.member.service.MemberService;
import org.younginhambak.backend.tag.Tag;
import org.younginhambak.backend.tag.dto.TagSplitDto;
import org.younginhambak.backend.tag.service.TagService;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

  private final PhotoRepository photoRepository;
  private final PhotoTagRepository photoTagRepository;
  private final PhotoFileService photoFileService;
  private final TagService tagService;
  private final MemberService memberService;

  @Override
  public Optional<Photo> getPhoto(Long photoId) {
    return photoRepository.findById(photoId);
  }

  @Override
  public List<Photo> getPhotoAll() {
    return photoRepository.findAll();
  }

  @Override
  public List<PhotoTag> getPhotoTags(List<PhotoTagId> photoTagIds) {
    List<PhotoTag> photoTags = photoTagRepository.findByIdsIn(photoTagIds);
    Assert.state(photoTags.size() == photoTagIds.size(), "There are some ids of PhotoTagIds that don't exist.");
    return photoTags;
  }

  @Override
  public PhotoDetailResponse readPhoto(Long photoId) {
    Photo photo = getPhoto(photoId).orElseThrow();
    URL downloadUrl = photoFileService.downloadFile(photo.getFile().getId());
    return PhotoDetailResponse.builder()
            .id(photo.getId())
            .title(photo.getTitle())
            .description(photo.getDescription())
            .photographer(photo.getPhotographer())
            .tagNames(photo.getTagNames())
            .fileUrl(downloadUrl)
            .created(photo.getCreated())
            .updated(photo.getUpdated())
            .build();
  }

  @Override
  public List<PhotoDetailResponse> readPhotos() {
    List<Photo> photos = getPhotoAll();
    return photos.stream().map(photo -> {
      URL downloadUrl = photoFileService.generateDownloadUrl(photo.getFile().getFileKey(), photo.getFile().getFileName());
      return PhotoDetailResponse.builder()
              .id(photo.getId())
              .title(photo.getTitle())
              .description(photo.getDescription())
              .photographer(photo.getPhotographer())
              .takenAt(photo.getTakenAt())
              .tagNames(photo.getTagNames())
              .fileUrl(downloadUrl)
              .created(photo.getCreated())
              .updated(photo.getUpdated())
              .build();
    }).toList();
  }

  @Override
  @Transactional
  public Long createPhoto(PhotoCreateRequest createRequest) {
    Member member = memberService.getMember(createRequest.getCreatorMemberId()).orElseThrow();
    PhotoFile photoFile = photoFileService.getFile(createRequest.getFileId()).orElseThrow();
    List<PhotoTag> photoTags = getOrCreatePhotoTags(createRequest.getTagNames());

    Photo photo = Photo.create(
            createRequest.getTitle(),
            createRequest.getDescription(),
            createRequest.getPhotographer(),
            createRequest.getTakenAt(),
            member,
            photoFile,
            photoTags
    );
    photoRepository.save(photo);
    return photo.getId();
  }

  @Override
  @Transactional
  public void updatePhoto(Long photoId, PhotoUpdateRequest updateRequest) {
    Photo photo = getPhoto(photoId).orElseThrow();
    List<PhotoTag> photoTags = getOrCreatePhotoTags(photoId, updateRequest.getTagNames());

    String string = photoTags.stream()
            .map(photoTag -> "%s".formatted(photoTag.getTag().getName()))
            .collect(Collectors.joining(", "));
    log.info("updatePhoto() photoTags : {}", string);

    photo.update(
            updateRequest.getTitle(),
            updateRequest.getDescription(),
            updateRequest.getPhotographer(),
            updateRequest.getTakenAt(),
            photoTags
    );

    log.info("updatePhoto() photoTags : {}", String.join(", ", photo.getTagNames()));
  }

  @Override
  @Transactional
  public void deletePhoto(Long photoId) {
    Photo photo = getPhoto(photoId).orElseThrow();
    photo.delete();
  }

  private List<PhotoTag> getOrCreatePhotoTags(List<String> tagNames) {
    List<PhotoTag> photoTags = new ArrayList<>();

    //DB에 이미 있는 태그와 없는 태그 분리
    TagSplitDto tagSplitDto = tagService.splitTagNamesByExistence(tagNames);

    //이미 DB에 존재하는 태그 이름이면 불러와서 PhotoTag 생성
    tagService.getTagsByName(tagSplitDto.getExistingTagNames()).stream()
            .map(PhotoTag::create)
            .forEach(photoTags::add);

    //DB에 없는 태그 이름이면 Tag 생성 + PhotoTag 생성
    tagSplitDto.getNonExistingTagNames().stream()
            .map(Tag::create)
            .map(PhotoTag::create)
            .forEach(photoTags::add);
    return photoTags;
  }

  private List<PhotoTag> getOrCreatePhotoTags(Long photoId, List<String> tagNames) {
    List<PhotoTag> photoTags = new ArrayList<>();

    //DB에 이미 있는 태그와 없는 태그 분리
    TagSplitDto tagSplitDto = tagService.splitTagNamesByExistence(tagNames);

    //DB에 없는 태그 처리
    //새로운 Tag 생성 및 새로운 PhotoTag 생성
    tagSplitDto.getNonExistingTagNames().stream()
            .map(Tag::create)
            .map(PhotoTag::create)
            .forEach(photoTags::add);

    //DB에 있는 태그 처리
    //이미 DB에서 Tag 불러오기
    List<Tag> existingTags = tagService.getTagsByName(tagSplitDto.getExistingTagNames());

    //대상 photo의 이미 존재하는 PhotoTag 불러오기
    Map<Long, PhotoTag> existingPhotoTags = photoTagRepository.findByPhotoId(photoId).stream()
            .collect(Collectors.toMap(
                    pt -> pt.getTag().getId(),
                    pt -> pt));
    //photo에서 이미 PhotoTag로 연결된 Tag들은 불러오고, PhotoTag로 연결되지 않았으면 새로 생성
    existingTags.stream()
            .map(tag -> existingPhotoTags.getOrDefault(tag.getId(), PhotoTag.create(tag)))
            .forEach(photoTags::add);
    return photoTags;
  }
}
