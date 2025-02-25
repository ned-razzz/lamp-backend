package org.younginhambak.backend.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.younginhambak.backend.member.Member;
import org.younginhambak.backend.member.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

  private final MemberRepository memberRepository;


  @Override
  public Optional<Member> getMember(Long memberId) {
    return memberRepository.findById(memberId);
  }

  @Override
  public List<Member> getMemberAll() {
    return memberRepository.findAll();
  }

  @Override
  @Transactional
  public void createMember() {

  }

  @Override
  @Transactional
  public void updatMember() {

  }

  @Override
  @Transactional
  public void deleteMember() {

  }
}
