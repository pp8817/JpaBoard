# JpaBoard
Spring data Jpa를 활용해서 Restful 게시판을 만듭니다.
- 회원가입 / 로그인 기능은 cookie, session 방식을 이용했습니다. 추후 Spring Security를 공부 후 변경 할 예정입니다.
- 23.12.23 - 대략 완성 후 코드를 다듬고 API를 공부하며 개발하고 있습니다.
- 24.01.05 - 양방향 연관관계로 엮여있는 것을 최대한 단방향으로 변경할 계획중

## 기술
- Framework & Language : Spring Boot 3.0 & Java 17
- ORM : JPA, Spring Data JPA, QueryDSL 
- DB : H2 2.2.224
- API Test: PostMan
- Dependency: Thymeleaf, Spring Validation, Spring Web, Lombok, Spring Data JPA

### ERD (23.12.31 업데이트)
<img width="480" alt="스크린샷 2023-12-31 19 33 45" src="https://github.com/pp8817/JpaBoard/assets/71458064/55598ebf-81a1-49e3-9c63-ec064b7a31f2">
