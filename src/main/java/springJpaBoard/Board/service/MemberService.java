package springJpaBoard.Board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springJpaBoard.Board.domain.Member;
import springJpaBoard.Board.repository.MemberRepositoryImpl;
import springJpaBoard.Board.repository.dto.UpdateMemberDto;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepositoryImpl memberRepository;

    /**
     * 회원 가입
     */
    @Transactional(readOnly = false)
    public Long join(Member member) {
        memberRepository.save(member);
        return member.getId();
    }

    /**
     * 회원 전체 조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    /**
     * 회원 단건 조회
     */
    public Member findOne(Long id) {
        return memberRepository.findOne(id);
    }

    /**
     * 회원 정보 수정
     */
    @Transactional(readOnly = false)
    public void update(Long memberId, UpdateMemberDto memberDto) {
        Member findMember = memberRepository.findOne(memberId);
        /*
        Dirty Checking 발생, 가능하다면 Setter는 사용하지 않는 방법으로 구현
         */
        findMember.editMember(memberDto);
    }

    /**
     * 회원 삭제
     */

}
