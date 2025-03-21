package org.younginhambak.backend.member.repository;

import org.younginhambak.backend.member.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {

  void save(Member member);

  Optional<Member> findById(Long id);

  List<Member> findAll();
}
