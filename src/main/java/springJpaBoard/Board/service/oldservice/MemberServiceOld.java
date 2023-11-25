package springJpaBoard.Board.service.oldservice;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import springJpaBoard.Board.controller.responsedto.MemberResponseDto;
import springJpaBoard.Board.domain.Member;
import springJpaBoard.Board.repository.oldRepository.MemberRepositoryImplOld;
import springJpaBoard.Board.repository.search.MemberSearch;

import java.util.List;

//@Service()
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceOld {

    private final MemberRepositoryImplOld memberRepository;

    /**
     * 회원 가입
     */
    @Transactional(readOnly = false)
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    /**
     * 중복 회원 검사
     */
    private void validateDuplicateMember(Member member) {
        //EXCEPTION
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     * 회원 전체 조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    /**
     * 회원 전체 조회(검색) - 검색 기능이 포함된 회원 목록 조회
     * controller에서 DTO로 변환 필요
     */
    public List<Member> findSearchMembers(MemberSearch memberSearch) {
        return memberRepository.findAll2(memberSearch);
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
    public void update(MemberResponseDto memberDto) {
        Member findMember = memberRepository.findOne(memberDto.getId());
        /*
        Dirty Checking 발생, 가능하다면 Setter는 사용하지 않는 방법으로 구현
         */
        findMember.editMember(memberDto);
    }

    /**
     * 회원 삭제
     */
    @Transactional
    public void delete(Long memberId) {
        memberRepository.delete(memberId);
    }

}
