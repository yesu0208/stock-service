# stock-service (주식 관리 서비스)
개별 유저의 관심종목을 그룹별로 관리할 수 있는 서비스이며, Github 계정을 이용하여 로그인 할 수 있습니다.
해당 종목의 매입가, 수량 등을 저장할 수 있으며, 실시간 종목 데이터를 외부 API로 받아와서 평가손익, 평가금액, 실현손익, 수익률을 자동으로 계산해서 제공합니다.
내 정보 페이지에서 수수료+세금의 합 비율을 설정할 수 있으며, 실현손익은 이에 기반하여 계산됩니다.
종목 정보 페이지에서 실시간 종목 정보(현재가, 변동가, 변동률, 거래량, 거래대금 등)을 확인할 수 있습니다.
종목 토론방을 통해 게시물과 댓글을 작성할 수 있으며, 좋아요와 싫어요를 누를 수 있습니다.
또한, 종목톡에서는 개별 유저가 채팅방(특정 종목에 대한)을 개설할 수 있으며 다수의 유저와 실시간 소통이 가능합니다. 채팅방에는 해당 종목에 대한 실시간 정보를 확인할 수 있습니다.

설계 및 제작 기간은 2025.08 ~ 2025.09(약 30일)이며, 설계부터 구현까지 100% 기여하였습니다.

베포 URL : https://stock-service-89300edadb9e.herokuapp.com/

## 개발 환경
- Intellij IDEA Ultimate
- JAVA 21
- Gradle 8.14.3
- Spring Boot 3.5.4

## 기술 세부 스택
Spring Boot
- Spring Boot DevTools
- Spring Web
- Spring Data JPA
- Spring Security OAuth2 Client
- H2 Database
- MySQL Driver
- Lombok
- WebSocket
- Thymeleaf
그 외
- AssertJ
- JUnit5
- Mockito
- Tailwind CSS
- Heroku

## Tools
- MySQL Workbench 8.0 CE
- Postman
- Git
- GitKraken

  
