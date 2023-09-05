<div align="center">
  <br>
  <img width="273" alt="image" src="https://github.com/khsrla9806/popple-backend/assets/70641477/f8b29521-437e-44ee-8933-e4ccb109b996">
  <br>
  <h1>🛒 Popple (팝업 스토어, 플리마켓 플랫폼)</h1>
  <br>
</div>

<br><br>

## 🗓️ 프로젝트 기간
`2023-06 ~ 2023-09`

<br><br>

## 🙌🏻 프로젝트 설명
- 모든 오프라인 행사에 대한 정보를 하나로 통합하고, 회원과 판매자가 모두 한곳에서 소통할 수 있는 플랫폼을 위한 프로젝트

<br><br>

## 🎯 프로젝트 목적
- 전국에서 열리는 팝업 스토어, 플리마켓과 같은 정보를 한 곳에서 확인할 수 있는 플랫폼을 구성하기 위함
- 스토어에 대한 지역별, 연령별 수요조사를 필요한 곳(장소 제공자, 판매자)에서 사용할 수 있도록 제공하기 위함
- 소상공인(개인 판매자)이 더욱 활발한 판매, 홍보 활동을 할 수 있도록 도와주기 위함
- 개인 판매자(플리마켓, 팝업 스토어, 체험 부스 등)와 회원(소비자) 사이의 상호작용을 할 수 있는 커뮤니티 공간을 제공하기 위함

<br><br>

## 👨‍👨‍👦‍👦 개발 팀원
| Back-End 👑 | Back-End | Back-End | Front-End 👑 | Front-End | Front-End | Front-End |
| :-----------------: | :--------: | :--------: | :-------: | :-------: | :-------: | :-------: |
| <img src="https://avatars.githubusercontent.com/u/109710879?v=4" width="150"> | <img src="https://avatars.githubusercontent.com/u/70641477?v=4" width="150" width="150">  | <img src="https://avatars.githubusercontent.com/u/93474297?v=4" width="150"> | <img src="https://avatars.githubusercontent.com/u/87072568?v=4" width="150"> | <img src="https://avatars.githubusercontent.com/u/108085046?v=4" width="150">  | <img src="https://avatars.githubusercontent.com/u/83483378?v=4" width="150">  | <img src="https://avatars.githubusercontent.com/u/108586797?v=4" width="150">  |
| [최태윤](https://github.com/cxxxtxxyxx)            | [김훈섭](https://github.com/khsrla9806)   | [오형석](https://github.com/brotherstone97)   | [이정우](https://github.com/howooking)  | [이은비](https://github.com/EungBug)  | [유희태](https://github.com/1017yu)  | [이대양](https://github.com/oceanlee-seoul)  |

<br><br>

## 🛠️ 사용 기술
### 백엔드
- **Language & Framework**

  - Java (JDK 11)
  - SpringBoot (2.7.13)
  - Spring Security
  - Spring Data JPA
 
- **Build**

  - Gradle
 
- **Database**

  - MySQL (8.0.33)
  - Redis
 
- **AWS**

  - EC2
  - S3

- **Library**

  - Lombok
  - Junit5
  - JWT
 
- **Tool & Collaboration**

  - IntelliJ IDEA
  - Postman
  - Notion
  - Discord
  - Git / Github
  - Figma

<br>
 
### 프론트엔드
- **Language & Framework**

  - Typescript
  - React.js
 
- **AWS**

  - SDK

- **Library**

  - Tailwind CSS
  - Recoil
  - ViteJS
  - Axios
  - NPM
 
- **Deployment**

  - Netlify
 
- **Tool**

  - Visual Studio Code
  - Postman
  - Notion
  - Discord
  - Git / Github
  - Figma

<br><br>

## 🧑🏻‍💻 팀원 역할
| 이름 | 역할 |
| :-----------------: | -------------------------------- |
| 최태윤<br>`팀장`   | - **일반 유저, 개인 판매자 인증 인가 구현**<br>&nbsp;&nbsp;└ 자체 서비스 로그인 구현<br>&nbsp;&nbsp;└ 카카오 로그인 구현<br>&nbsp;&nbsp;└ 이메일 인증 구현<br>&nbsp;&nbsp;└ 토큰 기반 인증(AccessToken + RefreshToken) + Redis 활용<br>&nbsp;&nbsp;└ 인증에 필요한 외부 API 사용<br>- Database ERD 설계<br>- API 명세 설계<br>- EC2 서버 배포   |
| 김훈섭            | - **행사 수요조사 기능 구현**<br>&nbsp;&nbsp;└ 관리자 수요조사 등록, 수정, 삭제 기능 구현<br>&nbsp;&nbsp;└ 회원 수요조사 응답 제출 기능 구현<br>&nbsp;&nbsp;└ 매일 0시 정각에 작동 되는 수요조사 스케쥴러 구현<br>&nbsp;&nbsp;└ 수요조사 결과 통계 기능 구현<br>- **행사 기능 구현**<br>&nbsp;&nbsp;└ 판매자 행사 등록, 수정, 삭제 구현<br>&nbsp;&nbsp;└ 매일 0시 정각에 작동하여 행사 상태를 정리하는 스케쥴러 구현<br>&nbsp;&nbsp;└ 행사 목록 페이징 조회, 상세 조회 구현<br>- Database ERD 설계<br>- API 명세 설계<br>- EC2 서버 배포<br>- 배포 스크립트 작성   |
| 오형석            | - **큰 기능**<br>&nbsp;&nbsp;└ 소기능1<br>&nbsp;&nbsp;└ 소기능2<br>&nbsp;&nbsp;└ 소기능3<br>&nbsp;&nbsp;└ 소기능4<br>&nbsp;&nbsp;└ 소기능5<br>- Database ERD 설계<br>- API 명세 설계<br>- EC2 서버 배포   |

<br><br>

## 📋 ERD 설계
![image](https://github.com/FastSubTeam/server/assets/70641477/220d47b0-72fc-41e2-b741-0a13aad42936)

<br><br>

## 📚 API 명세서
> [API 명세서 페이지로 이동](https://frosted-loan-a1d.notion.site/48a024320cb04c5b9c4431ba12dae6f1?v=0d19c76f9d774871a2e9a53b877f8431&pvs=4)

### 행사 API
<img width="672" alt="image" src="https://github.com/FastSubTeam/server/assets/70641477/94b03bfe-d2d9-405e-af15-1f8dadf03cf9">

### 수요조사 API
<img width="672" alt="image" src="https://github.com/FastSubTeam/server/assets/70641477/c6dbc9a3-ce64-41df-a090-08ca18822daf">

### 인증/인가 API
<img width="672" alt="image" src="https://github.com/FastSubTeam/server/assets/70641477/aec31f74-3a3b-482f-b452-aee4ac12cc1f">

### 회원/커뮤니티 API
<img width="672" alt="image" src="https://github.com/FastSubTeam/server/assets/70641477/5089a63a-89e2-4d3a-b403-fc070c58c13b">

<br><br>

## 🖥️ 프로젝트 주요 페이지
| 사용자 회원가입 페이지 | 개인 판매자 회원가입 페이지 |
| ------------------ | -------------------- |
| ![image](https://github.com/FastSubTeam/server/assets/70641477/10f59f9d-ac85-49f2-a993-855464c8b5c9)          | ![image](https://github.com/FastSubTeam/server/assets/70641477/00627b0a-0c92-4164-9ef4-ca595662f7e9)            |

| 로그인 페이지 | 메인 페이지(+ 수요조사 팝업) |
| ------------------ | -------------------- |
| ![image](https://github.com/FastSubTeam/server/assets/70641477/f139b0e1-23d1-4f0e-9be2-a7d89b299f01)          | ![image](https://github.com/FastSubTeam/server/assets/70641477/05926bd4-4375-4453-ae31-a20f51f6cdde)            |

| 행사 목록 페이지 | 행사 등록 페이지 |
| ----------------------------- | ------------------------------- |
| ![image](https://github.com/FastSubTeam/server/assets/70641477/e977f72f-bfeb-4492-851d-ae35b6e356c7)               | ![image](https://github.com/FastSubTeam/server/assets/70641477/f23aad91-1fda-457d-bb6b-564d3138f2c4)                       |

| 행사 상세 조회 페이지 | 커뮤니티 목록 페이지 |
| ----------------------------- | ------------------------------- |
| ![image](https://github.com/FastSubTeam/server/assets/70641477/21b9dc6e-1f52-4da0-8363-3c2129300420)                | ![image](https://github.com/FastSubTeam/server/assets/70641477/0ca833f9-f82f-40b0-9d17-f70435fc3633)                       |

| 커뮤니티 게시글 등록 페이지 | 커뮤니티 상세 페이지 |
| ------------------- | --------------------- |
| ![image](https://github.com/FastSubTeam/server/assets/70641477/6e9d01b2-7f2f-4c83-82d9-17b13d1e3f07)               | ![image](https://github.com/FastSubTeam/server/assets/70641477/b8d0afcb-1048-45bf-a5d4-947531664e4c)             |

| 수요조사 결과 목록 페이지 (최근 10개) | 수요조사 결과 상세 페이지 |
| ------------------- | --------------------- |
| ![image](https://github.com/FastSubTeam/server/assets/70641477/68d7f4cd-1625-469b-81fc-ae231d3f14c9)           | ![image](https://github.com/FastSubTeam/server/assets/70641477/11bb39a8-8d22-46b2-ab29-20b206654347)             |

| 수요조사 응답 페이지 | 관리자 수요조사 관리 페이지 |
| ------------------- | --------------------- |
| ![image](https://github.com/FastSubTeam/server/assets/70641477/afe42dd3-5b7d-400e-9374-930c0f98b610)           | ![image](https://github.com/FastSubTeam/server/assets/70641477/2289ae58-37a7-44d5-a8fb-685afcfd87a1)             |

| 관리자 수요조사 등록 페이지 |
| ------------------- |
| ![image](https://github.com/FastSubTeam/server/assets/70641477/f1fc527d-7a70-46ae-97c0-c3d8e64f6f81)           |

