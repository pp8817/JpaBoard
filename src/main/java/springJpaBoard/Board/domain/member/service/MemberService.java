package springJpaBoard.Board.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springJpaBoard.Board.domain.member.exception.MemberNotFoundException;
import springJpaBoard.Board.domain.member.exception.UserException;
import springJpaBoard.Board.domain.member.model.Member;
import springJpaBoard.Board.domain.member.repository.MemberRepository;
import springJpaBoard.Board.global.Error.exception.ErrorCode;

import java.util.List;
import java.util.Objects;

import static springJpaBoard.Board.domain.member.dto.MemberDto.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원 가입
     */
    @Transactional(readOnly = false)
    public Long join(CreateMemberRequest memberRequestDTO) {

        Member member = memberRequestDTO.toEntity();

        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    /**
     * 회원 로그인
     */

    public Member login(String loginId, String password) {
        Member member = memberRepository.findByLoginId(loginId).orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_USER));
        validationPassword(member, password);
        return member;
    }

    private static void validationPassword(Member member, String password) {
        if (!Objects.equals(member.getPassword(), password)) {
            throw new UserException(ErrorCode.INVALID_INPUT_ID_PASSWORD);
        }
    }

    public void loginValidation(Member loginMember, Member boardMember) {
        if ((!Objects.equals(loginMember.getLoginId(), boardMember.getLoginId()))||(!Objects.equals(loginMember.getPassword(), boardMember.getPassword()))) {
            throw new UserException(ErrorCode.USER_MISMATCH);
        }
    }

//    public Boolean loginValidation(Member loginMember, Member BoardMember) {
//
//        if ((loginMember.getLoginId().equals(BoardMember.getLoginId()) && (loginMember.getPassword().equals(BoardMember.getPassword())))) {
//            return TRUE;
//        }
//        return FALSE;
//    }

    /**
     * 중복 회원 검사
     */
    private void validateDuplicateMember(Member member) {
        //EXCEPTION
        List<Member> findMembers = memberRepository.findAllByName(member.getName());
        List<Member> findLoginId = memberRepository.findAllByLoginId(member.getLoginId());
        if (!findMembers.isEmpty() || !findLoginId.isEmpty()) {
            throw new UserException(ErrorCode.USER_ALREADY_EXISTS);
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
    public Page<Member> searchGender(String gender, Pageable pageable) {
        return memberRepository.findByGender(gender, pageable);
    }

    /* 제목, 성별 검색 */
    public Page<Member> searchAll(String name, String gender, Pageable pageable) {
        return memberRepository.findByNameContainingAndGender(name, gender, pageable);
    }

    /**
     * 회원 단건 조회
     */
    public Member findOne(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));
    }

    /**
     * 회원 정보 수정
     */
    @Transactional
    public MemberResponse update(Long memberId, ModifyMemberRequest memberDto) {
        /*
        Dirty Checking 발생, 가능하다면 Setter는 사용하지 않는 방법으로 구현
         */
        Member member = findOne(memberId);
        member.editMember(memberDto);

        return MemberResponse.of(member);
    }

    /**
     * 회원 삭제
     */
    @Transactional
    public void delete(Long memberId) {
        memberRepository.deleteById(memberId);
    }

}
