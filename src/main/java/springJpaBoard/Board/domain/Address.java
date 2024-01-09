package springJpaBoard.Board.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springJpaBoard.Board.controller.requestdto.MemberRequestDTO;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Address {

    private String city;
    private String street;
    private String zipcode;


    @Builder
    public Address(String city, String street, String zipcode) {
//        /* 안전한 객체 생성인지 확인 */
//        Assert.hasText(city, "city must not be empty");
//        Assert.hasText(street, "street must not be empty");
//        Assert.hasText(zipcode, "zipcode must not be empty");

        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }

    public void updateAddress(MemberRequestDTO memberRequestDTO) {
        this.city = memberRequestDTO.getCity();
        this.street = memberRequestDTO.getStreet();
        this.zipcode = memberRequestDTO.getZipcode();
    }
}
