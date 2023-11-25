package springJpaBoard.Board.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springJpaBoard.Board.controller.responsedto.MemberResponseDto;
import springJpaBoard.Board.domain.Address;
import springJpaBoard.Board.domain.Member;
import springJpaBoard.Board.domain.status.GenderStatus;
import springJpaBoard.Board.repository.MemberRepository;

@SpringBootTest //junit4의 @RunWith(SpringRunner.class)이 속해있음
@Transactional
class MemberServiceTest {
    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        Address address = new Address("1", "1","1");
        member.createMember("member", GenderStatus.남성, address);

        //when
        Long savedId = memberService.join(member);

        //then
        Assertions.assertEquals(member, memberRepository.findById(savedId).get());
        Assertions.assertEquals(member.getName(), "member");
    }

    @Test
    public void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        Address address = new Address("1", "1","1");
        member1.createMember("member", GenderStatus.여성, address);

        Member member2 = new Member();
        member2.createMember("member", GenderStatus.남성, address);

        //when
        memberService.join(member1);
        memberService.join(member2);

        //then
//        fail("예외가 발생해야 한다.");
    }

    @Test
    public void 회원_수정() throws Exception {
        //given
        Member member = new Member();
        Address address = new Address("1", "1","1");
        member.createMember("member1", GenderStatus.남성, address);

        Long savedId = memberService.join(member);

        //when
        Address address1 = new Address("2", "2", "2");
        MemberResponseDto memberDto = new MemberResponseDto();
        memberDto.UpdateBoard(savedId, "member2", GenderStatus.여성, address1);


        memberService.update(memberDto);
        Member findMember = memberService.findOne(savedId);

        //then
        Assertions.assertEquals(findMember.getId(), memberDto.getId());
        Assertions.assertEquals(findMember.getName(), memberDto.getName());
        Assertions.assertEquals(findMember.getGender(), memberDto.getGender());
        Assertions.assertEquals(findMember.getAddress(), memberDto.getAddress());

    }

}