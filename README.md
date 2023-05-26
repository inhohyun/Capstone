# Capstone design

## 설명
- 회사에서 업무를 처리할 때 생긴 데이터를 원하는 형태의 데이터로 바꿔주는 업무보조 프로그램
- 파이썬 서버를 활용해 웹 프로그래밍과 상호작용 
- open ai를 활용해 데이터를 이미지로 변환 가능
- 2차 로그인, 웹 로그인시 활용될 otp code 확인 기능 등 보안에 중점</br></br>

## 앱에서 적용한 전반적인 로직
- 사용자로부터 데이터를 입력받아 이를 json으로 파싱하여 서버에 보낸 후 서버로 부터 결과값을 받아 사용자에게 보여주는 형식

## 구현 목록 및 진행 상황 - 백엔드 서버가 있다는 가정하 진행
- 회원 가입, 로그인 기능 // clear
- 2차 로그인 기능 // clear
- 회원 정보 확인 // clear
- 30초주기 otp key를 서버로 보내고 otp code를 할당받음 // clear
- ocr 기능(이미지를 text 형태로 변환) // clear
- 멀티 데이터 기능(데이터 형식 변환 기능) // 5월 마지막 주 개발 예정(진행중)
- 서버와 통합 및 에러사항 찾기 (최종발표 가능)
- 추가사항 및 발생할 수 있는 버그 수정</br></br>

## 에러사항
- viewModel 접근시 앱 중단 -> viewModel 코드를 사이드 프로젝트로 빼서 실습해보기
- 30초 마다 서버 연동시 객체 생성(30초에 한 번씩 retroit 객체가 생성됨-> 메모리 낭비) // 수정 완료 : 싱글톤 활용
 </br></br>

## 서버와 연결 실패시 시도해볼 방안
- request를 보낼 시 데이터를 data class에 실어서 보내보기(적용함)
- 데이터를 보낼때 @Field -> @Body로 수정해서 보내보기(관련 공부 더 하기)
- 실패시 .Message()를 활용해 에러 메세지 확인하기


## 충동 피하기
- 작업하다 끝까지 못했어도 깃허브에 올려놓기(그 사이에 다른사람이 수정해놓으면 충돌)
- 작업시작할때 깃허브에서 업데이트 받고 시작하기(항상 최신 코드로 작업)
- 작업시간 안겹치게하기(같은 시간에 작업하면 업데이트가 안된 코드로 작업할 확률 높음, 보통 오전에 할듯?)



