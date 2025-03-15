package org.younginhambak.backend.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.younginhambak.backend.member.dto.MemberCreateDto;
import org.younginhambak.backend.member.entity.Member;
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

  @Transactional
  @Override
  public void createMember(MemberCreateDto memberCreateDto) {
    Member member = Member.create(
            memberCreateDto.getName(),
            memberCreateDto.getEmail(),
            memberCreateDto.getNickname()
    );
    memberRepository.save(member);
  }

  @Transactional
  @Override
  public void updatMember() {

  }

  @Transactional
  @Override
  public void deleteMember() {

  }
}
