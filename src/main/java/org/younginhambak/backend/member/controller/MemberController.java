package org.younginhambak.backend.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.younginhambak.backend.member.dto.MemberCreateDto;
import org.younginhambak.backend.member.service.MemberService;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

  private final MemberService memberService;

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public void createMember(@RequestBody MemberCreateDto createDto) {
    log.info("POST /members : {}", createDto);
    memberService.createMember(createDto);
  }
}
