package springJpaBoard.Board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springJpaBoard.Board.controller.responsedto.MemberResponseDTO;
import springJpaBoard.Board.domain.Member;
import springJpaBoard.Board.domain.status.GenderStatus;
import springJpaBoard.Board.repository.MemberRepository;

import java.util.List;

import static java.lang.Boolean.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

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
     * 회원 로그인
     * loginId와 password 정보가 정확하다면 회원 객체 반환
     * 틀리다면 null 반환
     */
    public Member login(String loginId, String password) {
        return memberRepository.findByLoginId(loginId)
                .filter(m -> m.getPassword().equals(password))
                .orElse(null);
    }

    public Boolean loginValidation(Member loginMember, Member BoardMember) {

        if ((loginMember.getLoginId().equals(BoardMember.getLoginId()) && (loginMember.getPassword().equals(BoardMember.getPassword())))) {
            return TRUE;
        }
        return FALSE;
    }

    /**
     * 중복 회원 검사
     */
    private void validateDuplicateMember(Member member) {
        //EXCEPTION
        List<Member> findMembers = memberRepository.findAllByName(member.getName());
        List<Member> findLoginId = memberRepository.findAllByLoginId(member.getLoginId());
        if (!findMembers.isEmpty() || !findLoginId.isEmpty()) {
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
     * 게시글 조회
     * Search
     */

    /* 회원 전체 조회 */
    public Page<Member> memberList(Pageable pageable) {
        return memberRepository.findAll(pageable);
    }

    /* 이름만 검색 */
    public Page<Member> searchName(String name, Pageable pageable) {
        return memberRepository.findByNameContaining(name, pageable);
    }

    /* 성별만 검색 */
    public Page<Member> searchGender(GenderStatus gender, Pageable pageable) {
        return memberRepository.findByGender(gender, pageable);
    }

    /* 제목, 성별 검색 */
    public Page<Member> searchAll(String name, GenderStatus gender, Pageable pageable) {
        return memberRepository.findByNameContainingAndGender(name, gender, pageable);
    }

    /**
     * 회원 단건 조회
     */
    public Member findOne(Long memberId) {
        return memberRepository.findById(memberId).get();
    }

    /**
     * 회원 정보 수정
     */
    @Transactional
    public Member update(Member member, MemberResponseDTO memberDto) {
        /*
        Dirty Checking 발생, 가능하다면 Setter는 사용하지 않는 방법으로 구현
         */
        member.editMember(memberDto);
        System.out.println("member.getName() = " + member.getName());
        return member;
    }

    /**
     * 회원 삭제
     */
    @Transactional
    public void delete(Long memberId) {
        memberRepository.deleteById(memberId);
    }


}
