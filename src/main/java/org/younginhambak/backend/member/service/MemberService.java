package org.younginhambak.backend.member.service;

import org.younginhambak.backend.member.Member;

import java.util.List;
import java.util.Optional;

public interface MemberService {

  Optional<Member> getMember(Long memberId);
  List<Member> getMemberAll();
  void createMember();
  void updatMember();
  void deleteMember();
}
