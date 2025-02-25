package org.younginhambak.backend.archive.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import org.younginhambak.backend.tag.service.TagService;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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
    if (documentTags.size() != documentTagIds.size()) {
      throw new NoSuchElementException("documentTag ids 중에 존재하지 않는 record의 id가 있습니다.");
    }
    return documentTags;
  }

  @Override
  public DocumentDetailResponse readDocumentDetail(Long documentId) {
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

  //영속성에서 관리되는 Tag를 구분해서 DocumentTag를 생성
  private List<DocumentTag> getOrCreateDocumentTags(List<String> tagNames) {
    //영속성으로 관리되는 Tag는 가져오고, 아닌 Tag는 객체 생성
    TagNamesSeparator tagNamesSeparator = separateTagNamesByPersistence(tagNames);
    TagsSeparator tagsSeparator = createTags(tagNamesSeparator);

    //이미 존재하는 Tag들과 새로 생성되는 Tag들을 DocumentTag 생성
    List<DocumentTag> documentTags = new ArrayList<DocumentTag>();
    tagsSeparator.newTags.stream()
            .map(DocumentTag::create)
            .forEach(documentTags::add);
    tagsSeparator.persistentTags.stream()
            .map(DocumentTag::create)
            .forEach(documentTags::add);

    return documentTags;
  }

  //영속성에서 관리되는 Tag, DocumentTag를 구분해서 DocumentTag를 생성 또는 가져오기
  private List<DocumentTag> getOrCreateDocumentTags(Long documentId, List<String> tagNames) {
    TagNamesSeparator tagNamesSeparator = separateTagNamesByPersistence(tagNames);
    TagsSeparator tagsSeparator = createTags(tagNamesSeparator);

    List<DocumentTag> documentTags = new ArrayList<DocumentTag>();

    //DB에 없는 Tag들은 DocumentTag 생성
    tagsSeparator.newTags.stream()
            .map(DocumentTag::create)
            .forEach(documentTags::add);

    //이미 영속성에 관리되는 Tag들 중에 이미 DocumentTag로 연결되어 있으면 불러오기
    List<DocumentTag> persistentDocumentTags = getDocumentTags(
            tagsSeparator.persistentTags.stream()
                    .map(tag -> new DocumentTagId(documentId, tag.getId()))
                    .toList()
    );
    documentTags.addAll(persistentDocumentTags);

    //이미 영속성에 관리되는 Tag들 중에 이미 DocumentTag로 연결되지 않았으면 불러오기
    List<Tag> connectedTagsAlready = persistentDocumentTags.stream().map(DocumentTag::getTag).toList();
    tagsSeparator.persistentTags.stream()
            .filter(tag -> !connectedTagsAlready.contains(tag))
            .map(DocumentTag::create)
            .forEach(documentTags::add);
    return documentTags;
  }

  //해당 태그가 이미 DB에 존재하는지 아닌지 검증하고 분리
  private TagNamesSeparator separateTagNamesByPersistence(List<String> tagNames) {
    List<String> existTagNames = tagService.filterExistTagNames(tagNames);
    List<String> nonexistTagNames = tagNames.stream()
            .filter(tagName -> !existTagNames.contains(tagName))
            .toList();
    return new TagNamesSeparator(existTagNames, nonexistTagNames);
  }

  //영속성으로 관리되는 Tag는 가져오고 아닌 Tag는 생성
  private TagsSeparator createTags(TagNamesSeparator namesSeparator) {
    return new TagsSeparator(
            namesSeparator.existTagNames.stream().map(tagName -> tagService.getTagByName(tagName).orElseThrow()).toList(),
            namesSeparator.nonexistTagNames.stream().map(Tag::create).toList()
    );
  }

  //새로 생성되는 태그와 영속성에서 가져온 태그를 구분하는 용도
  private record TagNamesSeparator(List<String> existTagNames, List<String> nonexistTagNames) { }
  private record TagsSeparator(List<Tag> persistentTags, List<Tag> newTags) {
  }
}
