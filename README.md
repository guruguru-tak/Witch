# 🧙WITCH

<img width="100%" alt="썸네일" src="https://github.com/user-attachments/assets/63168c57-07bd-4d9b-8331-2660a88aac88" />
<div align=center>

#### WITCH는 약속 시작 1시간 전부터 친구들의 위치를 실시간으로 공유하는 서비스입니다.<br> 함께하는 만남을 간편하게 관리하고, 스낵을 공유하며 즐거운 하루를 만들어보세요!

#### 🗓️2025.01.06 ~ 2025.02.21 (7주)

</div>

## 🧙주요 기능

#### 🍪실시간 위치 공유

- `Android` 기기에 내장된 `GPS` 기능을 활용해 사용자 간에 현재 위치를 실시간 공유
- 약속 시작 1시간 전부터 사용자의 위치를 `지도`에 표시해 지각 및 길 찾기 문제 최소화

#### 🍪스낵(Snack)을 통한 흥미 유발

- 일정 시간 동안만 공유 가능한 스토리형 콘텐츠 `스낵`을 통해 사용자 간 재미있는 소통 활성화
- 약속 장소 근처에서만 스낵 생성 및 조회가 가능하도록 `위치 기반 제한`을 적용해 적극적인 참여 유도

#### 🍪사용자 편의성을 고려한 약속 관리 기능

- `캘린더`를 통해 월별, 주별, 일자별 약속을 `직관적`으로 확인 가능
- 캘린더의 약속 클릭 시 `상세 정보`를 즉시 확인 가능
- 예정, 진행 중, 종료 등 `약속 상태`를 `태그`와 `FCM 데이터 메시지`로 구분해 관리 편의성 극대화

## 🧙기술 스택

### Back-End

<img src="https://img.shields.io/badge/spring-%236DB33F.svg?&style=for-the-badge&logo=spring&logoColor=white" /> <img src="https://img.shields.io/badge/Spring%20Boot-6DB33F.svg?style=for-the-badge&logo=springboot&logoColor=white" />
<img src="https://img.shields.io/badge/Spring%20Security-6DB33F.svg?style=for-the-badge&logo=springsecurity&logoColor=white" /> <img src="https://img.shields.io/badge/mysql-%234479A1.svg?&style=for-the-badge&logo=mysql&logoColor=white" /> <img src="https://img.shields.io/badge/redis-%23DC382D.svg?&style=for-the-badge&logo=redis&logoColor=white" /> 
<img src="https://img.shields.io/badge/apache%20kafka-%23231F20.svg?&style=for-the-badge&logo=apache%20kafka&logoColor=white" /> <img src="https://img.shields.io/badge/firebase-%23FFCA28.svg?&style=for-the-badge&logo=firebase&logoColor=black" /> <img src="https://img.shields.io/badge/Amazon S3-569A31?&style=for-the-badge&logo=amazons3&logoColor=white" /> <img src="https://img.shields.io/badge/Amazon EC2-FF9900?&style=for-the-badge&logo=amazonec2&logoColor=black" />

### Android

<img src="https://img.shields.io/badge/android%20studio-%233DDC84.svg?&style=for-the-badge&logo=android%20studio&logoColor=white" /> <img src="https://img.shields.io/badge/google%20maps-%234285F4.svg?&style=for-the-badge&logo=google%20maps&logoColor=white" />

### CI/CD

<img src="https://img.shields.io/badge/docker-%232496ED.svg?&style=for-the-badge&logo=docker&logoColor=white" /> <img src="https://img.shields.io/badge/kubernetes-%23326CE5.svg?&style=for-the-badge&logo=kubernetes&logoColor=white" /> <img src="https://img.shields.io/badge/helm-%230F1689.svg?&style=for-the-badge&logo=helm&logoColor=white" /> <img src="https://img.shields.io/badge/jenkins-%23D24939.svg?&style=for-the-badge&logo=jenkins&logoColor=white" /> <img src="https://img.shields.io/badge/Argo-EF7B4D?style=for-the-badge&logo=argo&logoColor=white"/>

### Co-Work

<img src="https://img.shields.io/badge/gitlab-%23FCA121.svg?&style=for-the-badge&logo=gitlab&logoColor=orange" /><img src="https://img.shields.io/badge/jira-%230052CC.svg?&style=for-the-badge&logo=jira&logoColor=white" /><img src="https://img.shields.io/badge/notion-%23000000.svg?&style=for-the-badge&logo=notion&logoColor=white" /><img src="https://img.shields.io/badge/mattermost-%230072C6.svg?&style=for-the-badge&logo=mattermost&logoColor=white" /><img src="https://img.shields.io/badge/figma-%23F24E1E.svg?&style=for-the-badge&logo=figma&logoColor=white" />


## 🧙디렉토리 구조

### Back-End

```
witch
|-- 📦infrastructure
|-- witch-adapters
|   |-- 📦adapter-event
|   |-- 📦adapter-fcm
|   |-- 📦adapter-generator
|   |-- 📦adapter-kafka-publisher
|   |-- 📦adapter-mail
|   |-- 📦adapter-persistence
|   |-- 📦adapter-redis
|   |-- 📦adapter-s3
|   `-- 📦adapter-security
|-- witch-apps
|   |-- 📦app-api
|   `-- 📦kafka-subscriber
|-- 📦witch-commons
|-- witch-cores
|   |-- 📦core-domain
|   |-- 📦core-port
|   |-- 📦core-service
|   `-- 📦core-usecase
`-- 📦witch-tests
```

### Android

```
witch
|-- 📦base
|-- data
|   |-- 📦local
|   |-- 📦model
|   `-- 📦remote
|-- ui
|   |-- 📦appointment
|   |-- 📦auth
|   |-- 📦group
|   |-- 📦home
|   |-- 📦mypage
|   `-- 📦snack
`-- 📦util
```

## 🧙시스템 아키텍쳐

![Image](https://github.com/user-attachments/assets/f03822e0-e9a6-4861-a960-939fa6b134cd)

## 🧙화면 구성

#### 📌회원가입

![회원가입](https://github.com/user-attachments/assets/e95f137b-09e6-478d-b3ab-a48e02e3c018)

#### 📌로그인 및 로그아웃

![로그인/로그아웃](https://github.com/user-attachments/assets/f4805372-e532-4bb4-bfbf-08907854c696)

#### 📌모임 생성

![모임 생성](https://github.com/user-attachments/assets/3248c209-5511-4996-ad8a-69aaa58742c8)

#### 📌약속 생성

![약속 생성](https://github.com/user-attachments/assets/96740155-7f60-47de-b214-678376db28b1)

#### 📌약속 조회

![약속 조회](https://github.com/user-attachments/assets/07459542-2397-495e-b1c9-91647bc604ad)

#### 📌실시간 위치 조회

![실시간 위치 조회](https://github.com/user-attachments/assets/351e5a9b-30e4-464f-ac43-627188893d80)

#### 📌스낵 생성

![스낵 생성](https://github.com/user-attachments/assets/3178c2fb-7897-40cd-b8e6-807813b77906)

#### 📌스낵 조회

![스낵 조회](https://github.com/user-attachments/assets/faa8f223-28fb-44d7-b26c-dbb12b123edf)

## 🧙팀원 소개

<table style="width: 100%; table-layout: fixed;">
  <tr>
    <td align="center" valign="middle">
      <img src="https://github.com/user-attachments/assets/2a85ce03-799c-4369-ae38-0dc19276acc1" alt="Image" style="width: 100%; height: 100%; object-fit: cover;">
    </td>
    <td align="center" valign="middle">
      <img src="https://github.com/user-attachments/assets/e3687799-72a5-42b4-823b-8a46a7d0d0be" alt="Image" style="width: 100%; height: 100%; object-fit: cover;">
    </td>
    <td align="center" valign="middle">
      <img src="https://github.com/user-attachments/assets/35a47f05-eca1-4333-b6a3-d5d30a8cbe4a" alt="Image" style="width: 100%; height: 100%; object-fit: cover;">
    </td>
    <td align="center" valign="middle">
      <img src="https://github.com/user-attachments/assets/eb28d3d5-415b-430f-bec4-4ebfa2d1f10f" alt="Image" style="width: 450px; height: 150px; object-fit: cover;">
    </td>
    <td align="center" valign="middle">
      <img src="https://github.com/user-attachments/assets/1b20a1f6-c1f7-47dd-8d1b-03f71c03f657" alt="Image" style="width: 100%; height: 100%; object-fit: cover;">
    </td>
    <td align="center" valign="middle">
      <img src="https://github.com/user-attachments/assets/923b8ca8-ee88-4815-b8b2-131e24ef3058" alt="Image" style="width: 100%; height: 100%; object-fit: cover;">
    </td>
  </tr>
  <tr style="background: #f5f5f5;">
    <td align="center" valign="middle">Back-End</td>
    <td align="center" valign="middle">Back-End</td>
    <td align="center" valign="middle">Back-End</td>
    <td align="center" valign="middle">Infra</td>
    <td align="center" valign="middle">Android</td>
    <td align="center" valign="middle">Android</td>
  </tr>
  <tr>
    <td align="center" valign="middle">김덕윤👑</td>
    <td align="center" valign="middle">채용수</td>
    <td align="center" valign="middle">태성원</td>
    <td align="center" valign="middle">권경탁</td>
    <td align="center" valign="middle">남수정</td>
    <td align="center" valign="middle">임수미</td>
  </tr>
</table>
