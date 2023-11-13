package springJpaBoard.Board.repository;

import springJpaBoard.Board.domain.Member;

import java.util.List;

public interface MemberRepository {

    // 회원 등록
    void save(Member member);

    // 회원 단건 조회
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
