package org.younginhambak.backend.member.service;

import org.springframework.transaction.annotation.Transactional;
import org.younginhambak.backend.member.dto.MemberCreateDto;
import org.younginhambak.backend.member.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberService {

  Optional<Member> getMember(Long memberId);
  List<Member> getMemberAll();

  void createMember(MemberCreateDto memberCreateDto);
  void updatMember();
  void deleteMember();
}
