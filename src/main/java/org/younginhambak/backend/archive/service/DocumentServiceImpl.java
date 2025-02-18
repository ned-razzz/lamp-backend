package org.younginhambak.backend.archive.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.younginhambak.backend.archive.dto.DocumentCreateDto;
import org.younginhambak.backend.archive.dto.DocumentUpdateDto;
import org.younginhambak.backend.archive.entity.Document;
import org.younginhambak.backend.archive.entity.DocumentTag;
import org.younginhambak.backend.archive.entity.DocumentTagId;
import org.younginhambak.backend.archive.repository.DocumentRepository;
import org.younginhambak.backend.archive.repository.DocumentTagRepository;
import org.younginhambak.backend.member.Member;
import org.younginhambak.backend.member.service.MemberService;
import org.younginhambak.backend.tag.Tag;
import org.younginhambak.backend.tag.service.TagService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

  private final DocumentRepository documentRepository;
  private final DocumentTagRepository documentTagRepository;
  private final MemberService memberService;
  private final TagService tagService;

  @Override
  public Optional<Document> getDocument(Long id) {
    return documentRepository.findById(id);
  }

  @Override
  public List<Document> getDocumentAll() {
    return documentRepository.findAll();
  }

  @Override
  @Transactional
  public void createDocument(DocumentCreateDto createDto) {
    Member member = memberService.getMember(createDto.getCreatorMemberId()).orElseThrow();

    //DB에 없는 tagName들은 Tag, DocumentTag 생성
    List<DocumentTag> documentTags = createTagAndDocumentTag(createDto.getTagNames());

    //이미 존재하는 tagName들은 Tag를 불러와서 DocumentTag 생성
    List<Tag> existTags = getExistTag(createDto.getTagNames());
    existTags.stream()
            .map(DocumentTag::create)
            .forEach(documentTags::add);

    Document document = Document.create(
            createDto.getTitle(),
            createDto.getDescription(),
            createDto.getAuthorName(),
            member,
            //todo 나중에 file 파라미터로 받아서 DocumentFile 생성
            List.of(),
            documentTags
    );
    documentRepository.save(document);
  }

  @Override
  @Transactional
  public void updatedDocument(Long documentId, DocumentUpdateDto updateDto) {
    Document document = documentRepository.findById(documentId).orElseThrow(IllegalStateException::new);

    //DB에 없는 tagName들은 Tag, DocumentTag 생성
    List<DocumentTag> documentTags = createTagAndDocumentTag(updateDto.getTagNames());

    //이미 존재하는 tagName들은 Tag를 불러오기
    //todo DB에 documentTagRepository.findById 쿼리를 계속 날린다. 나중에 쿼리 최적화 필요
    List<Tag> existTags = getExistTag(updateDto.getTagNames());
    for (Tag tag : existTags) {
      DocumentTagId documentTagId = new DocumentTagId(documentId, tag.getId());
      Optional<DocumentTag> result = documentTagRepository.findById(documentTagId);

      //불러온 Tag들이 document 객체와 연결 안되어 있으면 DocumentTag 생성, 이미 연결되어 있으면 불러오기
      if (result.isPresent()) {
        documentTags.add(result.get());
      }
      else {
        documentTags.add(DocumentTag.create(tag));
      }
    }

    document.update(
            updateDto.getTitle(),
            updateDto.getDescription(),
            updateDto.getAuthorName(),
            List.of(),
            documentTags
    );
  }

  @Override
  @Transactional
  public void deleteDocument(Long documentId) {
    Document document = documentRepository.findById(documentId).orElseThrow();
    document.delete();
  }

  //이미 존재하는 name의 Tag 객체들 가져오기
  private List<Tag> getExistTag(List<String> tagNames) {
    return tagNames.stream()
            //todo existTag()가 게속 모든 tag들을 findAll()하므로 오버헤드 발생. DB 최적화 필요함.
            .filter(tagService::existTag)
            .map(name -> tagService.getTagByName(name).orElseThrow())
            .collect(Collectors.toCollection(ArrayList::new));
  }

  //존재하지 않는 tag names들만 Tag와 DocumentTag 객체 생성
  private List<DocumentTag> createTagAndDocumentTag(List<String> tagNames) {
    return tagNames.stream()
            //todo existTag()가 게속 모든 tag들을 findAll()하므로 오버헤드 발생. DB 최적화 필요함.
            .filter(tagName -> !tagService.existTag(tagName))
            .map(Tag::create)
            .map(DocumentTag::create)
            .collect(Collectors.toCollection(ArrayList::new));
  }
}
