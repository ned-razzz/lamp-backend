package org.younginhambak.backend.archive.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.younginhambak.backend.archive.dto.DocumentCreateRequest;
import org.younginhambak.backend.archive.dto.DocumentDetailResponse;
import org.younginhambak.backend.archive.dto.DocumentInfoResponse;
import org.younginhambak.backend.archive.dto.DocumentUpdateRequest;
import org.younginhambak.backend.archive.entity.Document;
import org.younginhambak.backend.archive.entity.DocumentTag;
import org.younginhambak.backend.archive.entity.DocumentTagId;
import org.younginhambak.backend.archive.repository.DocumentRepository;
import org.younginhambak.backend.archive.repository.DocumentTagRepository;
import org.younginhambak.backend.file.entity.DocumentFile;
import org.younginhambak.backend.file.service.DocumentFileService;
import org.younginhambak.backend.member.Member;
import org.younginhambak.backend.member.service.MemberService;
import org.younginhambak.backend.tag.Tag;
import org.younginhambak.backend.tag.dto.TagSplitDto;
import org.younginhambak.backend.tag.service.TagService;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

  private final DocumentRepository documentRepository;
  private final DocumentTagRepository documentTagRepository;
  private final DocumentFileService documentFileService;
  private final MemberService memberService;
  private final TagService tagService;

  @Override
  public Optional<Document> getDocument(Long documentId) {
    return documentRepository.findById(documentId);
  }

  @Override
  public List<Document> getDocumentAll() {
    return documentRepository.findAll();
  }

  @Override
  public List<DocumentTag> getDocumentTags(List<DocumentTagId> documentTagIds) {
    List<DocumentTag> documentTags = documentTagRepository.findByIdsIn(documentTagIds);
    Assert.state(documentTags.size() == documentTagIds.size(), "There are some DocumentTagIds that don't exist.");
    return documentTags;
  }

  @Override
  public DocumentDetailResponse readDocument(Long documentId) {
    Document document = getDocument(documentId).orElseThrow();
    List<URL> urls = documentFileService.downloadFiles(document.getFileIds());
    return DocumentDetailResponse.builder()
            .id(document.getId())
            .title(document.getTitle())
            .description(document.getDescription())
            .authorName(document.getAuthorName())
            .tags(document.getTagNames().stream().toList())
            .fileUrls(urls)
            .created(document.getCreated())
            .updated(document.getUpdated())
            .build();
  }

  @Override
  public List<DocumentDetailResponse> readDocuments() {
    List<Document> documents = getDocumentAll();
    return documents.stream().map(document -> {
      List<URL> urls = document.getFiles().stream()
              .map(file -> documentFileService.generateDownloadUrl(file.getFileKey(), file.getFileName()))
              .toList();
      return DocumentDetailResponse.builder()
              .id(document.getId())
              .title(document.getTitle())
              .description(document.getDescription())
              .authorName(document.getAuthorName())
              .tags(document.getTagNames())
              .fileUrls(urls)
              .created(document.getCreated())
              .updated(document.getUpdated())
              .build();
    }).toList();
  }

  @Override
  public List<DocumentInfoResponse> readDocumentsInfo() {
    List<Document> documents = getDocumentAll();
    return documents.stream().
            map(document ->
                    DocumentInfoResponse.builder()
                            .id(document.getId())
                            .title(document.getTitle())
                            .authorName(document.getAuthorName())
                            .tags(document.getTagNames().stream().limit(3).toList())
                            .created(document.getCreated())
                            .updated(document.getUpdated())
                            .build())
            .toList();
  }


  @Override
  @Transactional
  public void createDocument(DocumentCreateRequest createDto) {
    Member member = memberService.getMember(createDto.getCreatorMemberId()).orElseThrow();
    List<DocumentFile> documentFiles = documentFileService.getFiles(createDto.getFileIds());
    List<DocumentTag> documentTags = getOrCreateDocumentTags(createDto.getTagNames());

    Document document = Document.create(
            createDto.getTitle(),
            createDto.getDescription(),
            createDto.getAuthorName(),
            member,
            documentFiles,
            documentTags
    );
    documentRepository.save(document);
  }

  @Override
  @Transactional
  public void updateDocument(Long documentId, DocumentUpdateRequest updateDto) {
    Document document = getDocument(documentId).orElseThrow();
    List<DocumentFile> documentFiles = documentFileService.getFiles(updateDto.getFileIds());
    List<DocumentTag> documentTags = getOrCreateDocumentTags(documentId, updateDto.getTagNames());

    document.update(
            updateDto.getTitle(),
            updateDto.getDescription(),
            updateDto.getAuthorName(),
            documentFiles,
            documentTags
    );
  }

  @Override
  @Transactional
  public void deleteDocument(Long documentId) {
    Document document = getDocument(documentId).orElseThrow();
    document.delete();
  }

  private List<DocumentTag> getOrCreateDocumentTags(List<String> tagNames) {
    List<DocumentTag> documentTags = new ArrayList<DocumentTag>();

    //DB에 이미 있는 태그와 없는 태그 분리
    TagSplitDto tagSplitDto = tagService.splitTagNamesByExistence(tagNames);

    //이미 DB에 존재하는 태그 이름이면 불러와서 DocumentTag 생성
    tagSplitDto.getNonExistingTagNames().stream()
            .map(Tag::create)
            .map(DocumentTag::create)
            .forEach(documentTags::add);
    //DB에 없는 태그 이름이면 Tag 생성 + DocumentTag 생성
    tagService.getTagsByName(tagSplitDto.getExistingTagNames()).stream()
            .map(DocumentTag::create)
            .forEach(documentTags::add);

    return documentTags;
  }

  //영속성에서 관리되는 Tag, DocumentTag를 구분해서 DocumentTag를 생성 또는 가져오기
  private List<DocumentTag> getOrCreateDocumentTags(Long documentId, List<String> tagNames) {
    List<DocumentTag> documentTags = new ArrayList<DocumentTag>();

    //DB에 이미 있는 태그와 없는 태그 분리
    TagSplitDto tagSplitDto = tagService.splitTagNamesByExistence(tagNames);

    //DB에 없는 태그 처리
    //새로운 Tag 생성 및 새로운 DocumentTag 생성
    tagSplitDto.getNonExistingTagNames().stream()
            .map(Tag::create)
            .map(DocumentTag::create)
            .forEach(documentTags::add);

    //DB에 있는 태그 처리
    //이미 DB에서 Tag 불러오기
    List<Tag> existingTags = tagService.getTagsByName(tagSplitDto.getExistingTagNames());

    //대상 docuemnt의 이미 존재하는 DocumentTag 불러오기
    Map<Long, DocumentTag> existingDocumentTags = documentTagRepository.findByDocumentId(documentId).stream()
            .collect(Collectors.toMap(
                    dt -> dt.getTag().getId(),
                    dt -> dt));

    //docuemnt에서 이미 DocumentTag로 연결된 Tag들은 불러오고, DocumentTag로 연결되지 않았으면 새로 생성
    existingTags.stream()
            .map(tag -> existingDocumentTags.getOrDefault(tag.getId(), DocumentTag.create(tag)))
            .forEach(documentTags::add);

    return documentTags;
  }
}
