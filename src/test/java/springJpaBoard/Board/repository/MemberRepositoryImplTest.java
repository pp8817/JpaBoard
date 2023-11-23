package springJpaBoard.Board.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springJpaBoard.Board.domain.Address;
import springJpaBoard.Board.domain.status.GenderStatus;
import springJpaBoard.Board.domain.Member;

import static org.assertj.core.api.Assertions.assertThat;

//@ExtendWith(SpringExtension.class)
@SpringBootTest //junit4의 @RunWith(SpringRunner.class)이 속해있음
@Transactional
class MemberRepositoryImplTest {

    @Autowired
    MemberRepositoryImpl memberRepository;

    @Test
    public void testMember() throws Exception {
        //given
        Member member = new Member();
        Address address = new Address("1", "1","1");
        member.createMember("memberA", GenderStatus.남성, address);

        //when
        memberRepository.save(member);
        Member findMember = memberRepository.findOne(member.getId());

        //then
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getName()).isEqualTo(member.getName());
        assertThat(findMember.getGender()).isEqualTo(member.getGender()); //JPA 엔티티 동일성 보장
    }

}