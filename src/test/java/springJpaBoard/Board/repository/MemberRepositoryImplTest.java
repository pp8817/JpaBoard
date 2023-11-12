package springJpaBoard.Board.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import springJpaBoard.Board.domain.GenderStatus;
import springJpaBoard.Board.domain.Member;

import java.util.List;

//@ExtendWith(SpringExtension.class)
@SpringBootTest //junit4의 @RunWith(SpringRunner.class)이 속해있음
@Transactional
class MemberRepositoryImplTest {

    @Autowired
    MemberRepositoryImpl memberRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void testMember() throws Exception {
        //given
        Member member = new Member();
        member.setName("memberA");
        member.setGender(GenderStatus.Man);

        //when
        memberRepository.save(member);
        Member findMember = memberRepository.findOne(member.getId());

        //then
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getName()).isEqualTo(member.getName());
        assertThat(findMember.getGender()).isEqualTo(member.getGender()); //JPA 엔티티 동일성 보장
    }

}