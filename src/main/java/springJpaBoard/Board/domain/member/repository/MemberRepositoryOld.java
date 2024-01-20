package springJpaBoard.Board.domain.member.repository;

import springJpaBoard.Board.domain.member.model.Member;

import java.util.List;

public interface MemberRepositoryOld {

    // 회원 등록
    void save(Member member);

    // 회원 단건 조회a
    Member findOne(Long memberId);

    // 회원 전체 조회
    List<Member> findAll();

    //이름으로 회원 조회
    List<Member> findByName(String name);

//    // 회원 수정
//    void update(Long memberId, UpdateBoardDto updateMember);

    // 회원 삭제
    void delete(Long memberId);

}
