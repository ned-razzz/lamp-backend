package org.younginhambak.backend.archive.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.younginhambak.backend.archive.dto.DocumentCreateDto;
import org.younginhambak.backend.archive.dto.DocumentUpdateDto;
import org.younginhambak.backend.archive.service.DocumentService;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Slf4j
@RequiredArgsConstructor
public class DocumentController {

  private final DocumentService documentService;

  @GetMapping("/documents/{documentId}")
  public void getDocument(@PathVariable Long documentId) {
  }

  @GetMapping("/documents")
  public void getDocumentAll() {

  }

  @PostMapping("/documents")
  public void createDocument(
          @RequestPart("document") DocumentCreateDto createDto,
          @RequestPart("files") List<MultipartFile> files) {

  }

  @PatchMapping("/documents/{documentId}")
  public void updatedDocument(
          @PathVariable Long documentId,
          @RequestPart("document") DocumentUpdateDto updateDto,
          @RequestPart("files") List<MultipartFile> files) {

  }

  @DeleteMapping("/documents/{documentId}")
  public void deleteDocument(@PathVariable Long documentId) {

  }
}
